// ExperimentLog Singleton code adapted from Professor Abram Hindle
// https://eclass.srv.ualberta.ca/mod/forum/discuss.php?d=1611626
// Date Posted: Monday, 18 January 2021, 12:10 PM

package com.cmput301w21t06.crowdfly.Controllers;

import com.cmput301w21t06.crowdfly.Models.Experiment;

import java.util.ArrayList;


/**
 * this is the experiment log that updates/keeps track of a singleton experiment arraylist
 */
public class ExperimentLog {


    private static final ExperimentLog singleton = new ExperimentLog();

    /** Gets the singleton instance of the ExperimentManager
     * @return singleton
     */
    public static ExperimentLog getExperimentLog(){
        return singleton;
    }

    private ArrayList<Experiment> experiments;

    /** Initialize the ExperimentManager singleton.
     */
    ExperimentLog(){
        experiments = new ArrayList<Experiment>();
    }

    /** Gets the current list of experiments
     * @return A list of experiments.
     * */
    public ArrayList<Experiment> getExperiments() {
        return experiments;
    }

    public int getSize(){
        return experiments.size();
    }

    /** Removes and experiment at the position
     * @param position The index of the experiment to remove
     * */
    public void removeExperiment(int position) {
        experiments.remove(position);
    }

    /** Gets the experiment at the position
     * @param position The index of the experiment to get.
     * @return An experiment.
     * */
    public Experiment getExperiment(int position) {
        return experiments.get(position);
    }

    /** Adds the experiment at the end of the ArrayList experiments.
     * @param experiment The new experiment to add.
     * */
    public void addExperiment(Experiment experiment) {
        this.experiments.add(experiment);
    }

    /***
     * Resets the values of experiment list
     */
    public void resetExperimentLog() { experiments = new ArrayList<>(); }

    /***
     * Get experiment by ID
     * @param expID
     * @return
     */
    public int getExperimentPositionByID(String expID) {
        int expPosition = 0;
        for (Experiment exp : experiments) {
            String compId = exp.getExperimentId();
            if (compId.compareTo(expID) == 0) {
                return expPosition;
            }
            expPosition++;
        }
        // if no experiment found with id, return an invalid position
        return -1;
    }

    /**
     * sets a experiment in a particular position
     * @param position
     * @param exp
     */
    public void set(int position, Experiment exp){
        this.experiments.set(position,exp);
    }
}
