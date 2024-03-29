package com.cmput301w21t06.crowdfly.Controllers;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Models.Experiment;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ExperimentLogTest {
    private ExperimentLog expLog;

    @Before
    public void setup() {
        expLog = ExperimentLog.getExperimentLog();
    }

    @Test
    public void testAddExperiment() {
        Experiment expAdd = new Experiment("test", "0,0", 20, "","",false);
        expLog.addExperiment(expAdd);
        assertEquals(true, expAdd.equals(expLog.getExperiment(0)));
    }

    @Test
    public void testRemoveExperiment() {
        Experiment expAdd = new Experiment("test", "0,0", 20, "","",false);
        Experiment expAdd2 = new Experiment("test2", "0,1", 220, "","",false);
        ArrayList<Experiment> expList = new ArrayList<>();
        expList.add(expAdd);

        expLog.addExperiment(expAdd);
        expLog.addExperiment(expAdd2);
        expLog.removeExperiment(1);

        assertEquals(true, expList.equals(expLog.getExperiments()));
    }

    @Test
    public void testResetExperimentLog() {
        Experiment expAdd = new Experiment("test", "0,0", 20, "","",false);
        expLog.addExperiment(expAdd);
        expLog.resetExperimentLog();

        ArrayList<Experiment> newList = new ArrayList<>();
        assertEquals(true, newList.equals(expLog.getExperiments()));
    }
}
