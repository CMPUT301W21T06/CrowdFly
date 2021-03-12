package com.cmput301w21t06.crowdfly.Models;

public class CountTrial extends Trial{

    private int count;

    public CountTrial(String description, int count) {
        super(description);
        this.count = count;
    }

    public int getCount(){return count;}

    public void setCount(int count){this.count = count;}
}
