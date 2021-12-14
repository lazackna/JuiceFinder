package com.lazackna.juicefinder.fragments;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.lazackna.juicefinder.MainActivity;
import com.lazackna.juicefinder.OnMarkerClickListener;
import com.lazackna.juicefinder.R;
import com.lazackna.juicefinder.databinding.FragmentMapBinding;
import com.lazackna.juicefinder.util.API.ApiHandler;
import com.lazackna.juicefinder.util.juiceroot.Feature;
import com.lazackna.juicefinder.util.juiceroot.JuiceRoot;

import org.json.JSONObject;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

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
        binding.map.getOverlays().add(locationNewOverlay);
        binding.map.getController().zoomTo(14.0d);
        binding.map.setMultiTouchControls(true);



        this.apiHandler.makeVolleyObjectRequest(
                response -> {
                    Gson gson = new Gson();
                    JuiceRoot root = gson.fromJson(response.toString(), JuiceRoot.class);
                    Log.d(TAG,"received juice root with length: " + root.features.length);
                    fillMap(root);
                },
                null
        );
    }

    public void setMapInteraction(boolean isInactive){
        binding.map.setOnTouchListener((view, motionEvent) -> isInactive);

        if (isInactive)
            binding.map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        else
            binding.map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
    }

    private void fillMap(JuiceRoot root) {
        for  (Feature f : root.features) {
            double[] coords = f.geometry.coordinates;
            GeoPoint point = new GeoPoint(coords[1], coords[0]);
            Marker marker = new Marker(binding.map);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_LEFT);
            marker.setInfoWindow(null);
            marker.setIcon(ResourcesCompat.getDrawable(this.getResources(), R.drawable.charger_icon, null));
            this.binding.map.getOverlays().add(marker);

            this.markerMap.put(marker,f);
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    //TODO only select top marker.
                    Feature f = markerMap.get(marker);
                    if (getActivity() instanceof OnMarkerClickListener) {
                        OnMarkerClickListener a = (OnMarkerClickListener) getActivity();
                        a.onClick(f);
                    }
                    return false;
                }
            });
        }
        double[] coords = root.features[0].geometry.coordinates;
        GeoPoint point = new GeoPoint(coords[1], coords[0]);

        binding.map.getController().setCenter(point);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false);

        initializeMap();

        return binding.getRoot();
    }
}