package com.lazackna.juicefinder.util;

import android.location.Location;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.lazackna.juicefinder.util.API.ApiHandler;
import com.lazackna.juicefinder.util.API.OpenChargeMapRequestBuilder;
import com.lazackna.juicefinder.util.juiceroot.JuiceRoot;


public class MapThread extends Thread{

    private Location location;
    private ApiHandler apiHandler;
    private String TAG;
    private IRootCallback callback;

    public MapThread(Location location, ApiHandler apiHandler, String TAG, IRootCallback callback) {
        this.location = location;
        this.apiHandler = apiHandler;
        this.TAG = TAG;
        this.callback = callback;
    }

    @Override
    public void run() {
        this.apiHandler.makeVolleyObjectRequest(
                response -> {
                    Gson gson = new Gson();
                    JuiceRoot root = gson.fromJson(response.toString(), JuiceRoot.class);
                    Log.d(TAG,"received juice root with length: " + root.features.length);
                    callback.notifyRoot(root);
                },
                error -> {
                    Log.d("e", "e");
                },
                new OpenChargeMapRequestBuilder()
                        .Location(location.getLatitude(), location.getLongitude())
                        .CountryCode("NL")
                        .Distance(10)
                        .IncludeComments()
                        .MaxResults(100)
                        .build(this.apiHandler.getApiKey()).toUrl()
        );
    }
}
