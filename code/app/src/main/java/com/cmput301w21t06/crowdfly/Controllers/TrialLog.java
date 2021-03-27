package com.cmput301w21t06.crowdfly.Controllers;

import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.Trial;

import java.util.ArrayList;

/**
 * this is the trial log that updates/keeps track of a singleton experiment arraylist
 */
public class TrialLog {

    private static final TrialLog singleton = new TrialLog();

    /** Gets the singleton instance of the TrialManager
     * @return singleton
     */
    public static TrialLog getTrialLog(){
        return singleton;
    }

    private ArrayList<Trial> trials;

    /** Initialize the TrialManager singleton.
     */
    TrialLog(){
        trials = new ArrayList<Trial>();
    }

    /** Gets the current list of trials
     * @return A list of trials.
     * */
    public ArrayList<Trial> getTrials() {
        return trials;
    }

    /** Removes a trial at the position
     * @param position The index of the experiment to remove
     * */
    public void removeTrial(int position) {
        trials.remove(position);
    }

    /** Gets the trial at the position
     * @param position The index of the trial to get.
     * @return An trial.
     * */
    public Trial getTrial(int position) {
        return trials.get(position);
    }

    /** Adds the experiment at the end of the ArrayList experiments.
     * @param trial The new experiment to add.
     * */
    public void addTrial(Trial trial) {
        this.trials.add(trial);
    }

    /***
     * Resets the values of trial list
     */
    public void resetTrialLog() { trials = new ArrayList<>(); }

    /**
     * sets a trial in a particular position
     * @param position
     * @param trial
     */
    public void set(int position, Trial trial){
        this.trials.set(position,trial);
    }
}
