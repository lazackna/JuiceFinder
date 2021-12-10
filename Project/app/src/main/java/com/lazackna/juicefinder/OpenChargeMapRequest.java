package com.lazackna.juicefinder;

import android.location.Geocoder;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenChargeMapRequest {

    private String key;
    private ArrayList<GeoPoint> boundingBox;
    private String countryCode;
    private GeoPoint location;
    private String output;
    private Boolean includeComments;
    private int maxResults;
    private int distance;


    public OpenChargeMapRequest(OpenChargeMapRequestBuilder builder) {
        key = builder.getKey();
        countryCode = builder.getCountryCode();
        location = builder.getLocation();
        output = builder.getOutput();
        includeComments = builder.getIncludeComments();
        maxResults = builder.getMaxResults();
        distance = builder.getDistance();
    }


    public String toUrl() {
        return "https://api.openchargemap.io/v3/poi?" +
                "key=" + key +
                "&countrycode=" + countryCode +
                "&latitude=" + location.getLatitude() +
                "&longitude=" + location.getLongitude() +
                "&output=" + output +
                "&includecomments=" + includeComments +
                "&maxresults=" + maxResults +
                "&distance=" + distance;
    }
}
