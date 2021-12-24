package com.lazackna.juicefinder.util;

import java.io.Serializable;

public class FilterSettings implements Serializable {

    private int distance;
    private int maxResults;
    private Unit unit;

    public FilterSettings() {
        this.distance = 100;
        this.maxResults = 100;
        this.unit = Unit.MILES;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getMaxResults() {
        return this.maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public String getUnit() {
        return this.unit.toString().toLowerCase();
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public enum Unit{
        MILES,
        KM
    }
}
