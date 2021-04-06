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

/**
 * This is a custom adapter for items in drop down menus
 */
public class DropdownAdapter extends ArrayAdapter<String> {
    private ArrayList<Integer> pos;
    boolean filterFirst;

    /**
     * This is the constructor for the custom adapter, which creates the superobject and other items
     * @param context
     * The application context
     * @param resourceID
     * The layout of the items in the adapter
     * @param items
     * The list of items that feed the adapter
     */
    public DropdownAdapter(Context context, int resourceID, ArrayList<String> items, boolean filterFirst){
        super(context,resourceID,items);
        pos = new ArrayList<Integer>();
        this.filterFirst = filterFirst;
        Log.e("FUCK",String.valueOf(items));
    }

    /**
     * This logs the position of the user selection for correct handling
     * @param position
     * This is the position clicked
     * @return
     * This indicates if the item was added (was not pre-selected) or deleted (was pre-selected)
     */
    public boolean addSelectedPosition(Integer position){
        if (pos.contains(position)){
            pos.remove(position);
            return false;
        }else{
            pos.add(position);
            return true;
        }
    }

    /**
     * This handles creating views for items in the dropdown
     * @param position
     * The position of the item to be displayed
     * @param convertView
     * An old view which can be converted to save time
     * @param parent
     * The parent view group
     * @return
     * The created view
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (!filterFirst || (filterFirst && position != 0)) {
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
