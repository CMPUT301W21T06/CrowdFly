package com.cmput301w21t06.crowdfly.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the Count Trial subclass that specifies Count Trial getters and setters
 * Location relation issues still need to be implemented for this
 */
public class CountTrial extends Trial{
    private int count;

    public CountTrial(String description, int count, String trialID, String creatorID, String region) {
        super(description,creatorID,trialID,region);
        this.count = count;
    }

    /***
     * this is the hash map constructor
     * @param data
     */
    public CountTrial(Map<String, Object> data) {
        super(data);
        this.count = (Long.valueOf(data.get("count").toString())).intValue();
    }

    /***
     * this returns a CountTrial object in its current state
     * @return CountTrial object
     */
    public CountTrial getData(){
        return new CountTrial(description,count,trialID,creatorID,location);
    }

    /**
     * this returns the string display of the number of counts that occurs in a count trial
     * @return
     *    return number of counts
     */
    public int getCount(){return count;}

    /***
     * this transforms the Count Trial to a HashMap that is fed into the database
     * @return Map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = super.toHashMap();
        trl.put("type","count");
        trl.put("count", this.count);
        return trl;
    }
}
