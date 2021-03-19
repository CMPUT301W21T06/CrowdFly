package com.cmput301w21t06.crowdfly.Models;

import java.util.ArrayList;

public class LiveTextUpdater {

    private static void handleEmpty(ArrayList<String> items, ArrayList<String> template ){
        items.clear();
        for (String id : template){
            items.add(id);
        }
    }
    private static void parse(ArrayList<String> items, ArrayList<String> template, String text){
        items.clear();
        for (String id : template){
            String prefix = id.substring(0,text.length());
            if (prefix.toLowerCase().matches(text.toLowerCase())){
                items.add(id);
            }
        }

    }
    public static void handleChange(ArrayList<String> items, ArrayList<String> template, String text){
        if (text.length() > 0){
            parse(items, template, text);
        }else{
            handleEmpty(items, template);
        }
    }
}
