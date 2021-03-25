package com.cmput301w21t06.crowdfly.Models;

import java.util.HashMap;
import java.util.Map;


public class CountTrial extends Trial{
    private int count;

    public CountTrial(String description, int count, String trialID, String creatorID) {
        super(description,creatorID,trialID);
        this.count = count;
    }


    public CountTrial(Map<String, Object> data) {
        super((String) data.get("description"),(String) data.get("trialID"),(String) data.get("experimenter"));
        this.count = (int) data.get("count");

    }


    public int getCount(){return count;}

    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = super.toHashMap();
        trl.put("type","count");
        trl.put("count", this.count);
        return trl;
    }
}
