package com.lazackna.juicefinder.util.API;

import org.osmdroid.util.GeoPoint;

public class OpenChargeMapRequestBuilder {

    private String key;
    private String countryCode;
    private GeoPoint location;
    private String output = "geojson";
    private Boolean includeComments;
    private int maxResults;
    private int distance;

    public OpenChargeMapRequestBuilder() {

    }

    public OpenChargeMapRequest build(String key){
        this.key = key;
//        this.output = "geojson";
//        this.countryCode = "NL";
//        this.location = new GeoPoint(0.0d, 0.0d);
//        this.maxResults = 1000;
//        this.distance = 1000;
        return new OpenChargeMapRequest(this);
    }

    public OpenChargeMapRequestBuilder CountryCode(String countryCode){
        this.countryCode = countryCode;
        return this;
    }

    public OpenChargeMapRequestBuilder Location(double lat, double lon){
        this.location = new GeoPoint(lat, lon);
        return this;
    }

    public OpenChargeMapRequestBuilder IncludeComments(){
        this.includeComments = true;
        return this;
    }

    public OpenChargeMapRequestBuilder MaxResults(int maxResults){
        this.maxResults = maxResults;
        return this;
    }

    public OpenChargeMapRequestBuilder Distance(int distance){
        this.distance = distance;
        return this;
    }

    public String getKey() {
        return key;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public String getOutput() {
        return output;
    }

    public Boolean getIncludeComments() {
        return includeComments;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public int getDistance() {
        return distance;
    }
}
