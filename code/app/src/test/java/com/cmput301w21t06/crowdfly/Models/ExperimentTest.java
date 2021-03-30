package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.Question;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.Models.User;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExperimentTest {
    private Experiment exp;

    @Before
    public void setup() {
        exp = new Experiment("sampleExp", "Canada", 15,"","");
    }

    @Test
    public void testGetDescription() {
        assertEquals("sampleExp", exp.getDescription());
    }

    @Test
    public void testGetRegion() {
        assertEquals("Canada", exp.getRegion());
    }

    @Test
    public void testGetMinTrials() {
        assertEquals(15, exp.getMinTrials());
    }

    @Test
    public void testGetStillRunning() {
        assertEquals(true, exp.getStillRunning());
        exp.setStillRunning(false);
        assertEquals(false, exp.getStillRunning());
    }

    @Test
    public void testGetSubscribedUsers() {
        assertEquals(new ArrayList<User>(), exp.getSubscribedUsers());
    }

    @Test
    public void testGetQuestions() {
        assertEquals(new ArrayList<Question>(), exp.getQuestions());
    }

    @Test
    public void testGetOwnerID() {
        exp.setOwnerID("ownerID");
        assertEquals("ownerID", exp.getOwnerID());
    }

    @Test
    public void testGetQRManager() {
        // not implemented yet
    }

    @Test
    public void testGetExperimentId() {
        exp.setExperimentId("expID");
        assertEquals("expID", exp.getExperimentId());
    }

    @Test
    public void testToHashMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("description", "testDescription");
        data.put("region", "Canada");
        data.put("minTrials", (long) 99);
        data.put("stillRunning", true);
        data.put("ownerID", "testID");
        data.put("experimentID", "experiment1");
        data.put("type","test");

        exp = new Experiment(data);
        assertEquals(data.keySet(), exp.toHashMap().keySet());
    }
}