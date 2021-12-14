package com.lazackna.juicefinder.util.juiceroot;

import java.io.Serializable;

public class AddressInfo implements Serializable {
    public long id;
    public String title;
    public String addressLine1;
    public String town;
    public String postcode;
    public long countryID;
    public Country country;
    public double latitude;
    public double longitude;
    public long distanceUnit;
    public String stateOrProvince;
}
