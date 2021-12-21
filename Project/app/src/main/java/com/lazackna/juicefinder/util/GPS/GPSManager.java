package com.lazackna.juicefinder.util.GPS;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class GPSManager {

    private static GPSManager instance;

    private FusedLocationProviderClient client;
    private final LocationCallback callback;
    private Location latestLocation;

    private static final int UPDATE_INTERVAL = 10000;
    private static final int FASTEST_INTERVAL = 2000;
    private final List<IGPSSubscriber> subscribers = new ArrayList<>();
    private boolean isRunning;

    private GPSManager(Context context) {
        this.callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                latestLocation = locationResult.getLastLocation();
                notifySubscribers(latestLocation);
            }
        };
        this.client = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                latestLocation = location;
                notifySubscribers(location);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("g", "failure");
            }
        });
        latestLocation = null;
        isRunning = false;
    }

    public static GPSManager getInstance(Context context) {
        if(instance == null) instance = new GPSManager(context);
        return instance;
    }

    public boolean subscribe(IGPSSubscriber subscriber) {

        if (!this.subscribers.contains(subscriber)) {
            this.subscribers.add(subscriber);
            return true;
        }

        return false;
    }

    public boolean unsubscribe(IGPSSubscriber subscriber) {
        this.subscribers.remove(subscriber);
        return true;
    }

    public void start(Context context) {
        if (this.isRunning) return;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationRequest request = getRequest();

        if (client == null) {
            client = LocationServices.getFusedLocationProviderClient(context);
        }

        client.requestLocationUpdates(request, callback, Looper.getMainLooper());

        this.isRunning = true;
    }

    public void stop() {
        client.removeLocationUpdates(callback);
        this.isRunning = false;
    }

    /**
     * Used to build an LocationRequest.
     *
     * @return a LocationRequest
     */
    private LocationRequest getRequest() {
        LocationRequest request = LocationRequest.create();
        request.setInterval(UPDATE_INTERVAL);
        request.setFastestInterval(FASTEST_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return request;
    }

    /**
     * Used to notify all IGPSSubscribers.
     *
     * @param location a Location
     */
    private void notifySubscribers(Location location) {
        this.subscribers.forEach(sub -> sub.notifyLocationChanged(location));
    }

    public FusedLocationProviderClient getClient(Context context) {
        return LocationServices.getFusedLocationProviderClient(context);
    }

    public LocationCallback getCallback() {
        return this.callback;
    }

    public Location getLastLocation() {
        return this.latestLocation;
    }
}
