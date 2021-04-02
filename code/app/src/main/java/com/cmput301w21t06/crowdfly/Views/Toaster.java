package com.cmput301w21t06.crowdfly.Views;

import android.content.Context;
import android.widget.Toast;

public interface Toaster {
    public static void makeToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
    public static void makeCrispyToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }
}
