package com.cmput301w21t06.crowdfly;

import com.cmput301w21t06.crowdfly.Models.BinomialTrial;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class BinomialTrialTest extends TestCase {

    private BinomialTrial mockBinomialTrial(){
        String description = "mock binomial";
        int successes = 3;
        int failures = 4;

        BinomialTrial mockBTrial = new BinomialTrial(description, successes, failures, "", "");
        return mockBTrial;
    }

    @Test
    public void testGetSuccesses() {
        BinomialTrial binomialTrial = mockBinomialTrial();
        assertEquals(3, binomialTrial.getSuccesses());
    }

    @Test
    public void testGetFailures() {
        BinomialTrial binomialTrial = mockBinomialTrial();
        assertEquals(4, binomialTrial.getFailures());
    }

    @Test
    public void testToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("trialID", "testID");
        data.put("experimenter", "ownerID");
        data.put("type", "Binomial");
        data.put("description", "testDescription");
        data.put("successes", (long) 0);
        data.put("failures", (long) 1);

        BinomialTrial btrial = new BinomialTrial(data);
        assertEquals(data.keySet(), btrial.toHashMap().keySet());
    }

}
