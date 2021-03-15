package com.cmput301w21t06.crowdfly.Models;

public class CountTrial extends Trial{

    private String count;

    public CountTrial(String description, String count) {
        super(description);
        this.count = count;
    }

    public String getCount(){return count;}

    public void setCount(String count){this.count = count;}
}
