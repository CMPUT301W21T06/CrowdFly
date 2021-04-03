package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;

import java.util.HashMap;
import java.util.Map;

/**
 * this is the Measurement Trial subclass that specifies Measurement Trial getters and setters
 * Things related to region have yet to be implemented
 */
public class MeasurementTrial extends Trial{
    private double measurement;

    public MeasurementTrial(String description, double measurement, String trialID, String creatorID, String region) {
        super(description,creatorID,trialID,region);
        this.measurement = measurement;

    }

    /***
     * this is the hashmap constructor for measurement trial
     * @param data
     */
    public MeasurementTrial(Map<String, Object> data) {
        super((String) data.get("description"), (String) data.get("experimenter"), (String) data.get("trialID"),(String) data.get("region"));
        this.measurement = (double) data.get("measurement");

    }

    /**
     * this returns the string display of the measurement number that occurs in a binomial trial
     * @return
     *    return number of successes
     */
    public double getMeasurement(){return measurement;}

    /**
     * sets the string display of the number of the measurement number that occur in a binomial trial
     * @param measurement
     */
    public void setMeasurement(double measurement){
        this.measurement = measurement;
    }

    /***
     * this returns a MeasurementTrial object in its current state
     * @return Map
     */
    public MeasurementTrial getData(){
        return new MeasurementTrial(description, measurement, trialID,creatorID,location);
    }

    /***
     * this transforms the Measurement Trial to a HashMap that is fed into the database
     * @return Map
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = super.toHashMap();
        trl.put("type","measurement");
        trl.put("measurement", this.measurement);
        return trl;
    }
}
