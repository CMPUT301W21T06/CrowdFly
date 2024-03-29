package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
//https://github.com/PhilJay/MPAndroidChart

/**
 * This will be used to show the statistics screen
 */
public class ViewStatisticActivity extends AppCompatActivity implements CrowdFlyListeners.OnDoneGetExpListener, CrowdFlyListeners.OnDoneGetTrialsListener {
    private String trialType;
    private Experiment exp;
    private TrialLog trialLog;
    private static ArrayList<Trial> trialArrayList = new ArrayList<Trial>();
    private String expID;

    private TextView firstQuartileTextView;
    private TextView thirdQuartileTextView;
    private TextView medianTextView;
    private TextView meanTextView;
    private TextView sdTextView;
    private TextView minTextView;
    private TextView maxTextView;
    private BarChart barChart;
    private final String notApplicableMsg = "NA";
    private GraphView graphView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistic);


        drawerLayout = findViewById(R.id.drawer_stats);
        navigationView = findViewById(R.id.nav_view_stat);
        toolbar = findViewById(R.id.toolbar_stat);
        toolbar.setTitle("CrowdFly");

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });


        barChart = findViewById(R.id.barchart);
        firstQuartileTextView = findViewById(R.id.sFirstQuartile);
        thirdQuartileTextView = findViewById(R.id.sThirdQuartile);
        medianTextView = findViewById(R.id.sMedian);
        meanTextView = findViewById(R.id.sMean);
        sdTextView = findViewById(R.id.sStdDev);
        minTextView = findViewById(R.id.sMinimum);
        maxTextView = findViewById(R.id.sMaximum);
        graphView = findViewById(R.id.graph);

        expID = getIntent().getStringExtra("expID");
        ExperimentController.getExperimentData(expID, this);
        trialLog.getTrials();

        if (trialArrayList.isEmpty()) {
            setNotApplicableMessages(notApplicableMsg);
        } else {
            displayStats();
            displayHistogram();
            displayGraph();
        }

    }

    /**
     * This method adds data points to the graph where the horizontal axis is the date the entry was added and the vertical axis is the current trial's value
     *
     * @return dataPoints
     * this returns the data points
     */

    private ArrayList<DataPoint> getDataPoints() {
        long time;
        ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();
        for (Trial trial : trialArrayList) {
            time = trial.getTimestamp().getSeconds() * 1000L;
            if ("measurement".equals(trialType)) {
                dataPoints.add(new DataPoint(time, ((MeasurementTrial) trial).getMeasurement()));

            } else if ("count".equals(trialType)) {
                dataPoints.add(new DataPoint(time, ((CountTrial) trial).getCount()));

            } else if ("binomial".equals(trialType)) {
                dataPoints.add(new DataPoint(time, ((BinomialTrial) trial).getSuccesses()));
            }
        }
        return dataPoints;
    }

    /**
     * This method sets and displays the graph in respect to its current trials
     */

    private void displayGraph() {
        ArrayList<DataPoint> arr = getDataPoints();
        ArrayList<Double> values = new ArrayList<>();

        for (DataPoint d:arr){
            values.add(d.getX());
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(arr.toArray(new DataPoint[arr.size()]));
        series.setDrawDataPoints(true);
        String title = trialType.toUpperCase() +" TRIALS OVER TIME";
        if (trialType.equals("binomial")){
            title = trialType.toUpperCase() +" SUCCESS TRIALS OVER TIME";
        }
        graphView.addSeries(series);
        graphView.setTitle(title);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMaxX(Collections.max(values)+1);
        graphView.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.HORIZONTAL );
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this,new SimpleDateFormat("dd/MM"+"\n\n"+"HH:mm:ss")));
        graphView.getGridLabelRenderer().setHorizontalLabelsAngle(115);
        graphView.getGridLabelRenderer().setPadding(50);
        graphView.getGridLabelRenderer().setHumanRounding(false);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(trialArrayList.size());
    }
    /**
     * This method displays the statistical information in respect to its current trials
     */
    private void displayStats() {

        double mean = mean(trialArrayList);
        double median = median(trialArrayList);
        if (median == -1) {
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
        } else {
            firstQuartileTextView.setText(String.valueOf(firstQuartile));
            thirdQuartileTextView.setText(String.valueOf(thirdQuartile));
        }

    }

    /**
     * This method displays the histogram in respect to its current trials
     */
    private void displayHistogram() {

        List<BarEntry> barEntries = new ArrayList<BarEntry>();
        ArrayList<Double> barChartTrials = getTrialList(this.trialType, trialArrayList);
        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<String> sortedStringLabels = new ArrayList<String>();
        ArrayList<Double> freq = new ArrayList<>();
        Set<Double> distinct = new HashSet<>(barChartTrials);
        ArrayList<Double> distinctArr = new ArrayList<>(distinct);
        Collections.sort(distinctArr);
        int i;

        for (Double n: distinctArr){
            freq.add((double)Collections.frequency(barChartTrials,n));
        }

        if (barChartTrials.isEmpty()) {
            barChart.removeAllViews();
        } else {

            for (i = 0; i < freq.size(); i++) {
                float x = freq.get(i).floatValue();
                barEntries.add(new BarEntry(i,x));
                labels.add(distinctArr.get(i).toString());
            }
            for (int j = 0; j < distinctArr.size(); j++){
                sortedStringLabels.add(String.valueOf(distinctArr.get(j)));
            }

            BarDataSet dataSet = new BarDataSet(barEntries,"Frequency");
            dataSet.setValueTextSize(12f);
            dataSet.setBarBorderWidth(2.0f);
            XAxis barChartXAxis = barChart.getXAxis();
            barChartXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //barChartXAxis.setLabelCount(i);  // Set the number of labels on the x-axis
            barChartXAxis.setGranularity(1f);
            barChartXAxis.setGranularityEnabled(true);
            barChartXAxis.setTextSize(12f); // The size of the label on the x axis
            barChartXAxis.setDrawGridLines(false); // Set this to true to draw grid lines for this axis.
            barChart.setDrawGridBackground(false);
            BarData data = new BarData(dataSet);
            data.setBarWidth(0.5f);
            barChartXAxis.setValueFormatter(new IndexAxisValueFormatter(sortedStringLabels));
            barChart.setData(data);
            barChart.setFitBars(true); // make the x-axis fit exactly all bars
            barChart.getAxisRight().setEnabled(false);

            barChart.setData(data); // set the data and list of labels into chart
            barChart.animateY(2000);
            barChart.setDescription(null);

        }
    }


    /**
     * This method handles when the trial array list is empty
     *
     * @param message
     */
    private void setNotApplicableMessages(String message) {
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
     *
     * @param experiment
     */

    @Override
    public void onDoneGetExperiment(Experiment experiment) {
        exp = experiment;
        trialType = getIntent().getStringExtra("trialType");
        exp.getTrialController().getTrialLogData(this);
    }

    /**
     * This handles getting the trial log after trial log has been pulled from the database
     *
     * @param trialLog
     */

    @Override
    public void onDoneGetTrials(TrialLog trialLog) {
        this.trialLog = trialLog;
        trialArrayList = trialLog.getTrials();
        Collections.reverse(trialArrayList);

    }

    /**
     * Given the trialArrayList this method will return the mean for the current trial
     *
     * @param trialArrayList
     * @return mean of given trial
     */

    private double mean(ArrayList<Trial> trialArrayList) {
        double sum = 0;
        ArrayList<Double> trials = getTrialList(this.trialType, trialArrayList);
        int length = trials.size();
        for (double n : trials)
            sum += n;
        return  Math.round((sum / length)*100.0) / 100.0;
    }

    /**
     * Given the trialArrayList this method will return the standard deviation for the current trial
     *
     * @param trialArrayList
     * @return standard deviation of given trial
     */
    private double standardDeviation(ArrayList<Trial> trialArrayList) {
        double sum = 0;
        double sd = 0;
        ArrayList<Double> trials = getTrialList(this.trialType, trialArrayList);
        Collections.sort(trials);
        int length = trials.size();

        for (double n : trials) {
            sum += n;
        }
        double mean = sum / length;
        for (double n : trials) {
            sd += Math.pow(n - mean, 2);
        }
        return Math.round((Math.sqrt(sd / length)) * 100.0) / 100.0;
    }

    /**
     * Given the trialArrayList this method will return the median for the current trial
     *
     * @param trialArrayList
     * @return median of given trial
     */
    private double median(ArrayList<Trial> trialArrayList) {
        int length = trialArrayList.size();

        ArrayList<Double> trials = getTrialList(this.trialType, trialArrayList);
        Collections.sort(trials);

        if (trials.isEmpty())
            return -1;

        if (length % 2 != 0)
            return trials.get(length / 2);

        double n1 = trials.get((length / 2) - 1);
        double n2 = trials.get(length / 2);
        return (n1 + n2) / 2;

    }

    /**
     * Given the trialArrayList this method will return the maximum value for the current trial
     *
     * @param trialArrayList
     * @return max of given trial
     */
    private double max(ArrayList<Trial> trialArrayList) {
        ArrayList<Double> trials = getTrialList(this.trialType, trialArrayList);
        return Collections.max(trials);

    }

    /**
     * Given the trialArrayList this method will return the minimum for the current trial
     *
     * @param trialArrayList
     * @return min of given trial
     */
    private double min(ArrayList<Trial> trialArrayList) {
        ArrayList<Double> trials = getTrialList(this.trialType, trialArrayList);
        return Collections.min(trials);
    }

    /**
     * Given the trialArrayList this method will return the first quartile for the current trial
     * First quartile will be displayed as NA if the size of the list of trials is less than 4
     *
     * @param trialArrayList
     * @return first quartile of given trial
     */
    private double firstQuartile(ArrayList<Trial> trialArrayList) {
        ArrayList<Double> trials = getTrialList(this.trialType, trialArrayList);
        Collections.sort(trials);

        ArrayList<Double> subTrials;
        int length = trials.size();
        int subTrialLength;

        if (length < 4)
            return -1;

        else if (length % 2 != 0) {
            subTrials = new ArrayList<>(trials.subList(0, (length / 2)+1));
            subTrialLength = subTrials.size();
            double n1 = subTrials.get((subTrialLength / 2) - 1);
            double n2 = subTrials.get(subTrialLength / 2);
            return (n1 + n2) / 2;
        } else {
            subTrials = new ArrayList<>(trials.subList(0, (length / 2)));
            subTrialLength = subTrials.size();
            return subTrials.get(subTrialLength / 2);
        }
    }

    /**
     * Given the trialArrayList this method will return the standard deviation for the current trial
     * Third quartile will be displayed as NA if the size of the list of trials is less than 4
     *
     * @param trialArrayList
     * @return third quartile of given trial
     */
    private double thirdQuartile(ArrayList<Trial> trialArrayList) {
        ArrayList<Double> trials = getTrialList(this.trialType, trialArrayList);
        Collections.sort(trials);

        ArrayList<Double> subTrials;
        int length = trials.size();
        int subTrialLength;

        if (length < 4)
            return -1;

        else if (length % 2 != 0) {
            subTrials = new ArrayList<>(trials.subList((length / 2) + 1, length));
            subTrialLength = subTrials.size();
            double n1 = subTrials.get((subTrialLength / 2) - 1);
            double n2 = subTrials.get(subTrialLength / 2);
            return (n1 + n2) / 2;
        } else {
            subTrials = new ArrayList<>(trials.subList((length / 2), length));
            subTrialLength = subTrials.size();
            return subTrials.get(subTrialLength / 2);
        }

    }

    /**
     * Given the type of trial and trial array list this method will return an array list of doubles
     * containing trials in respect to its trial type
     *
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
            } else if ("count".equals(typeOfTrial)) {
                trials.add((double) ((CountTrial) trial).getCount());
            } else if ("binomial".equals(typeOfTrial)) {
                s += ((BinomialTrial) trial).getSuccesses();
                f += ((BinomialTrial) trial).getFailures();
            }
        }
        if ("binomial".equals(typeOfTrial)) {
            for (int i = 0; i < s; i++) {
                trials.add((double) 1);
            }
            for (int i = 0; i < f; i++) {
                trials.add((double) 0);
            }
        }
        return trials;
    }

}

