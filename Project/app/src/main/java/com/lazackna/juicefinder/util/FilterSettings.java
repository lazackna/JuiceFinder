package com.lazackna.juicefinder.util;

import java.io.Serializable;

public class FilterSettings implements Serializable {

    public int distance;
    public int maxResults;
    public String unit;

    public FilterSettings() {
        this.distance = 100;
        this.maxResults = 100;
        this.unit = "km";
    }


}
