package com.cmput301w21t06.crowdfly.Models;
//This trial

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the Measurement Trial subclass that specifies Measurement Trial getters and setters
 */
public class MeasurementTrial extends Trial{
    private int trialID;
    private String measurement;
    private String description;

    public MeasurementTrial(String description, String measurement) {
        super(description);
        this.description = description;
        this.measurement = measurement;

        trialID = TrialLog.getTrialLog().getTrials().size() + 1;
    }

    /***
     *
     * @param data
     */
    public MeasurementTrial(Map<String, Object> data) {
        super(data);
        this.description = (String) data.get("description");
        this.measurement = (String) data.get("measurement");
        //this.owner = (int) data.get("owner");
        // set id to maxID + 1
        this.trialID = (int) (long) data.get("trialID");

    }

    /**
     * this returns the string display of the measurement number that occurs in a binomial trial
     * @return
     *    return number of successes
     */
    public String getMeasurement(){return measurement;}

    public void setMeasurement(String measurement){this.measurement = measurement;}

    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = new HashMap<>();
        trl.put("trialID", this.trialID);
        trl.put("description", this.description);
        trl.put("measurement", this.measurement);
        //trl.put("owner", String.format("users/{}", this.recordedBy.getUserID()));

        return trl;
    }
}
