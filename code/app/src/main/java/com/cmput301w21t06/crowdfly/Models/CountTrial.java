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
        super((String) data.get("description"),(String) data.get("experimenter"), (String) data.get("trialID"));
        this.count = ((Long) data.get("count")).intValue();

    }

    public CountTrial getData(){
        return new CountTrial(description,count,trialID,creatorID);
    }
    public int getCount(){return count;}

    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = super.toHashMap();
        trl.put("type","count");
        trl.put("count", this.count);
        return trl;
    }
}
