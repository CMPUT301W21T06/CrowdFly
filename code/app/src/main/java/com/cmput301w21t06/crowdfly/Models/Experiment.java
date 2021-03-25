package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Controllers.SubscriptionManager;
import android.util.Log;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Controllers.QRManager;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a model of the experiment object
 * Issues related to region have yet to be implemented
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
    private String experimentId;

    // CONSTRUCTORS

    /**
     * This constructor is for experiments created during the app runtime by a user
     * @param desc
     * The description that was entered by the user
     * @param reg
     * The region that was entered by the user
     * @param minT
     * The minimum number of trials entered by the user
     */
    public Experiment(String desc,String reg, int minT){
        this.description = desc;
        this.region = reg;
        this.minTrials = minT;
        this.stillRunning = true;

        subscribedUsers = new ArrayList<>();
        trials = new ArrayList<>();
        questions = new ArrayList<>();

    }

    /**
     * Constructor for Experiment class used when getting experiments from FireStore.
     * @param data
     * This is data representing the experiment
     */
    public Experiment(Map<String, Object> data) {
        this.description = (String) data.get("description");
        this.region = (String) data.get("region");
        this.minTrials = (int) (long) data.get("minTrials");
        this.stillRunning = (boolean) data.get("stillRunning");
        this.ownerID = (String) data.get("ownerID");
        this.experimentId = (String) data.get("experimentID");
    }

    // METHODS

//    public void summarizeTrials(ArrayList<Trial> t){}

    /**
     * This allows a user to subscribe to this experiment
     * @param user
     * The user who wishes to subscribe
     */
    public void subscribe(User user){
        new SubscriptionManager().subscribe(user, this);
    }

    /**
     * This allows a user to unsubscribe to an experiment
     * @param user
     * The user who wishes to unsubscribe
     */
    public void unsubscribe(User user){
        new SubscriptionManager().unsubscribe(user, this);
    }

    /**
     * This determines if or if not the user is subscribed to the following experiment
     * @param user
     * The user who is unsubscribing
     */
    public void isSubscribed(User user, SubscriptionManager.OnDoneGetSubscribedListener onDoneGetSubscribedListener){
        new SubscriptionManager().isSubscribed(this, user, onDoneGetSubscribedListener);
    }


    // GETTERS

    /**
     * This returns the experiment description
     * @return
     * This is the experiment description
     */
    public String getDescription() {return description;}

    /**
     * This returns the experiment region
     * @return
     * This is the experiment region
     */

    public String getRegion() {return region;}

    /**
     * This returns the experiment's minimum number of trials
     * @return
     * This is the experiment's minimum number of trials
     */

    public int getMinTrials() {return minTrials;}

    /**
     * This returns who is subscribed to the experiment
     * @return
     * This is who is subscribed to the experiment
     */

    public ArrayList<User> getSubscribedUsers() {return subscribedUsers;}

    /**
     * This returns the experiment's trials
     * @return
     * This is the experiment's trials
     */

    public ArrayList<Trial> getTrials() {return trials;}

    /**
     * This returns questions about an experiment
     * @return
     * This is the questions about this experiment
     */

    public ArrayList<Question> getQuestions() {return questions;}

    /**
     * This returns the owner of the experiment
     * @return
     * This is the owner of the experiment
    */

    public String getOwnerID() {return ownerID;}

    /**
     * This check if the experiment is still active
     * @return
     * THis is a boolean indicating whether or not the experiment is still active
     */

    public Boolean getStillRunning() { return stillRunning;}

    /**
     * This returns the experiment ID
     * @return
     * This is the experiment ID
     */

    public String getExperimentId() { return experimentId; }

    // SETTERS

    /**
     * This can be used to change the experiment between the active and inactive state
     * @param stillRunning
     * This is a boolean indicating if it is still active
     */

    public void setStillRunning(Boolean stillRunning) { this.stillRunning = stillRunning; }

    /**
     * This is used to set the owner ID of a new experiment
     * @param ownerID
     * This is the ID of the owner of the newly created experiment
     */

    public void setOwnerID(String ownerID) { this.ownerID = ownerID; }

    /**
     * This is used to set the experiment ID of an experiment
     * @param experimentId
     * This is the experiment ID to be associated with this experiment
     */

    public void setExperimentId(String experimentId) { this.experimentId = experimentId; }

    /***
     * This transforms the experiment to a hash map that is fed into the database
     * @return
     * The hash map to be fed into the database
     */

    public Map<String, Object> toHashMap() {
        Map<String, Object> exp = new HashMap<>();
        exp.put("description", this.description);
        exp.put("region", this.region);
        exp.put("minTrials", (long) this.minTrials);
        exp.put("stillRunning", this.stillRunning);
        exp.put("ownerID", this.ownerID);
        exp.put("experimentID", this.experimentId);

        return exp;
    }
}
