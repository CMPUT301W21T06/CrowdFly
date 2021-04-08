package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Models.CountTrial;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class CountTrialTest {

    private CountTrial mockCountTrial(){
        String description = "mock count";
        int count = 3;
        CountTrial mockCTrial = new CountTrial(description, count, "trial_id", "","1,0");
        return mockCTrial;
    }

    @Test
    public void testGetCount() {
        CountTrial countTrial = mockCountTrial();
        assertEquals(3, countTrial.getCount());
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
        data.put("experimenter", "ownerID");
        data.put("region","1,0");
        data.put("description", "testDescription");
        data.put("type", "count");
//        data.put("displayID","0");
        data.put("count", (long) 0);

        CountTrial ctrial = new CountTrial(data);
//        assertEquals(data.keySet(), ctrial.toHashMap().keySet());
    }


}
