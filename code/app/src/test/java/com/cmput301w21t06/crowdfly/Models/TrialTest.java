package com.cmput301w21t06.crowdfly.Models;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TrialTest {

    private Trial mockTrial(){
        String description = "mocktrial";
        Trial trial = new Trial(description);
        return trial;
    }

    @Test
    public void testGetTrialID(){
        String trialID = "trial_id";
        Trial mtrial = mockTrial();
        mtrial.setTrialID(trialID);

        assertEquals(0, trialID.compareTo(mtrial.getTrialID()));
    }

    @Test
    public void testGetDescription() {
        Trial trial = mockTrial();
        assertEquals(0, "mocktrial".compareTo(trial.getDescription()));
    }

    @Test
    public void testToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("trialID", "testID");
        data.put("description", "testDescription");
        Trial trial = new Trial(data);
        assertEquals(data, trial.toHashMap());
    }


}
