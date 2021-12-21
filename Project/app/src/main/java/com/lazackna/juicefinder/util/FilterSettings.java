package com.lazackna.juicefinder.util;

import java.io.Serializable;

public class FilterSettings implements Serializable {

    public int distance;

    public FilterSettings() {
        this.distance = 100;
    }


}
