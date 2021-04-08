package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Models.Trial;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TrialTest {

    private Trial mockTrial(){
        String description = "mocktrial";
        Trial trial = new Trial(description,"","","1,0");
        return trial;
    }
    private String userID = "userTestID";

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
    public void testSetExperimenterID(){
        Trial trial = mockTrial();
        trial.setExperimenterID(userID);

        assertEquals(0, "userTestID".compareTo(trial.getExperimenterID()));
    }

//    @Test
//    public void testToHashMap(){
//        Map<String, Object> data = new HashMap<>();
//        data.put("trialID", "testID");
//        data.put("experimenter", "expID");
//        data.put("description", "testDescription");
//        data.put("region","testregion");
////        data.put("displayID","0");
//        Trial trial = new Trial(data);
//        trial.setExperimenterID("expID");
////        assertEquals(data, trial.toHashMap());
//    }


}
