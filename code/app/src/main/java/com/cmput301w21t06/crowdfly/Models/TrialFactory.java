package com.cmput301w21t06.crowdfly.Models;

import androidx.annotation.Nullable;

import java.util.Map;

/**
 * This class aims to contain the logic for checking trial type and instanciating the correct
 * Trial Class
 */

public class TrialFactory {

    public static final String COUNT = "count";
    public static final String BINOMIAL = "binomial";
    public static final String MEASUREMENT = "measurement";

    public TrialFactory() {
    }

    /**
     * Factory method for getting Trial
     * @param trialType String representation of the trial Type
     * @param metadata HashMap with metadata from the database
     * @return Trial
     * @throws Exception
     */
    public Trial getTrial(String trialType, Map<String, Object> metadata) throws Exception {
        if(trialType.equals(BINOMIAL)){
            return new BinomialTrial(metadata);
        }
        if(trialType.equals(COUNT)){
            return new CountTrial(metadata);
        }
        if(trialType.equals(MEASUREMENT)){
            return new MeasurementTrial(metadata);
        }
        throw new Exception("Not a valid trial type");
    }

    public Trial getTrialInferType(Map<String, Object> metadata) throws Exception {
        if(metadata.containsKey(COUNT)){
            return this.getTrial(COUNT, metadata);
        }
        if(metadata.containsKey(MEASUREMENT)){
            return this.getTrial(MEASUREMENT, metadata);
        }
        if(metadata.containsKey("successes")){
            return this.getTrial(BINOMIAL, metadata);
        }
        return new Trial(metadata);
    }

}
