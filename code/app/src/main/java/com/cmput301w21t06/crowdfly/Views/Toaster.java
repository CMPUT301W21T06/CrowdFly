package com.cmput301w21t06.crowdfly.Views;

import android.content.Context;
import android.widget.Toast;

/**
 * This interface handles making toasts
 */
public interface Toaster {
    /**
     * This makes short lasting toasts
     * @param context
     * This is the activity context
     * @param s
     * This is the string to depict
     */
    public static void makeToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * This makes long lasting toasts i.e. extra crispy
     * @param context
     * This is the activity context
     * @param s
     * This is the string to depict
     */
    public static void makeCrispyToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }
}
