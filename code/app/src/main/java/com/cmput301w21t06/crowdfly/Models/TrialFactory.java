package com.cmput301w21t06.crowdfly.Models;

import androidx.annotation.Nullable;

import java.util.Map;

/**
 * This class aims to contain the logic for checking trial type and instanciating the correct
 * Trial Class
 */

public class TrialFactory {

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
        if(trialType.equals("binomial")){
            return new BinomialTrial(metadata);
        }
        if(trialType.equals("count")){
            return new CountTrial(metadata);
        }
        if(trialType.equals("measurement")){
            return new MeasurementTrial(metadata);
        }
        throw new Exception("Not a valid trial type");
    }

    /**
     * Factory method for getting Trial
     * @param metadata HashMap with metadata from the database
     * @return Trial
     * @throws Exception
     */
    public Trial getTrialInferType(Map<String, Object> metadata) {
        if(metadata.containsKey("successes")){
            return new BinomialTrial(metadata);
        }
        if(metadata.containsKey("count")){
            return new CountTrial(metadata);
        }
        if(metadata.containsKey("measurement")){
            return new MeasurementTrial(metadata);
        }
        return new Trial(metadata);
    }

}
