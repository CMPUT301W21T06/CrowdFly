package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Controllers.SubscriptionManager;

//import android.telephony.SubscriptionManager;
import android.util.Log;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Controllers.QRManager;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * this is the experiment model class involving model functions
 */

public class Experiment {
    // Eventually this class will import and export JSON objects to firebase, so these attributes
    //methods are subject to change
    private String description;
    private String region;
    private int minTrials;
    private Boolean stillRunning;
    private ArrayList<User> subscribedUsers;
    private ArrayList<Trial> trials;
    private ArrayList<Question> questions;
    private String ownerID;
    private QRManager qrCode;
    private Boolean published;
    private String experimentId;

    /***
     * General constructor for Experiment class
     * @param desc
     * @param reg
     * @param minT
     */
    public Experiment(String desc,String reg, int minT){
        this.description = desc;
        this.region = reg;
        this.minTrials = minT;
        this.stillRunning = true;
        this.published = true;

        subscribedUsers = new ArrayList<>();
        trials = new ArrayList<>();
        questions = new ArrayList<>();

    }

    /***
     * Constructor for Experiment class.
     * Used for getting experiments from FireStore.
     * @param data data containing experiment
     */
    public Experiment(Map<String, Object> data) {
        this.description = (String) data.get("description");
        this.region = (String) data.get("region");
        this.minTrials = (int) (long) data.get("minTrials");
        this.stillRunning = (boolean) data.get("stillRunning");
        this.ownerID = (String) data.get("ownerID");
        this.experimentId = (String) data.get("experimentID");
        this.published = (boolean) data.get("published");
    }


    public void summarizeTrials(ArrayList<Trial> t){}

    /**
     * this allows a user to subscribe to an experiment
     * @param user
     */
    public void subscribe(User user){
        new SubscriptionManager().subscribe(user, this);
    }

    /**
     * this allows a user to unsubscribe to an experiment
     * @param user
     */
    public void unsubscribe(User user){
        new SubscriptionManager().unsubscribe(user, this);
    }

    /**
     * this determines if or if not the user is subscribed to the following experiment
     * @param user
     */
    public void isSubscribed(User user, SubscriptionManager.OnDoneGetSubscribedListener onDoneGetSubscribedListener){
        new SubscriptionManager().isSubscribed(this, user, onDoneGetSubscribedListener);
    }
    public void changeType(int type){}
    public void addTrial(Trial trial){}
    public void ignore(ArrayList<User> subscribedUsers ){}
    public void togglePublish(){}

    // getters
    public String getDescription() {return description;}
    public String getRegion() {return region;}
    public int getMinTrials() {return minTrials;}
    public Boolean getStatus() {return stillRunning;}
    public int getNumTrials() {return trials.size();}
    public ArrayList<User> getSubscribedUsers() {return subscribedUsers;}
    public ArrayList<Trial> getTrials() {return trials;}
    public ArrayList<Question> getQuestions() {return questions;}
    public String getOwnerID() {return ownerID;}
    public QRManager getQRManager() {return qrCode;}
    public Boolean getPublished() {return published;}

    public void setOwnerID(String ownerID) { this.ownerID = ownerID; }
    public void setExperimentId(String experimentId) { this.experimentId = experimentId; }
    public String getExperimentId() { return experimentId; }

    /***
     * this transforms the Experiment to a HashMap that is fed into the database
     * @return Map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> exp = new HashMap<>();
        exp.put("description", this.description);
        exp.put("experimentID", this.experimentId);
        exp.put("minTrials", this.minTrials);
        exp.put("ownerID", this.ownerID);
        exp.put("published", this.published);
        exp.put("region", this.region);
        exp.put("stillRunning", this.stillRunning);

        return exp;
    }
}
