package com.cmput301w21t06.crowdfly.Controllers;

import android.widget.TextView;

import com.cmput301w21t06.crowdfly.Views.ViewLocationActivity;

public interface RegionViewSetter {
    public static void setRegion(TextView view, String loc){
        String location = "";
        if (!loc.matches("")) {
            Double[] arr = ViewLocationActivity.parseStringLocation(loc);
            location = ViewLocationActivity.getStringLocation(arr[0], arr[1], true);
        }
        view.setText(location);
    }
}
