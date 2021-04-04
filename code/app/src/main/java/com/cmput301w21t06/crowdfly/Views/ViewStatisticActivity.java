package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cmput301w21t06.crowdfly.Controllers.TrialLog;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Database.ExperimentController;
import com.cmput301w21t06.crowdfly.Models.BinomialTrial;
import com.cmput301w21t06.crowdfly.Models.CountTrial;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.MeasurementTrial;
import com.cmput301w21t06.crowdfly.Models.Trial;
import com.cmput301w21t06.crowdfly.R;
import android.os.Bundle;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This will be used to show the statistics screen
 * Required statistics implemented
 * Results of trials/over time yet to be implemented
 * Histogram of trials has now been implemented
 */
public class ViewStatisticActivity extends AppCompatActivity implements CrowdFlyListeners.OnDoneGetExpListener,CrowdFlyListeners.OnDoneGetTrialsListener {
    public String trialType;
    public String measurement;
    public Experiment exp;
    public Trial trial;
    public TrialLog trialLog;
    private static ArrayList<Trial> trialArrayList = new ArrayList<Trial>();
    public String expID;

    public TextView firstQuartileTextView;
    public TextView thirdQuartileTextView;
    public TextView medianTextView;
    public TextView meanTextView;
    public TextView sdTextView;
    public TextView minTextView;
    public TextView maxTextView;
    public BarChart barChart;
    final String notApplicableMsg = "NA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistic);

        barChart = findViewById(R.id.barchart);
        firstQuartileTextView = findViewById(R.id.sFirstQuartile);
        thirdQuartileTextView = findViewById(R.id.sThirdQuartile);
        medianTextView = findViewById(R.id.sMedian);
        meanTextView = findViewById(R.id.sMean);
        sdTextView = findViewById(R.id.sStdDev);
        minTextView = findViewById(R.id.sMinimum);
        maxTextView = findViewById(R.id.sMaximum);

        expID = getIntent().getStringExtra("expID");
        ExperimentController.getExperimentData(expID,this);
        trialLog.getTrials();

        if (trialArrayList.isEmpty()){
            setNotApplicableMessages(notApplicableMsg);
        }
        else{
            displayStats();
            displayHistogram();
        }

    }
    /**
     * This method displays the statistical information in respect to its current trials
     */
    private void displayStats(){

        double mean = mean(trialArrayList);
        double median = median(trialArrayList);
        if (median == -1){
            setNotApplicableMessages(notApplicableMsg);
            return;
        }

        double max = max(trialArrayList);
        double min = min(trialArrayList);
        double firstQuartile = firstQuartile(trialArrayList);
        double thirdQuartile = thirdQuartile(trialArrayList);
        double sd = standardDeviation(trialArrayList);


        medianTextView.setText(String.valueOf(median));
        meanTextView.setText(String.valueOf(mean));
        sdTextView.setText(String.valueOf(sd));
        minTextView.setText(String.valueOf(min));
        maxTextView.setText(String.valueOf(max));

        if (firstQuartile == -1) {
            firstQuartileTextView.setText(notApplicableMsg);
            thirdQuartileTextView.setText(notApplicableMsg);
        }
        else{
            firstQuartileTextView.setText(String.valueOf(firstQuartile));
            thirdQuartileTextView.setText(String.valueOf(thirdQuartile));
        }

    }
    /**
     * This method displays the histogram in respect to its current trials
     */
    private void displayHistogram(){

        ArrayList<BarEntry> entries = new ArrayList<>();
        BarDataSet bardataset = new BarDataSet(entries, "Cells");
        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<Double> barChartTrials = getTrialList(this.trialType,trialArrayList);

        System.out.println(barChartTrials);

        if (barChartTrials.isEmpty()){
            barChart.removeAllViews();
        }
        else {

            for (int i = 0; i < barChartTrials.size(); i++) {
                float x = barChartTrials.get(i).floatValue();
                System.out.println(x);
                entries.add(new BarEntry(x, i));
                labels.add("Trial " + i);
            }
            BarData data = new BarData(labels, bardataset);
            barChart.setData(data); // set the data and list of labels into chart
            barChart.setDescription(trialType.toUpperCase() + " TRIALS");  // set the description
            barChart.animateY(2000);
        }
    }
    /**
     * This method handles when the trial array list is empty
     * @param message
     */
    private void setNotApplicableMessages(String message){
        medianTextView.setText(String.valueOf(message));
        meanTextView.setText(String.valueOf(message));
        sdTextView.setText(String.valueOf(message));
        minTextView.setText(String.valueOf(message));
        maxTextView.setText(String.valueOf(message));
        firstQuartileTextView.setText(message);
        thirdQuartileTextView.setText(message);
    }

    /**
     * This handles getting the experiment after experiment has been pulled from the database
     * @param experiment
     */

    @Override
    public void onDoneGetExperiment(Experiment experiment) {
        exp = experiment;
        trialType = getIntent().getStringExtra("trialType");
        exp.getTrialController().getTrialLogData(this, new ArrayList<>());
    }
    /**
     * This handles getting the trial log after trial log has been pulled from the database
     * @param trialLog
     */

    @Override
    public void onDoneGetTrials(TrialLog trialLog) {
        this.trialLog = trialLog;
        trialArrayList = trialLog.getTrials();

    }
    /**
     * Given the trialArrayList this method will return the mean for the current trial
     * @param trialArrayList
     * @return mean of given trial
     */

    private double mean(ArrayList<Trial> trialArrayList){
        double sum = 0;
        ArrayList<Double> trials = getTrialList(this.trialType,trialArrayList);
        int length = trials.size();
        for(double n: trials)
            sum+=n;
        return sum/length;
    }

    /**
     * Given the trialArrayList this method will return the standard deviation for the current trial
     * @param trialArrayList
     * @return standard deviation of given trial
     */
    private double standardDeviation(ArrayList<Trial> trialArrayList){
        double sum = 0;
        double sd = 0;
        ArrayList<Double> trials = getTrialList(this.trialType,trialArrayList);
        Collections.sort(trials);
        int length = trials.size();

        for(double n: trials){
            sum+=n;
        }
        double mean = sum/length;
        for(double n: trials){
            sd += Math.pow(n - mean,2);
        }
        return Math.sqrt(sd/length);
    }

    /**
     * Given the trialArrayList this method will return the median for the current trial
     * @param trialArrayList
     * @return median of given trial
     */
    private double median(ArrayList<Trial> trialArrayList) {
        int length = trialArrayList.size();

        ArrayList<Double> trials = getTrialList(this.trialType,trialArrayList);
        System.out.println("THIS ONE"+trials);
        Collections.sort(trials);

        if (trials.isEmpty())
            return -1;

        if (length % 2 !=0)
            return trials.get(length / 2);

        double n1 = trials.get((length/2)-1);
        double n2 = trials.get(length/2);
        return (n1+n2)/2;

    }
    /**
     * Given the trialArrayList this method will return the maximum value for the current trial
     * @param trialArrayList
     * @return max of given trial
     */
    private double max(ArrayList<Trial> trialArrayList){
        ArrayList<Double> trials = getTrialList(this.trialType,trialArrayList);
        return Collections.max(trials);

    }
    /**
     * Given the trialArrayList this method will return the minimum for the current trial
     * @param trialArrayList
     * @return min of given trial
     */
    private double min(ArrayList<Trial> trialArrayList){
        ArrayList<Double> trials = getTrialList(this.trialType,trialArrayList);
        return Collections.min(trials);
    }
    /**
     * Given the trialArrayList this method will return the first quartile for the current trial
     * First quartile will be displayed as NA if the size of the list of trials is less than 4
     * @param trialArrayList
     * @return first quartile of given trial
     */
    private double firstQuartile(ArrayList<Trial> trialArrayList){
        ArrayList<Double> trials = getTrialList(this.trialType,trialArrayList);
        Collections.sort(trials);

        ArrayList<Double> subTrials;
        int length = trials.size();
        int subTrialLength;

        if (length<4)
            return -1;

        else if (length%2!=0){
            subTrials = new ArrayList<Double>(trials.subList(0,(length/2)));
            subTrialLength = subTrials.size();
            double n1 = subTrials.get((subTrialLength/2)-1);
            double n2 = subTrials.get(subTrialLength/2);
            return (n1+n2)/2;
        }
        else{
            subTrials = new ArrayList<Double>(trials.subList(0,(length/2)));
            subTrialLength = subTrials.size();
            return subTrials.get(subTrialLength / 2);
        }
    }
    /**
     * Given the trialArrayList this method will return the standard deviation for the current trial
     * Third quartile will be displayed as NA if the size of the list of trials is less than 4
     * @param trialArrayList
     * @return third quartile of given trial
     */
    private double thirdQuartile(ArrayList<Trial> trialArrayList){
        ArrayList<Double> trials = getTrialList(this.trialType,trialArrayList);
        Collections.sort(trials);

        ArrayList<Double> subTrials;
        int length = trials.size();
        int subTrialLength;

        if (length<4)
            return -1;

        else if (length%2!=0){
            subTrials = new ArrayList<Double>(trials.subList((length/2)+1,length));
            subTrialLength = subTrials.size();

            double n1 = subTrials.get((subTrialLength/2)-1);
            double n2 = subTrials.get(subTrialLength/2);
            return (n1+n2)/2;
        }
        else{
            subTrials = new ArrayList<Double>(trials.subList((length/2),length));
            subTrialLength = subTrials.size();
            return subTrials.get(subTrialLength / 2);
        }

    }
    /**
     * Given the type of trial and trial array list this method will return an array list of doubles
     * containing trials in respect to its trial type
     * @param trialArrayList
     * @param typeOfTrial
     * @return an array list of doubles for given trial; for binomial trials we set "1"'s for successes and "0"'s for fails to represent statistical information
     */
    private ArrayList<Double> getTrialList(String typeOfTrial, ArrayList<Trial> trialArrayList) {
        ArrayList<Double> trials = new ArrayList<Double>();
        int s = 0;
        int f = 0;
        for (Trial trial : trialArrayList) {
            if ("measurement".equals(typeOfTrial)) {
                trials.add(((MeasurementTrial) trial).getMeasurement());
            }
            else if ("count".equals(typeOfTrial)) {
                trials.add((double) ((CountTrial) trial).getCount());
            }
            else if ("binomial".equals(typeOfTrial)) {
                s+=((BinomialTrial) trial).getSuccesses();
                f+=((BinomialTrial) trial).getFailures();
                for(int i =0; i<s; i++){
                    trials.add((double) 1);
                }
                for(int i =0;i<f;i++){
                    trials.add((double) 0);
                }
            }
        }
        return trials;
    }
}

