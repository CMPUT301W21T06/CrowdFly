package com.cmput301w21t06.crowdfly.Models;

import android.util.Log;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Controllers.QRManager;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Database.SubscriptionController;
import com.cmput301w21t06.crowdfly.Database.TrialController;

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
    private ArrayList<Question> questions;
    private String ownerID;
    private QRManager qrCode;
    private String experimentId;
    private TrialController trialController;
    private SubscriptionController subController;
    private String type;
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
    public Experiment(String desc,String reg, int minT, String userID, String type){

        this.description = desc;
        this.region = reg;
        this.minTrials = minT;
        this.stillRunning = true;
        this.ownerID = userID;
        this.type = type;
        subscribedUsers = new ArrayList<>();
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
        this.minTrials = ((Long) data.get("minTrials")).intValue();
        this.stillRunning = (boolean) data.get("stillRunning");
        this.ownerID = (String) data.get("ownerID");
        this.experimentId = (String) data.get("experimentID");
        this.type = (String) data.get("type");
//        setUpFullExperiment((String) data.get("experimentID"));
    }

    // METHODS

//    public void summarizeTrials(ArrayList<Trial> t){}

    /**
     * This allows a user to subscribe to this experiment
     * @param user
     * The user who wishes to subscribe
     */
    public void subscribe(User user){
        subController.setSubscribedUser(this,user);
    }

    /**
     * This allows a user to unsubscribe to an experiment
     * @param user
     * The user who wishes to unsubscribe
     */
    public void unsubscribe(User user){
        subController.removeSubscribedUser(this,user);
    }

    /**
     * This determines if or if not the user is subscribed to the following experiment
     * @param user
     * The user who is unsubscribing
     */
    public void isSubscribed(User user, CrowdFlyListeners.OnDoneGetSubscribedListener onDoneGetSubscribedListener){
        subController.isSubscribed(user, onDoneGetSubscribedListener);
    }

    public boolean canEnd(){
        return (trialController.getNumTrials() >= minTrials);
    }

    // GETTERS

    /**
     * This returns the experiment description
     * @return
     * This is the experiment description
     */
    public String getDescription() {return description;}

    /**
     * This returns the type of experiment
     * @return
     * This is a string representing the type of experiment
     */
    public String getType(){return type;}
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
     * This returns the number of trials in the experiment
     * @return
     * This is the number of trials in the experiment
     */

    public int getNumTrials() {return trialController.getNumTrials();}
    /**
     * This returns who is subscribed to the experiment
     * @return
     * This is who is subscribed to the experiment
     */

    public ArrayList<User> getSubscribedUsers() {return subscribedUsers;}



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

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;

    }
    /**
     * This is used to instantiate new Trial and Subscription Controllers
     * @param experimentId
     * This is the experiment ID to be associated with the experiment
     */
    public void setUpFullExperiment(String experimentId){
        setExperimentId(experimentId);
        trialController = new TrialController(experimentId);
        subController = new SubscriptionController(experimentId);
    }

    /**
     * This gets trialController
     * @return trialController
     */
    public TrialController getTrialController() {
        return trialController;
    }

    /**
     * This gets subController
     * @return subController
     */
    public SubscriptionController getSubController() {
        return subController;
    }


    /***
     * This transforms the experiment to a hash map that is fed into the database
     * @return exp
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
        exp.put("type",this.type);
        return exp;
    }

}
