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
    public User experimenter;
    public String trialID;
    public String description;
    private Boolean locRequired;
    private String location;
    private String result;
    private Statistics statistics;
    //private final String userID = FirebaseAuth.getInstance().getUid();
    private String userID;

    public Trial(String description) {

        this.description = description;
//        this.experimenter = experimenter;
    }

    /**
     * this sets the user ID to a specific trial class
     * @param userID
     */
    public void setExperimenterID(String userID){ this.userID = userID;}


    /**
     * this returns user id that is connected with a specific trial
     * @return userID
     */
    public String getExperimenterID(){return userID;}

    /***
     * this returns the experimenter that created the trial
     * @return experimenter
     */
    public User getExperimenter() {
        return experimenter;
    }


    /***
     * this returns the trial ID number
     * @return String
     */
    public String getTrialID() {
        return trialID;
    }

    public void setTrialID(String trialID) {
        this.trialID = trialID;
    }

    /***
     * this sets the description of the trial
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /***
     * this is the hash map constructor for trial
     * @param data
     */
    public Trial(Map<String, Object> data) {
        this.description = (String) data.get("description");
        this.trialID = (String) data.get("trialID");
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

    /***
     * this transforms the Trial to a HashMap that is fed into the database
     * @return Map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = new HashMap<>();
        trl.put("description", this.description);
        trl.put("trialID",this.trialID);
        trl.put("experimenter",userID);
        //trl.put("owner", String.format("users/{}", this.experimenter.getUserID()));



        return trl;
    }
}

