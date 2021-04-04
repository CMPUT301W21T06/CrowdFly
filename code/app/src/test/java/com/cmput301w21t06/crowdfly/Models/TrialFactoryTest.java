package com.cmput301w21t06.crowdfly.Models;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class TrialFactoryTest extends TestCase {

    public static final String BINOMIAL = "binomial";
    public static final String MEASURMENT = "measurement";
    public static final String COUNT = "count";

    public void testGetBinomialTrial() {
        Map data = new HashMap<String, Object>();
        data.put("type", BINOMIAL);
        data.put("successes", (long) 0);
        data.put("failures", (long) 0);
        try {
            BinomialTrial binomial = (BinomialTrial) new TrialFactory().getTrial(BINOMIAL, data);
            assertTrue(binomial instanceof BinomialTrial);
        } catch (Exception e) {
            testFail(e);
        }

    }

    public void testGetCountTrial() {
        Map data = new HashMap<String, Object>();
        data.put("type", COUNT);
        data.put("count", (long) 0);
        try {
            CountTrial countTrial = (CountTrial) new TrialFactory().getTrial(COUNT, data);
            assertTrue(countTrial instanceof CountTrial);
        } catch (Exception e) {
            testFail(e);
        }

    }

    public void testGetMeasurementTrial() {
        Map data = new HashMap<String, Object>();
        data.put("type", MEASURMENT);
        data.put("measurement", (double) 0);
        try {
            MeasurementTrial measurementTrial = (MeasurementTrial) new TrialFactory().getTrial(MEASURMENT, data);
            assertTrue(measurementTrial instanceof MeasurementTrial);
        } catch (Exception e) {
            testFail(e);
        }

    }

    private void testFail(Exception e) {
        e.printStackTrace();
        fail("Invalid response");
    }
}