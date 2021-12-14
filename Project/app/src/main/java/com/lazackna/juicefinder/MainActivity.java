package com.lazackna.juicefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.lazackna.juicefinder.util.API.OpenChargeMapRequest;
import com.lazackna.juicefinder.util.API.OpenChargeMapRequestBuilder;
import com.lazackna.juicefinder.util.juiceroot.JuiceRoot;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        initialize();
    }

    private void initialize() {
        this.manager = getSupportFragmentManager();
        makeMapFragment();

    }

    private void makeMapFragment() {
        this.manager.beginTransaction()
                .setReorderingAllowed(false)
                .add(binding.fragmentHolder.getId(), MapFragment.class, null, "mapFragment")
                .commit();

    }



    private void openMap(JuiceRoot root) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("juice", root);
        startActivity(intent);
    }

}