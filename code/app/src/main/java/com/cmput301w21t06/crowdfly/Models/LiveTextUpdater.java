package com.cmput301w21t06.crowdfly.Models;

import java.util.ArrayList;

public class LiveTextUpdater {
    /**
     * This handles updating an array if what the user is searching for has yet to be entered
     * @param items
     * This is the array to be updated
     * @param template
     * This is the template array with all the items to search through, this is never updated
     */
    private static void handleEmpty(ArrayList<String> items, ArrayList<String> template ){
        items.clear();
        for (String id : template){
            items.add(id);
        }
    }

    /**
     * This parses through what the user is searching for and updates the array accordingly
     * @param items
     * The array to be updated
     * @param template
     * The never updated array with all the possible search items
     * @param text
     * The text to search for
     */
    private static void parse(ArrayList<String> items, ArrayList<String> template, String text){
        items.clear();
        for (String id : template){
            String prefix = id.substring(0,text.length());
            if (prefix.toLowerCase().matches(text.toLowerCase())){
                items.add(id);
            }
        }
    }

    /**
     * This handles a change in the user input
     * @param items
     * The array to be updated
     * @param template
     * The never updated array with all the possible search items
     * @param text
     * The text to search for
     */
    public static void handleChange(ArrayList<String> items, ArrayList<String> template, String text){
        if (text.length() > 0){
            parse(items, template, text);
        }else{
            handleEmpty(items, template);
        }
    }
}
