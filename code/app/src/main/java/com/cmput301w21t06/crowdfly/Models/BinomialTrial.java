package com.cmput301w21t06.crowdfly.Models;

import android.service.autofill.AutofillService;

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;

import java.util.HashMap;
import java.util.Map;


public class BinomialTrial extends Trial{
    private int successes;
    private int failures;


    public BinomialTrial(String description, int successes, int failures, String trialID, String creatorID) {
        super(description,creatorID,trialID);
        this.successes = successes;
        this.failures = failures;
    }


    public BinomialTrial(Map<String, Object> data) {
        super((String) data.get("description"),(String) data.get("trialID"),(String) data.get("experimenter"));
        successes = (int) data.get("successes");
        failures =  (int) data.get("failures");
    }


    public int getSuccesses() {
        return successes;
    }

    public void setSuccesses(int successes) {
        this.successes = successes;
    }


    public void setFailures(int failures) {
        this.failures = failures;
    }


    public int getFailures() {
        return failures;
    }

    public BinomialTrial getData(){
        return new BinomialTrial(description,successes,failures,trialID,creatorID);
    }

    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = super.toHashMap();
        trl.put("type","binomial");
        trl.put("successes", this.successes);
        trl.put("failures", this.failures);
        return trl;
    }
}
