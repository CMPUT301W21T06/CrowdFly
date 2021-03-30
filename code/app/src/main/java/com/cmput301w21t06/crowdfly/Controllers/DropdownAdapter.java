package com.cmput301w21t06.crowdfly.Controllers;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DropdownAdapter extends ArrayAdapter<String> {
    private ArrayList<Integer> pos;
    public DropdownAdapter(Context context, int resourceID, ArrayList<String> items){
        super(context,resourceID,items);
        pos = new ArrayList<Integer>();
    }

    public boolean addSelectedPosition(Integer position){
        Log.e("pos",String.valueOf(position));
        if (pos.contains(position)){
            Log.e("rrhrh","heheh");
            pos.remove(position);
            Log.e("stuff",String.valueOf(pos));
            return false;
        }else{
            Log.e("rrhrh","heheh");
            pos.add(position);
            Log.e("stuff",String.valueOf(pos));

            return true;
        }
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (position != 0) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setHeight(100);
            if (pos.contains(position)){
                view.setTextColor(Color.RED);
            }
            else{
                view.setTextColor(Color.BLACK);
            }
            return view;
        }else{
            TextView view = new TextView(getContext());
            view.setVisibility(View.GONE);
            view.setHeight(0);
            return view;
        }
    }
}
