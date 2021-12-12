package com.lazackna.juicefinder.util.juiceroot;

import java.io.Serializable;

public class UsageType implements Serializable {
    public boolean isPayAtLocation;
    public boolean isMembershipRequired;
    public boolean isAccessKeyRequired;
    public long id;
    public String title;
}
