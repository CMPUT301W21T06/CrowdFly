package com.cmput301w21t06.crowdfly.Database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.IndexQuery;
import com.algolia.search.saas.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SearchController {
    private static Client client = new Client("QJJ0ZUSNNJ","9236b464caa13c4420830ff5b68cdae0");
    private static final String indexName = "experiments_data";
    private static Index index = client.getIndex(indexName);
    private static HashSet<String> masks = new HashSet<String>();

    private static CompletionHandler handler = new CompletionHandler() {
        @Override
        public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
            if ( e == null){
                Log.i("Algolia","Operation completed " + String.valueOf(jsonObject));
            }
            else{
                Log.e("Algolia",String.valueOf(e));
            }
        }
    };


    /**
     * This allows an item to be added to Algolia
     * @param map
     * This is the item to be added
     * @param id
     * This is the object ID for it to be added at
     */
    public static void addObject(Map<String,Object> map, String id){
        JSONObject obj = new JSONObject(map);
        index.addObjectAsync(obj,id,handler);
    }

    /**
     * This deletes an item from Algolia
     * @param id
     * This is the id of the item to delete
     */
    public static void deleteObject(String id){
        index.deleteObjectAsync(id,handler);
    }

    /**
     * This updates an item in Algolia
     * @param map
     * This is the updated object's map
     * @param id
     * This is the id of the object to update
     */
    public static void updateObject(Map<String,Object> map, String id){
        JSONObject obj = new JSONObject(map);
        index.saveObjectAsync(obj,id,handler);
    }

    /**
     * This is provides query functionaltiy with Algolia
     * @param handlerMultiple
     * This is the class that implements the query's callback
     * @param symbol
     * This is the symbol the user chose
     * @param trial
     * The is the number they entered into the right trial box
     * @param trialLeft
     * This is the the number they entered into the left trial box
     * @param region
     * This is the level of region enforcement they chose from the dropdown box
     * @param active
     * This the level of activity/publication they chose from the dropdown box
     * @param general
     * This is the general text from the query
     */
    public static void query(CompletionHandler handlerMultiple, String symbol, String trial, String trialLeft, String region, String active, String general) {
        clearMasks();
        List<IndexQuery> queries = new ArrayList<IndexQuery>();
        String searchString = "";
        if (!(symbol.matches("N/A") && trial.matches("") && region.matches("N/A") && active.matches("N/A") && general.matches(""))) {
            if (symbol.matches("TO") && validTrialToFilter(trialLeft,trial)){
                searchString = searchString.concat("minTrials:" + trialLeft + " " + symbol + " " + trial);
            }
            else if (validTrialFilter(symbol,trial) && !(symbol.matches("N/A") || symbol.matches("TO"))){
                searchString = searchString.concat("minTrials " + symbol + " " + trial);

            }
            if (!region.matches("N/A")){
                String enabledString = "enabled:true";
                if (region.matches("Not Enforced")){
                    enabledString = "enabled:false";
                }
                searchString = combineString(searchString,enabledString);

            }

            if (!active.matches("N/A")){
                    String activeString = "stillRunning:true";
                    if (active.matches("Not Active")){
                        activeString = "stillRunning:false";
                    }
                searchString = combineString(searchString, activeString);
            }

            Log.i("MASK", searchString);
            index.searchAsync(new Query(general).setFilters(searchString), handlerMultiple);
        }
    }

    private static String addAnd(String searchString) {
        if(searchString.length() > 0){
            searchString = searchString.concat(" AND ");
        }
        return searchString;
    }

    public static boolean validTrialFilter(String symbol, String tFilter){
       if ((symbol.matches("N/A") && !tFilter.matches("")) || (tFilter.matches("") && !symbol.matches("N/A"))){
           return false;
        }
       return true;
    }

    public static boolean validTrialToFilter(String tFilterL,String tFilter){
        if ((tFilter.matches("") || tFilterL.matches(""))){
            return false;
        }
        return true;
    }
    public static HashSet<String> getMasks(){
        return masks;
    }
    public static void addMask(String mask){
        masks.add(mask);
    }
    public static void clearMasks(){
        masks.clear();
    }

    private static String combineString(String searchString, String addString){
        searchString = addAnd(searchString);
        searchString = searchString.concat(addString);
        return searchString;
    }

}
