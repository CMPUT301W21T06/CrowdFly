package com.cmput301w21t06.crowdfly.Models;
//This trial
public class MeasurementTrial extends Trial{

    private int measurement;

    public MeasurementTrial(String description, int measurement) {
        super(description);
        this.measurement = measurement;
    }

    public int getMeasurement(){return measurement;}

    public void setMeasurement(int measurement){this.measurement = measurement;}
}
