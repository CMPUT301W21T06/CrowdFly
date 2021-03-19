package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Models.BinomialTrial;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BinomialTrialTest {
    private BinomialTrial mockBinomialTrial(){
        String description = "mock binomial";
        String successes = "3";
        String failures = "4";

        BinomialTrial mockBTrial = new BinomialTrial(description, successes, failures);
        return mockBTrial;
    }

    @Test
    public void testGetSuccesses() {
        BinomialTrial binomialTrial = mockBinomialTrial();
        assertEquals(0, "3".compareTo(binomialTrial.getSuccesses()));
    }

    @Test
    public void testGetFailures() {
        BinomialTrial binomialTrial = mockBinomialTrial();
        assertEquals(0, "4".compareTo(binomialTrial.getFailures()));
    }

    @Test
    public void testToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("trialID", "testID");
        data.put("description", "testDescription");
        data.put("successes", "0");
        data.put("failures", "1");

        BinomialTrial btrial = new BinomialTrial(data);
        assertEquals(data, btrial.toHashMap());
    }

}
