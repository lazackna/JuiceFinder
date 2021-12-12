package com.lazackna.juicefinder.util.juiceroot;

import java.io.Serializable;

public class Connection implements Serializable {
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
