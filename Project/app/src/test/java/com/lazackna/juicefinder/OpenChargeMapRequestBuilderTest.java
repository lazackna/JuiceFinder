package com.lazackna.juicefinder;

import com.lazackna.juicefinder.util.API.OpenChargeMapRequest;
import com.lazackna.juicefinder.util.API.OpenChargeMapRequestBuilder;
import com.lazackna.juicefinder.util.FilterSettings;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Locale;

public class OpenChargeMapRequestBuilderTest {

    @Test
    public void builder_test() {
        OpenChargeMapRequestBuilder builder = new OpenChargeMapRequestBuilder();

        OpenChargeMapRequest request = builder
                .DistanceUnit(FilterSettings.Unit.KM.toString().toLowerCase())
                .MaxResults(100)
                .Distance(10)
                .Location(1d,1d)
                .CountryCode("NL")
                .IncludeComments()
                .build("key");

        assertEquals("km", builder.getUnit());
        assertEquals(100, builder.getMaxResults());
        assertEquals(10, builder.getDistance());
        assertArrayEquals(new double[]{1d, 1d},
                new double[]{builder.getLocation().getLatitude(),
                        builder.getLocation().getLongitude()},
                0d);
        assertEquals("NL", builder.getCountryCode());
        assertTrue(builder.getIncludeComments());
        assertEquals("key", builder.getKey());
        assertEquals("geojson", builder.getOutput());
        assertNotNull(request);

        builder.DistanceUnit(FilterSettings.Unit.MILES.toString().toLowerCase());
        assertEquals("miles", builder.getUnit());
    }

}
