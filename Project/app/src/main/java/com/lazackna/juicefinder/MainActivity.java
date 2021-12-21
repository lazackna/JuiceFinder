package com.lazackna.juicefinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lazackna.juicefinder.databinding.ActivityMainBinding;
import com.lazackna.juicefinder.fragments.FilterFragment;
import com.lazackna.juicefinder.fragments.MapFragment;
import com.lazackna.juicefinder.fragments.PopupFragment;
import com.lazackna.juicefinder.util.FilterSettings;
import com.lazackna.juicefinder.util.FilterViewModel;
import com.lazackna.juicefinder.util.IFilterSubscriber;
import com.lazackna.juicefinder.util.juiceroot.Feature;

import org.osmdroid.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMarkerClickListener, IFilterSubscriber {

    private ActivityMainBinding binding;
    private FragmentManager manager;
    private boolean popupActive = false;

    public static FilterViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);
        binding.myToolbar.showOverflowMenu();

        requestPermissions();
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                makeFilterFragment();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void requestPermissions() {
        List<String> permissionsToRequest = new ArrayList<>();

        if (!hasInternetPermission()) {
            permissionsToRequest.add(Manifest.permission.INTERNET);
        }

        if (!hasLocationCoarsePermission()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!hasLocationFinePermission()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[]{}), 0);
        }
    }

    private boolean hasInternetPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasLocationCoarsePermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasLocationFinePermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != 0 || grantResults.length == 0) return;

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) continue;

            Log.d("PermissionsRequest", permissions[i] + " granted.");
        }
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
        viewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        viewModel.getSettings().observe(this, s -> {
            if(popupActive)
            onBackPressed();
        });
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

    private void makeFilterFragment() {
        if (popupActive) return;
        Bundle bundle = new Bundle();

        this.manager.beginTransaction()
                .setReorderingAllowed(false)
                .add(binding.fragmentHolder.getId(), FilterFragment.class, bundle, "filterFragment")
                .addToBackStack("filter")
                .commit();
        popupActive = true;
        setMapInteraction(popupActive);
    }

    private void makePopupFragment(Feature f) {
        if (popupActive) return;
        Bundle bundle = new Bundle();
        bundle.putSerializable("feature", f);

        this.manager.beginTransaction()
                .setReorderingAllowed(false)
                .add(binding.fragmentHolder.getId(), PopupFragment.class, bundle, "popupFragment")
                .addToBackStack("popup")
                .commit();
        popupActive = true;
        setMapInteraction(popupActive);

    }

    @Override
    public void onBackPressed(){

        if (manager.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            manager.popBackStack();
            popupActive = false;
            setMapInteraction(popupActive);

        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    public boolean setMapInteraction(boolean isInteractive){
        MapFragment mapFragment = (MapFragment) this.manager.findFragmentByTag("mapFragment");
        if(mapFragment != null){
            mapFragment.setMapInteraction(isInteractive);
            return true;
        } else return false;
    }

    @Override
    public void onClick(Feature f) {
        //TODO add check if fragment already exists.
        if(!popupActive)
            makePopupFragment(f);
    }

    @Override
    public void notifySubscriber(FilterSettings settings) {

    }
}