package com.lazackna.juicefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.lazackna.juicefinder.databinding.ActivityMainBinding;
import com.lazackna.juicefinder.fragments.MapFragment;
import com.lazackna.juicefinder.fragments.PopupFragment;
import com.lazackna.juicefinder.util.juiceroot.Feature;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;

public class MainActivity extends AppCompatActivity implements OnMarkerClickListener{

    private ActivityMainBinding binding;
    private FragmentManager manager;
    private boolean popupActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        initialize();
    }

    private void clearBackstack() {
        int backstack = this.manager.getBackStackEntryCount();

        for (int i = 0; i < backstack; i++) {
            this.manager.popBackStack();
        }
    }

    private void initialize() {
        Configuration.getInstance().setUserAgentValue("JuiceFinder");

        this.manager = getSupportFragmentManager();
        clearBackstack();
        makeMapFragment();

    }

    private void makeMapFragment() {
        Bundle bundle = new Bundle();
        this.manager.beginTransaction()
                .setReorderingAllowed(false)
                .add(binding.fragmentHolder.getId(), MapFragment.class, null, "mapFragment")
                .commit();
    }

    private void makePopupFragment(Feature f) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("feature", f);

        this.manager.beginTransaction()
                .setReorderingAllowed(false)
                .add(binding.fragmentHolder.getId(), PopupFragment.class, bundle, "popupFragment")
                .addToBackStack("popup")
                .commit();
        popupActive = true;
        MapFragment mapFragment = (MapFragment) this.manager.findFragmentByTag("mapFragment");
        mapFragment.setMapInteraction(popupActive);
        mapFragment.drawRouteFromUser(new GeoPoint(f.geometry.coordinates[1], f.geometry.coordinates[0]), Color.BLUE);
    }

    @Override
    public void onBackPressed(){

        if (manager.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            manager.popBackStack();
            popupActive = false;
            MapFragment mapFragment = (MapFragment) this.manager.findFragmentByTag("mapFragment");
            mapFragment.setMapInteraction(popupActive);

        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(Feature f) {
        //TODO add check if fragment already exists.
        if(!popupActive)
            makePopupFragment(f);
    }
}