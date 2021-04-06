package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301w21t06.crowdfly.Controllers.CommentAdapter;
import com.cmput301w21t06.crowdfly.Controllers.ExperimentLog;
import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Models.Comment;
import com.cmput301w21t06.crowdfly.Models.Experiment;
import com.cmput301w21t06.crowdfly.Models.Question;
import com.cmput301w21t06.crowdfly.R;

import java.util.ArrayList;

/***
 * Shows comments within a question
 */
public class QuestionThreadActivity extends AppCompatActivity
    implements CrowdFlyListeners.OnDoneGetCommentsListener
{
    private ListView commentListView;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> cList;
    static public String expID;
    static public String qID;
    private ExperimentLog expLog;
    private Question q;

    private TextView askerID;
    private TextView questionDesc;
    private TextView qDate;
    private Button btnComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_thread);
        expID = getIntent().getStringExtra("expID");
        qID = getIntent().getStringExtra("qID");
        expLog = ExperimentLog.getExperimentLog();

        // get the question related to this comment
        int expPos = expLog.getExperimentPositionByID(expID);
        Experiment exp = expLog.getExperiment(expPos);
        q = exp.getQuestionByID(qID);

        // get views
        askerID = findViewById(R.id.qAskerID);
        questionDesc = findViewById(R.id.chosen_question);
        qDate = findViewById(R.id.qDate2);
        commentListView = findViewById(R.id.comment_list);
        btnComment = findViewById(R.id.commentButton);

        // set texts
        askerID.setText(q.getUsername());
        questionDesc.setText(q.getQuestion());
        qDate.setText(q.getDate());

        cList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, cList);
        commentListView.setAdapter(commentAdapter);

        q.getCommentController().getCommentData(this);

        btnComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionThreadActivity.this, PostCommentActivity.class);
                intent.putExtra("expID", String.valueOf(expID));
                intent.putExtra("qID", String.valueOf(qID));
                startActivity(intent);
            }
        });
    }

    /***
     * Handler for getting comments from database.
     * Updates the comments list
     * @param comments list of comments to update
     */
    @Override
    public void onDoneGetComments(ArrayList<Comment> comments) {
        this.cList = comments;
        commentAdapter.clear();
        commentAdapter.addAll(cList);
        commentAdapter.notifyDataSetChanged();
    }
}