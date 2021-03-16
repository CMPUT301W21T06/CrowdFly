package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the Count Trial subclass that specifies Count Trial getters and setters
 */

public class CountTrial extends Trial{
    private int trialID;
    private String description;
    private String count;

    public CountTrial(String description, String count) {
        super(description);
        this.description = description;
        this.count = count;

        trialID = TrialLog.getTrialLog().getTrials().size() + 1;
    }

    public int getTrialID(){ return trialID; }
    /***
     *
     * @param data
     */
    public CountTrial(Map<String, Object> data) {
        super(data);
        this.description = (String) data.get("description");
        this.count = (String) data.get("count");
        //this.owner = (int) data.get("owner");
        // set id to maxID + 1

        trialID = (int) (long) data.get("trialID");
    }

    /**
     * this returns the string display of the number of counts that occurs in a measurement trial
     * @return
     *    return number of counts
     */
    public String getCount(){return count;}


    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = new HashMap<>();
        trl.put("trialID", this.trialID);
        trl.put("description", this.description);
        trl.put("count", this.count);
        //trl.put("owner", String.format("users/{}", this.recordedBy.getUserID()));

        return trl;
    }
}
