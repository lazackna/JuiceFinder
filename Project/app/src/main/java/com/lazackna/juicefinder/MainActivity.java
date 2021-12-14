package com.lazackna.juicefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lazackna.juicefinder.databinding.ActivityMainBinding;
import com.lazackna.juicefinder.databinding.ActivityMapBinding;
import com.lazackna.juicefinder.fragments.MapFragment;
import com.lazackna.juicefinder.fragments.PopupFragment;
import com.lazackna.juicefinder.util.API.OpenChargeMapRequest;
import com.lazackna.juicefinder.util.API.OpenChargeMapRequestBuilder;
import com.lazackna.juicefinder.util.juiceroot.Feature;
import com.lazackna.juicefinder.util.juiceroot.JuiceRoot;

import org.json.JSONObject;
import org.osmdroid.config.Configuration;

public class MainActivity extends AppCompatActivity implements OnMarkerClickListener{

    private ActivityMainBinding binding;
    private FragmentManager manager;

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
    }

    @Override
    public void onBackPressed(){

        if (manager.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            manager.popBackStack();

        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(Feature f) {
        //TODO add check if fragment already exists.
        makePopupFragment(f);
    }
}