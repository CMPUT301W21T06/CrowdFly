package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Controllers.QuestionAdapter;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.Question;
import com.cmput301w21t06.crowdfly.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This is used to show the active questions
 */
public class ViewQuestionLogActivity extends AppCompatActivity
    implements CrowdFlyListeners.OnDoneGetQuestionsListener
{
    private ListView qListView;
    private QuestionAdapter qAdapter;
    private ExperimentLog expLog;
    private ArrayList<Question> qList;
    static public String expID;
    private Experiment exp;

    Button btnAskQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question_log);
        expID = getIntent().getStringExtra("expID");
        expLog = ExperimentLog.getExperimentLog();
        int expPos = expLog.getExperimentPositionByID(expID);
        exp = expLog.getExperiment(expPos); // get experiment associated with the questions
        qListView = findViewById(R.id.questionListView);
        btnAskQ = findViewById(R.id.askQuestionButton);
        qList = new ArrayList<>();
        qAdapter = new QuestionAdapter(this, qList);
        qListView.setAdapter(qAdapter);

        exp.getQuestionController().getQuestionData(this); 

        btnAskQ.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewQuestionLogActivity.this, AskQuestionActivity.class);
                intent.putExtra("expID", String.valueOf(expID));
                startActivity(intent);
            }
        });

        qListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Question question = (Question) parent.getAdapter().getItem(position);
                String questionID = question.getQuestionID();
                Intent intent = new Intent(ViewQuestionLogActivity.this, QuestionThreadActivity.class);
                intent.putExtra("expID", expID);
                intent.putExtra("qID", questionID);
                startActivity(intent);
            }
        });
    }

    /***
     * Handler for getting questions from database.
     * Updates the question list
     * @param questions list of questions to update
     */
    @Override
    public void onDoneGetQuestions(ArrayList<Question> questions) {
        this.qList = questions;
        qAdapter.clear();
        qAdapter.addAll(qList);
        qAdapter.notifyDataSetChanged();
    }
}
