// Welcome4.java

package com.lazackna.juicefinder.util;

import java.time.OffsetDateTime;

public class JuiceRoot {
    public String type;
    public Feature[] features;

}

class Feature {
    public String type;
    public String id;
    public Geometry geometry;
    public Properties properties;

}

class Geometry {
    public String type;
    public double[] coordinates;
}

class Properties {
    public String name;
    public String description;
    public String url;
    public String level;
    public String connectionType;
    public Poi poi;
}

// Poi.java


class Poi {
    public DataProvider dataProvider;
    public OperatorInfo operatorInfo;
    public UsageType usageType;
    public StatusType statusType;
    public SubmissionStatus submissionStatus;
    public boolean isRecentlyVerified;
    public OffsetDateTime dateLastVerified;
    public long id;
    public String uuid;
    public long dataProviderID;
    public long operatorID;
    public long usageTypeID;
    public AddressInfo addressInfo;
    public Connection[] connections;
    public long numberOfPoints;
    public long statusTypeID;
    public OffsetDateTime dateLastStatusUpdate;
    public long dataQualityLevel;
    public OffsetDateTime dateCreated;
    public long submissionStatusTypeID;
}

class AddressInfo {
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

class Country {
    public String isoCode;
    public String continentCode;
    public long id;
    public String title;
}

class Connection {
    public long id;
    public long connectionTypeID;
    public ConnectionType connectionType;
    public long statusTypeID;
    public StatusType statusType;
    public long levelID;
    public Level level;
    public double powerKW;
    public long currentTypeID;
    public CurrentType currentType;
    public long quantity;
}

class ConnectionType {
    public String formalName;
    public Boolean isDiscontinued;
    public Boolean isObsolete;
    public long id;
    public String title;
}

class CurrentType {
    public String description;
    public long id;
    public String title;
}

class Level {
    public String comments;
    public boolean isFastChargeCapable;
    public long id;
    public String title;
}

class StatusType {
    public boolean isOperational;
    public boolean isUserSelectable;
    public long id;
    public String title;
}

class DataProvider {
    public String websiteURL;
    public DataProviderStatusType dataProviderStatusType;
    public boolean isRestrictedEdit;
    public boolean isOpenDataLicensed;
    public boolean isApprovedImport;
    public String license;
    public long id;
    public String title;
}

class DataProviderStatusType {
    public boolean isProviderEnabled;
    public long id;
    public String title;
}


class OperatorInfo {
    public String websiteURL;
    public String phonePrimaryContact;
    public Boolean ispublicIndividual;
    public String contactEmail;
    public String faultReportEmail;
    public Boolean isRestrictedEdit;
    public long id;
    public String title;
}

// SubmissionStatus.java
class SubmissionStatus {
    public boolean isLive;
    public long id;
    public String title;
}

class UsageType {
    public boolean isPayAtLocation;
    public boolean isMembershipRequired;
    public boolean isAccessKeyRequired;
    public long id;
    public String title;
}
