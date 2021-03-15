package com.cmput301w21t06.crowdfly.Models;
//This trial

/**
 * this is the Measurement Trial subclass that specifies Measurement Trial getters and setters
 */
public class MeasurementTrial extends Trial{

    private String measurement;

    public MeasurementTrial(String description, String measurement) {
        super(description);
        this.measurement = measurement;
    }

    /**
     * this returns the string display of the measurement number that occurs in a binomial trial
     * @return
     *    return number of successes
     */
    public String getMeasurement(){return measurement;}

    public void setMeasurement(String measurement){this.measurement = measurement;}
}
