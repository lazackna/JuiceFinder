package com.lazackna.juicefinder.util.GPS;

import android.location.Location;

public interface IGPSSubscriber {

    void notifyLocationChanged(Location location);
}
