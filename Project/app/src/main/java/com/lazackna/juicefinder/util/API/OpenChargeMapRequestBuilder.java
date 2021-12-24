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
    private String distanceUnit = "miles";

    public OpenChargeMapRequestBuilder() {

    }

    public OpenChargeMapRequest build(String key){
        this.key = key;
        return new OpenChargeMapRequest(this);
    }

    public OpenChargeMapRequestBuilder CountryCode(String countryCode){
        this.countryCode = countryCode;
        return this;
    }

    public OpenChargeMapRequestBuilder DistanceUnit(String distanceUnit) {
        if (distanceUnit.equals("miles"))
            this.distanceUnit = "miles";
        else if (distanceUnit.equals("km"))
            this.distanceUnit = "km";

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

    public String getUnit() {
        return distanceUnit;
    }
}
