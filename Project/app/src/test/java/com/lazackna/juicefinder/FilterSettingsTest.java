package com.lazackna.juicefinder;


import com.lazackna.juicefinder.util.FilterSettings;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

public class FilterSettingsTest {

    @Test
    public void testSetters() {
        FilterSettings s = new FilterSettings();
        s.setDistance(100);
        s.setMaxResults(10);
        s.setUnit(FilterSettings.Unit.KM);

        assertEquals(100, s.getDistance());
        assertEquals(10, s.getMaxResults());
        assertEquals(FilterSettings.Unit.KM.toString().toLowerCase(), s.getUnit());
    }
}
