package com.cmput301w21t06.crowdfly.Models;
//This trial
public class MeasurementTrial extends Trial{

    private String measurement;

    public MeasurementTrial(String description, String measurement) {
        super(description);
        this.measurement = measurement;
    }

    public String getMeasurement(){return measurement;}

    public void setMeasurement(String measurement){this.measurement = measurement;}
}
