package com.cmput301w21t06.crowdfly.Models;

import android.util.Log;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Controllers.QRManager;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private User owner;
    private QRManager qrCode;
    private Boolean published;
    private int experimentId;
    
    // no users in place at the moment, just gonna comment it out for now
    // public Experiment(String desc, String reg, int minT, ArrayList<User> subscribedU, String owner){}

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

        // set id to maxID + 1
        experimentId = ExperimentLog.getExperimentLog().getExperiments().size() + 1;
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
        // this.owner = owner;
        this.experimentId = (int) (long) data.get("experimentID");
        this.published = (boolean) data.get("published");
    }

    // methods
    public void summarizeTrials(ArrayList<Trial> t){}
    public void subscribe(User user){}
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
    public User getOwner() {return owner;}
    public QRManager getQRManager() {return qrCode;}
    public Boolean getPublished() {return published;}

    public int getExperimentId() {
        return experimentId;
    }

    public Map<String, Object> toHashMap() {
        Map<String, Object> exp = new HashMap<>();
        exp.put("description", this.description);
        exp.put("experimentID", this.experimentId);
        exp.put("minTrials", this.minTrials);
        // exp.put("owner", String.format("/users/{}", this.owner.getUserID()));
        exp.put("published", this.published);
        exp.put("region", this.region);
        exp.put("stillRunning", this.stillRunning);

        return exp;
    }
}
