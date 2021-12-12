package com.lazackna.juicefinder.util.juiceroot;

import java.io.Serializable;

public class DataProvider implements Serializable {
    public String websiteURL;
    public DataProviderStatusType dataProviderStatusType;
    public boolean isRestrictedEdit;
    public boolean isOpenDataLicensed;
    public boolean isApprovedImport;
    public String license;
    public long id;
    public String title;
}
