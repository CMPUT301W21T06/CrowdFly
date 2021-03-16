package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the Binomial Trial subclass that specifies Binomial Trial getters and setters
 */

public class BinomialTrial extends Trial{
    private int trialID;
    private String successes;
    private String failures;
    private String description;
    private int owner;



    public BinomialTrial(String description, String successes, String failures) {
        super(description);
        this.description = description;
        this.successes = successes;
        this.failures = failures;

        // set id to maxID + 1
        trialID = TrialLog.getTrialLog().getTrials().size() + 1;
    }

    /***
     *
     * @param data
     */
    public BinomialTrial(Map<String, Object> data) {
        super(data);
        this.description = (String) data.get("description");
        this.successes = (String) data.get("successes");
        this.failures = (String) data.get("failures");
        //this.owner = (int) data.get("owner");
        this.trialID = (int) (long) data.get("trialID");
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
     * this returns the string display of the number of failures that occur in a binomial trial
     * @return
     *    return number of failures
     */
    public String getFailures() {
        return failures;
    }


    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = new HashMap<>();
        trl.put("trialID", this.trialID);
        trl.put("description", this.description);
        trl.put("successes", this.successes);
        trl.put("failures", this.failures);
        //trl.put("owner", String.format("users/{}", this.recordedBy.getUserID()));

        return trl;
    }
}
