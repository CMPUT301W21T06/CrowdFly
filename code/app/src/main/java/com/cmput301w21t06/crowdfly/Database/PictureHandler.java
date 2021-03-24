package com.cmput301w21t06.crowdfly.Database;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PictureHandler {
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    public static final String qasimHehe = "gs://crowdfly-76eb6.appspot.com/smiley.png";

    /**
     * This method retrieves items from storage at the particular URL
     * @param URL
     * This is the URL of the image in storage
     */
    public static StorageReference getPic(String URL, ImageView view, Context context) {
        return storage.getReferenceFromUrl(URL);
    }


    private static void onDoneGetPic(StorageReference pic, ImageView view, Context context) {
        Glide.with(context)
                .load(pic)
                .fitCenter()
                .into(view);
    }

    /**
     * This method handles loading images into the views to avoid repetitive code
     * @param URL
     * The URL of the image
     * @param view
     * The view to load the image into
     * @param context
     * The application contexts
     */
    public static void updatePic(String URL, ImageView view, Context context){
        onDoneGetPic(getPic(URL, view, context), view, context);
    }

}
