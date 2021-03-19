package com.cmput301w21t06.crowdfly;

import com.cmput301w21t06.crowdfly.Models.CountTrial;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CountTrialTest {

    private CountTrial mockCountTrial(){
        String description = "mock count";
        String count = "3";
        CountTrial mockCTrial = new CountTrial(description, count);
        return mockCTrial;
    }

    @Test
    public void testGetCount() {
        CountTrial countTrial = mockCountTrial();
        assertEquals(0, "3".compareTo(countTrial.getCount()));
    }

    @Test
    public void testGetTrialID(){
        String trialID = "trial_id";
        CountTrial ctrial = mockCountTrial();
        ctrial.setTrialID(trialID);

        assertEquals(0, trialID.compareTo(ctrial.getTrialID()));

    }

    @Test
    public void testToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("trialID", "testID");
        data.put("description", "testDescription");
        data.put("count", "0");

        CountTrial ctrial = new CountTrial(data);
        assertEquals(data, ctrial.toHashMap());
    }


}
