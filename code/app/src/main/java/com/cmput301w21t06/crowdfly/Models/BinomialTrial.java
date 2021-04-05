package com.cmput301w21t06.crowdfly.Models;

import android.service.autofill.AutofillService;

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the Binomial Trial subclass that specifies Binomial Trial getters and setters
 * Still need to handle things related to location
 */
public class BinomialTrial extends Trial{
    private int successes;
    private int failures;


    /**
     * this is the main constructor for purposes of instantiating a valid binomial trial
     * @param description
     * @param successes
     * @param failures
     * @param creatorID
     * @param trialID
     */
    public BinomialTrial(String description, int successes, int failures, String trialID, String creatorID,String region) {
        super(description,creatorID,trialID,region);
        this.successes = successes;
        this.failures = failures;
        this.type = "binomial";
    }


    /**
     * this is a hashmap constructor for the purposes of getting data from the DB
     * @param data
     */
    public BinomialTrial(Map<String, Object> data) {
        super((String) data.get("description"),(String) data.get("experimenter"), (String) data.get("trialID"),(String) data.get("region"));
        this.type = "binomial";
        successes = (Long.valueOf(data.get("successes").toString())).intValue();
        failures =  (Long.valueOf(data.get("failures").toString())).intValue();
    }


    /**
     * this returns the string display of the number of successes that occur in a binomial trial
     * @return
     *    return number of successes
     */
    public int getSuccesses() {
        return successes;
    }

    /**
     * sets the string display of the number of successes that occur in a binomial trial
     * @param successes
     */
    public void setSuccesses(int successes) {
        this.successes = successes;
    }

    /**
     * sets the string display of the number of failures that occur in a binomial trial
     * @param failures
     */
    public void setFailures(int failures) {
        this.failures = failures;
    }


    /**
     * this returns the string display of the number of failures that occur in a binomial trial
     * @return failures
     *    return number of failures
     */
    public int getFailures() {
        return failures;
    }

    /**
     * this returns the string display of the number of failures that occur in a binomial trial
     * @return failures
     *    return number of failures
     */
    public BinomialTrial getData(){
        return new BinomialTrial(description,successes,failures,trialID,creatorID,location);
    }
    /***
     * this transforms the Binomial Trial to a HashMap that is fed into the database
     * @return Map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = super.toHashMap();
        trl.put("type","binomial");
        trl.put("successes", this.successes);
        trl.put("failures", this.failures);
        return trl;
    }
}
