package com.cmput301w21t06.crowdfly;

import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;

import org.junit.Test;

import java.time.chrono.MinguoEra;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MeasurementTrialTest {

    private MeasurementTrial mockMeasurementTrial(){
        String description = "mock count";
        int measurement = 3;
        MeasurementTrial mockMTrial = new MeasurementTrial(description, measurement, "", "");
        return mockMTrial;
    }

    @Test
    public void testGetMeasurement() {
        MeasurementTrial measurementTrial = mockMeasurementTrial();
        assertEquals(3, measurementTrial.getMeasurement(),  0);
    }

    @Test
    public void testGetTrialID(){
        String trialID = "trial_id";
        MeasurementTrial mtrial = mockMeasurementTrial();
        mtrial.setTrialID(trialID);

        assertEquals(0, trialID.compareTo(mtrial.getTrialID()), 0);
    }

    @Test
    public void testToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("trialID", "testID");
        data.put("description", "testDescription");
        data.put("experimenter", "ownerID");
        data.put("type", "Measurement");
        data.put("measurement", (double) 0);

        MeasurementTrial mtrial = new MeasurementTrial(data);
        assertEquals(data.keySet(), mtrial.toHashMap().keySet());
    }
}
