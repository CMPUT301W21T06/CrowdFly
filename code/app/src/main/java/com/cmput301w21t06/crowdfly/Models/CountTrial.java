package com.cmput301w21t06.crowdfly.Models;

/**
 * this is the Count Trial subclass that specifies Count Trial getters and setters
 */

public class CountTrial extends Trial{

    private String count;

    public CountTrial(String description, String count) {
        super(description);
        this.count = count;
    }

    /**
     * this returns the string display of the number of counts that occurs in a measurement trial
     * @return
     *    return number of counts
     */
    public String getCount(){return count;}

}
