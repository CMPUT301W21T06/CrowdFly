package com.cmput301w21t06.crowdfly;

import android.util.Log;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Controllers.TrialLog;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.Trial;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TrialLogTest {
    private TrialLog trialLog;

    @Before
    public void setup() {
        trialLog = TrialLog.getTrialLog();
    }

    @Test
    public void testAddTrial() {
        Trial trialAdd = new Trial("test_description");
        trialLog.addTrial(trialAdd);
        assertEquals(true, trialAdd.equals(trialLog.getTrial(1)));
    }

    @Test
    public void testRemoveTrial() {
        Trial trialAdd = new Trial("test");
        Trial trialAdd2 = new Trial("test2");
        ArrayList<Trial> trialList = new ArrayList<>();
        trialList.add(trialAdd);

        trialLog.addTrial(trialAdd);
        trialLog.addTrial(trialAdd2);
        trialLog.removeTrial(1);

        assertEquals(true, trialList.equals(trialLog.getTrials()));
    }

    @Test
    public void testResetTrialLog() {
        Trial trialAdd = new Trial("test");
        trialLog.addTrial(trialAdd);
        trialLog.resetTrialLog();

        ArrayList<Trial> newList = new ArrayList<>();
        assertEquals(true, newList.equals(trialLog.getTrials()));
    }
}
