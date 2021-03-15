package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;

import java.util.HashMap;
import java.util.Map;

public class Trial {
    private int trialID;

    private String description;
    private String successes;
    private String failures;

    private Boolean locRequired;
    private String location;
    private String result;
    private Statistics statistics;
    private User recordedBy;


    //constructors
    public Trial(String description, String successes, String failures) {
        this.description = description;
        this.successes = successes;
        this.failures = failures;
    }

    /***
     *
     * @param data
     */
    public Trial(Map<String, Object> data) {
        this.description = (String) data.get("description");
    }


    //setup getters
    public String getDescription() {
        return description;
    }

    public String getSuccesses() {
        return successes;
    }

    public String getFailures() {
        return failures;
    }

    public int getTrialID() { return trialID; }

    public void specifyLocReq(Boolean locReq){}
    public void specifyLoc(String location){}
    public String getLoc(){return "";}
    private void warnUsers(){}

    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = new HashMap<>();
        trl.put("description", this.description);
        trl.put("successes", this.successes);
        trl.put("failures", this.failures);
        trl.put("owner", String.format("users/{}", this.recordedBy.getUserID()));

        return trl;
    }
}
