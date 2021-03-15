package com.cmput301w21t06.crowdfly.Models;

/**
 * this is the Binomial Trial subclass that specifies Binomial Trial getters and setters
 */

public class BinomialTrial extends Trial{
    private String successes;
    private String failures;


    public BinomialTrial(String description, String successes, String failures) {
        super(description);
        this.successes = successes;
        this.failures = failures;
    }

    /**
     * this returns the string display of the number of successes that occur in a binomial trial
     * @return
     *    return number of successes
     */
    public String getSuccesses() {
        return successes;
    }


    /**
     * this returns the string display of the number of failures that occur in a binomial trial
     * @return
     *    return number of failures
     */
    public String getFailures() {
        return failures;
    }
}
