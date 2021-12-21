package com.lazackna.juicefinder.util.API;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class DownloadRoadTask extends AsyncTask<Void, Void, Road> {
    private static final String LOGTAG = DownloadRoadTask.class.getName();

    private final Context context;
    private List<GeoPoint> geoPoints;

    public DownloadRoadTask(Context context, ArrayList<GeoPoint> geoPoints) {
        this.context = context;
        this.geoPoints = geoPoints;
    }

    @Override
    protected Road doInBackground(Void... voids) {
        Log.i(LOGTAG, "DownloadTask trying to download route...");
        OSRMRoadManager roadManager = new OSRMRoadManager(this.context, Configuration.getInstance().getUserAgentValue());
        roadManager.setMean(OSRMRoadManager.MEAN_BY_CAR);
        Log.i(LOGTAG, "Downloading route from geoPoints: " + this.geoPoints.size());
        return roadManager.getRoad((ArrayList<GeoPoint>) this.geoPoints);

    }
}