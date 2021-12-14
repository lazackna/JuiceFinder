package com.lazackna.juicefinder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.lazackna.juicefinder.databinding.ActivityMapBinding;
import com.lazackna.juicefinder.util.juiceroot.Feature;
import com.lazackna.juicefinder.util.juiceroot.JuiceRoot;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.HashMap;

public class MapActivity extends AppCompatActivity {

    private ActivityMapBinding binding;
    private JuiceRoot juiceRoot;
    private HashMap<Marker, Feature> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(this, getSharedPreferences("osm_config", Context.MODE_PRIVATE));
        binding = ActivityMapBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        map = new HashMap<>();

        setupMap();

        Bundle bundle = getIntent().getExtras();
        juiceRoot = (JuiceRoot) bundle.get("juice");

        fillMap();

    }

    private void setupMap() {
        binding.map.setTileSource(TileSourceFactory.MAPNIK);
        MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(this), binding.map);
        locationNewOverlay.enableMyLocation();
        binding.map.getOverlays().add(locationNewOverlay);
        binding.map.getController().zoomTo(14.0d);
        binding.map.setMultiTouchControls(true);

    }

    private void fillMap() {
        for  (Feature f : juiceRoot.features) {
            double[] coords = f.geometry.coordinates;
            GeoPoint point = new GeoPoint(coords[1], coords[0]);
            Marker marker = new Marker(binding.map);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_LEFT, Marker.ANCHOR_LEFT);
            marker.setInfoWindow(null);
            marker.setIcon(ResourcesCompat.getDrawable(this.getResources(), R.drawable.charger_icon, null));
            binding.map.getOverlays().add(marker);
            map.put(marker,f);
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {

                    Feature f = map.get(marker);

                    return false;
                }
            });
        }
        double[] coords = juiceRoot.features[0].geometry.coordinates;
        GeoPoint point = new GeoPoint(coords[1], coords[0]);

        binding.map.getController().setCenter(point);
    }


}