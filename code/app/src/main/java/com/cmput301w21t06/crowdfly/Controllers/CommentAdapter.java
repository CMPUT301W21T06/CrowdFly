package com.cmput301w21t06.crowdfly.Controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmput301w21t06.crowdfly.Models.Comment;
import com.cmput301w21t06.crowdfly.R;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private ArrayList<Comment> comments;
    private Context context;

    /***
     * commentContent constructor
     * @param context
     * @param comments
     */
    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
        this.comments = comments;
        this.context = context;
    }

    /***
     * Gets the view of an individual comment from a list of comments
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.comment_content, parent, false);
        }

        Comment comment = comments.get(position);

//        TextView date = view.findViewById(R.id.qDate);
        TextView uid = view.findViewById(R.id.commenterID);
        TextView qSnippet = view.findViewById(R.id.cSnippet);

//        date.setText(comment.getDate());
        uid.setText(comment.getUsername());
        qSnippet.setText(comment.getComment());

        return view;
    }
}
