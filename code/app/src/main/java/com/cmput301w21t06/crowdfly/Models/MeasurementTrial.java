package com.cmput301w21t06.crowdfly.Models;

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;

import java.util.HashMap;
import java.util.Map;


public class MeasurementTrial extends Trial{
    private double measurement;

    public MeasurementTrial(String description, double measurement, String trialID, String creatorID) {
        super(description,creatorID,trialID);
        this.measurement = measurement;

    }

    public MeasurementTrial(Map<String, Object> data) {
        super((String) data.get("description"),(String) data.get("trialID"),(String) data.get("experimenter"));
        this.measurement = (double) data.get("measurement");

    }

    public double getMeasurement(){return measurement;}

    public void setMeasurement(double measurement){
        this.measurement = measurement;
    }

    public MeasurementTrial getData(){
        return new MeasurementTrial(description, measurement, trialID,creatorID);
    }

    public Map<String, Object> toHashMap() {
        Map<String, Object> trl = super.toHashMap();
        trl.put("type","measurement");
        trl.put("measurement", this.measurement);
        return trl;
    }
}
