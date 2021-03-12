package com.cmput301w21t06.crowdfly.Models;

public class BinomialTrial extends Trial{
    private String successes;
    private String failures;


    public BinomialTrial(String description, String successes, String failures) {
        super(description);
        this.successes = successes;
        this.failures = failures;
    }
    public String getSuccesses() {
        return successes;
    }

    public String getFailures() {
        return failures;
    }
}
