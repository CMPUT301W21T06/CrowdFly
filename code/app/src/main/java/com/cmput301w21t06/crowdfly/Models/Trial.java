package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Controllers.TrialLog;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the Trial superclass that extends its functionalities to its subclasses
 */
public class Trial {
    private int trialID;

    private String description;
    private Boolean locRequired;
    private String location;
    private String result;
    private Statistics statistics;
    private User recordedBy;


    public Trial(String description) {
        this.description = description;

    }

    /***
     *
     * @param data
     */
    public Trial(Map<String, Object> data) {
        this.description = (String) data.get("description");
    }


    //setup getters
    /**
     * this returns the string description of the trial
     * @return
     *    return string description of the trial
     */
    public String getDescription() {
        return description;
    }
    public void specifyLocReq(Boolean locReq){}
    public void specifyLoc(String location){}
    public String getLoc(){return "";}
    private void warnUsers(){}
    public int getTrialID(){ return trialID; }

    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = new HashMap<>();
        trl.put("description", this.description);
        //trl.put("successes", this.successes);
        //trl.put("failures", this.failures);
        trl.put("owner", String.format("users/{}", this.recordedBy.getUserID()));

        return trl;
    }
}

