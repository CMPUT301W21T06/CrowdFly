package com.cmput301w21t06.crowdfly.Controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmput301w21t06.crowdfly.Models.Question;
import com.cmput301w21t06.crowdfly.R;

import java.util.ArrayList;

public class QuestionContent extends ArrayAdapter<Question> {
    private ArrayList<Question> questions;
    private Context context;

    /***
     * QuestionContent constructor
     * @param context
     * @param questions
     */
    public QuestionContent(Context context, ArrayList<Question> questions) {
        super(context, 0, questions);
        this.questions = questions;
        this.context = context;
    }

    /***
     * Gets the view of an individual question from a list of questions
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.question_content, parent, false);
        }

        Question question = questions.get(position);

        TextView date = view.findViewById(R.id.qDate);
        TextView uid = view.findViewById(R.id.askerID);
        TextView replies = view.findViewById(R.id.qReplies);
        TextView qSnippet = view.findViewById(R.id.qSnippet);

        date.setText("2021-04-01");
        uid.setText("wonbin");
        replies.setText("2");
        qSnippet.setText("What is a question?");

        return view;
    }

}
