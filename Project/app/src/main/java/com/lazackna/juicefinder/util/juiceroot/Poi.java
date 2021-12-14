package com.lazackna.juicefinder.util.juiceroot;

import java.io.Serializable;

public class Poi implements Serializable {
    public DataProvider dataProvider;
    public OperatorInfo operatorInfo;
    public UsageType usageType;
    public StatusType statusType;
    public SubmissionStatus submissionStatus;
    public boolean isRecentlyVerified;
    public String dateLastVerified;
    public long id;
    public String uuid;
    public long dataProviderID;
    public long operatorID;
    public long usageTypeID;
    public AddressInfo addressInfo;
    public Connection[] connections;
    public long numberOfPoints;
    public long statusTypeID;
    public String dateLastStatusUpdate;
    public long dataQualityLevel;
    public String dateCreated;
    public long submissionStatusTypeID;
}
