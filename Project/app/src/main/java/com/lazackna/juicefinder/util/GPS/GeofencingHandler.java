package com.lazackna.juicefinder.util.GPS;

import android.location.Location;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.Set;

public class GeofencingHandler {

    public static final int DISTANCE = 50;
    public Marker Geofence(Set<Marker> markers, Location location) {
        GeoPoint point = new GeoPoint(location);
        for(Marker m : markers) {

            if(point.distanceToAsDouble(m.getPosition()) < DISTANCE) {
                return m;
            }

        }
        return null;
    }
}
