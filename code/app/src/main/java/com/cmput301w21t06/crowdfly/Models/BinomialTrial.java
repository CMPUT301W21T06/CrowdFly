package com.cmput301w21t06.crowdfly.Models;

import android.service.autofill.AutofillService;

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the Binomial Trial subclass that specifies Binomial Trial getters and setters
 */
public class BinomialTrial extends Trial{
    private String successes;
    private String failures;

    /**
     * this is the main constructor for purposes of instantiating a valid binomial trial
     * @param description
     * @param successes
     * @param failures
     */
    public BinomialTrial(String description, String successes, String failures) {
        super(description); //This needs to match the Trial constructor with the user experimenter
        this.successes = successes;
        this.failures = failures;
    }

    /**
     * this is a hashmap constructor for the purposes of getting data from the DB
     * @param data
     */
    public BinomialTrial(Map<String, Object> data) {
        super(data);
        this.successes = (String) data.get("successes");
        this.failures = (String) data.get("failures");
    }

    /**
     * this returns the string display of the number of successes that occur in a binomial trial
     * @return
     *    return number of successes
     */
    public String getSuccesses() {
        return successes;
    }

    /**
     * sets the string display of the number of successes that occur in a binomial trial
     * @param successes
     */
    public void setSuccesses(String successes) {
        this.successes = successes;
    }

    /**
     * sets the string display of the number of failures that occur in a binomial trial
     * @param failures
     */
    public void setFailures(String failures) {
        this.failures = failures;
    }

    /**
     * this returns the string display of the number of failures that occur in a binomial trial
     * @return failures
     *    return number of failures
     */
    public String getFailures() {
        return failures;
    }


    /***
     * this transforms the Binomial Trial to a HashMap that is fed into the database
     * @return Map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = new HashMap<>();
        trl.put("trialID", this.trialID);
        trl.put("description", this.description);
        trl.put("successes", this.successes);
        trl.put("failures", this.failures);
        //trl.put("owner", String.format("users/{}", this.experimenter.getUserID()));
        
        return trl;
    }
}
