package com.cmput301w21t06.crowdfly.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the Count Trial subclass that specifies Count Trial getters and setters
 */
public class CountTrial extends Trial{
    private String count;
    private String description;

    public CountTrial(String description, String count) {
        super(description);
        this.description = description;
        this.count = count;
    }

    /***
     * this is the hash map constructor
     * @param data
     */
    public CountTrial(Map<String, Object> data) {
        super(data);
        this.description = (String) data.get("description");
        this.count = (String) data.get("count");

    }

    /**
     * this returns the string display of the number of counts that occurs in a count trial
     * @return
     *    return number of counts
     */
    public String getCount(){return count;}


    /**
     * this returns the string ID of trial
     * @return
     *      the trial id
     */
    public String getTrialID() {
        return trialID;
    }


    /***
     * this transforms the Count Trial to a HashMap that is fed into the database
     * @return Map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = new HashMap<>();
        trl.put("trialID", this.trialID);
        trl.put("description", this.description);
        trl.put("count", this.count);
        //trl.put("owner", String.format("users/{}", this.experimenter.getUserID()));
        return trl;
    }
}
