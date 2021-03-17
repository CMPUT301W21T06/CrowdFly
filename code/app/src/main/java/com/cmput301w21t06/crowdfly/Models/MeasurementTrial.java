package com.cmput301w21t06.crowdfly.Models;
//This trial

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the Measurement Trial subclass that specifies Measurement Trial getters and setters
 */
public class MeasurementTrial extends Trial{
    private String measurement;

    public MeasurementTrial(String description, String measurement) {
        super(description);
        this.description = description;
        this.measurement = measurement;

    }

    /***
     *
     * @param data
     */
    public MeasurementTrial(Map<String, Object> data) {
        super(data);
        this.measurement = (String) data.get("measurement");

    }

    /**
     * this returns the string display of the measurement number that occurs in a binomial trial
     * @return
     *    return number of successes
     */
    public String getMeasurement(){return measurement;}

    /**
     * sets the string display of the number of the measurement number that occur in a binomial trial
     * @param measurement
     */
    public void setMeasurement(String measurement){
        this.measurement = measurement;
    }


    /***
     * this transforms the Measurement Trial to a HashMap that is fed into the database
     * @return Map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = new HashMap<>();
        trl.put("trialID", this.trialID);
        trl.put("description", this.description);
        trl.put("measurement", this.measurement);
        //trl.put("owner", String.format("users/{}", this.recordedBy.getUserID()));

        return trl;
    }
}
