package com.cmput301w21t06.crowdfly.Views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w21t06.crowdfly.Controllers.QuestionContent;
import com.cmput301w21t06.crowdfly.Models.Question;
import com.cmput301w21t06.crowdfly.R;

import java.util.ArrayList;

/**
 * This will be used to show the active questions, however it has not been implemented as of yet
 */
public class ViewQuestionLogActivity extends AppCompatActivity {
    private ListView qListView;
    private QuestionContent qAdapter;
    private ArrayList<Question> qList;

    Button btnAskQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question_log);
    }
}
