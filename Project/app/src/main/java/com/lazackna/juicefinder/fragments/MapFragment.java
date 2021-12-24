package com.lazackna.juicefinder.fragments;

import android.location.Location;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.lazackna.juicefinder.MainActivity;
import com.lazackna.juicefinder.OnMarkerClickListener;
import com.lazackna.juicefinder.R;
import com.lazackna.juicefinder.databinding.FragmentMapBinding;
import com.lazackna.juicefinder.util.API.ApiHandler;
import com.lazackna.juicefinder.util.API.OpenChargeMapRequestBuilder;
import com.lazackna.juicefinder.util.FilterSettings;
import com.lazackna.juicefinder.util.GPS.GPSManager;
import com.lazackna.juicefinder.util.GPS.IGPSSubscriber;
import com.lazackna.juicefinder.util.IRootCallback;
import com.lazackna.juicefinder.util.MapThread;
import com.lazackna.juicefinder.util.API.DownloadRoadTask;
import com.lazackna.juicefinder.util.juiceroot.Feature;
import com.lazackna.juicefinder.util.juiceroot.JuiceRoot;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.PolyOverlayWithIW;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements IGPSSubscriber, IRootCallback {

    private static final String TAG = MapFragment.class.getName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentMapBinding binding;

    private ApiHandler apiHandler;
    private HashMap<Marker, Feature> markerMap;

    private GPSManager manager;
    private Location lastLocation;
    private boolean firstUpdate = false;

    private MapThread mapThread;
    private static int selectedMarker = 0;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        apiHandler = ApiHandler.getInstance(getContext());
    }

    private void initializeMap() {

        this.markerMap = new HashMap<>();

        binding.map.setTileSource(TileSourceFactory.MAPNIK);
        MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(requireContext()), binding.map);
        locationNewOverlay.enableMyLocation();
        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.car);
        locationNewOverlay.setDirectionArrow(icon, icon);
        locationNewOverlay.setPersonIcon(icon);
        binding.map.getOverlays().add(locationNewOverlay);
        binding.map.getController().zoomTo(14.0d);
        binding.map.setMultiTouchControls(true);

    }

    private void clearMap() {
        if (binding.map == null) return;
        Iterator<Overlay> iterator = binding.map.getOverlays().iterator();
        while(iterator.hasNext()) {
            Overlay o = iterator.next();
            if (o instanceof Marker) {
                binding.map.getOverlays().remove(o);
            }
        }
        markerMap.clear();
    }

    public void setMapInteraction(boolean isInactive){
        binding.map.setOnTouchListener((view, motionEvent) -> isInactive);

        if (isInactive)
            binding.map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        else
            binding.map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
    }

    private int markersPut;
    private void fillMap(JuiceRoot root) {
        clearMap();
        this.markersPut = 0;
        FilterSettings settings = MainActivity.viewModel.getSettings().getValue();

        for  (Feature f : root.features) {
            if (settings != null)
            if (markersPut >= settings.maxResults) break;
            double[] coords = f.geometry.coordinates;
            GeoPoint point = new GeoPoint(coords[1], coords[0]);
            Marker marker = new Marker(binding.map);
            marker.setPosition(point);
            marker.setAnchor(0.2f, 0.2f);
            marker.setInfoWindow(null);
            marker.setIcon(ResourcesCompat.getDrawable(this.getResources(), R.drawable.charger_icon, null));
            this.binding.map.getOverlays().add(marker);
            this.markerMap.put(marker,f);
            markersPut++;
            marker.setOnMarkerClickListener((marker1, mapView) -> {
                //TODO only select top marker.
                Feature f1 = markerMap.get(marker1);
                if (getActivity() instanceof OnMarkerClickListener) {
                    OnMarkerClickListener a = (OnMarkerClickListener) getActivity();
                    a.onClick(f1);
                }
                return false;
            });
        }
        try {
            double[] coords = root.features[0].geometry.coordinates;
            GeoPoint point = new GeoPoint(coords[1], coords[0]);

            binding.map.getController().setCenter(point);
        } catch (Exception e) {

        }
    }

    public void drawRouteFromUser(GeoPoint geoPoint, int color){
        if(lastLocation != null) {
            ArrayList<GeoPoint> points = new ArrayList<>();
            points.add(new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()));
            points.add(geoPoint);
            try {
                Road road = new DownloadRoadTask(getContext(), points).execute().get();
                drawRouteOnMap(road, color);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void drawRouteOnMap(@NotNull Road road, int color) {
        Log.i(TAG, "Drawing route");
        PolyOverlayWithIW overlay = RoadManager.buildRoadOverlay(road, color, 20f);
        this.binding.map.getOverlays().add(overlay);
        this.binding.map.invalidate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false);

        initializeMap();
        initializeGPS();
        MainActivity.viewModel.getSettings().observe(getViewLifecycleOwner(), s -> {
            if (lastLocation == null) return;
            showRetrievingMessage();
            this.mapThread = new MapThread(lastLocation, this.apiHandler, TAG, this, s);
            this.mapThread.start();
        });
        //this.firstUpdate = false;

        return binding.getRoot();
    }

    private void initializeGPS() {
        this.manager = GPSManager.getInstance(getContext());
        this.manager.subscribe(this);
        this.manager.start(getContext());
    }

    private void showRetrievingMessage() {
        View view = getView();
        if (view != null)
            Snackbar.make(view, "Retrieving charging points", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void notifyLocationChanged(Location location) {
        if (location == null) return;
        lastLocation = location;

        if (!firstUpdate) {
            firstUpdate = true;
            showRetrievingMessage();
            this.mapThread = new MapThread(location, this.apiHandler, TAG, this, new FilterSettings());
            this.mapThread.start();
        }

        GeoPoint g = new GeoPoint(location);
        this.binding.map.getController().animateTo(g);
    }

    @Override
    public void notifyRoot(JuiceRoot root) {
        if (root != null) {
            fillMap(root);
        }
    }
}