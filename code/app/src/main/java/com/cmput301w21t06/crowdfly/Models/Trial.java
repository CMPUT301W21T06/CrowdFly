package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Controllers.TrialLog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the Trial superclass that extends its functionalities to its subclasses
 */
public class Trial {
    private String trialID;
    private String description;
//    private Boolean locRequired;
//    private String location;
//    private String result;
//    private Statistics statistics;
    private String creatorID;

    public Trial(String description, String creatorID, String trialID) {
        this.description = description;
        this.creatorID = creatorID;
        this.trialID = trialID;
    }

    public String getExperimenterID(){return creatorID;}

    public String getTrialID() {
        return trialID;
    }

    public void setTrialID(String trialID) {
        this.trialID = trialID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

//    public void specifyLocReq(Boolean locReq){}
//    public void specifyLoc(String location){}
//    public String getLoc(){return "";}
//    private void warnUsers(){}


    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = new HashMap<>();
        trl.put("description", this.description);
        trl.put("trialID",this.trialID);
        trl.put("experimenter",creatorID);
        return trl;
    }
}

