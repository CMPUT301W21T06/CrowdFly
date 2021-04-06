package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.Nullable;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.IndexQuery;
import com.algolia.search.saas.Query;
import com.cmput301w21t06.crowdfly.Views.Toaster;
import com.cmput301w21t06.crowdfly.Views.ViewExperimentLogActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchController {
    private static Client client = new Client("QJJ0ZUSNNJ","9236b464caa13c4420830ff5b68cdae0");
    private static final String indexName = "experiments_data";
    private static Index index = client.getIndex(indexName);
    private static final Client.MultipleQueriesStrategy strategy = Client.MultipleQueriesStrategy.NONE;
    private static ArrayList<String> masks = new ArrayList<String>();

    private static CompletionHandler handler = new CompletionHandler() {
        @Override
        public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
            if (jsonObject != null){
                Log.i("Algolia","Operation completed " + String.valueOf(jsonObject));
            }
            else{
                Log.e("Algolia",String.valueOf(e));
            }
        }
    };

    private static CompletionHandler handlerMultiple= new CompletionHandler() {
        @Override
        public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
            if (jsonObject != null){
                Log.i("Algolia","Operation completed " + String.valueOf(jsonObject));
            }
            else{
                Log.e("Algolia",String.valueOf(e));
            }
        }
    };




    public static void addObject(Map<String,Object> map, String id){
        JSONObject obj = new JSONObject(map);
        index.addObjectAsync(obj,id,handler);
    }

    public static void deleteObject(String id){
        index.deleteObjectAsync(id,handler);
    }

    public static void updateObject(Map<String,Object> map, String id){
        JSONObject obj = new JSONObject(map);
        index.saveObjectAsync(obj,id,handler);
    }

    public static void query(String symbol, String trial, String trialLeft, String region, String active, String general){
        clearMasks();
        List<IndexQuery> queries = new ArrayList<IndexQuery>();
        if (!(symbol.matches("N/A") && trial.matches("") && region.matches("N/A") && active.matches("N/A") && general.matches(""))) {
            if (symbol.matches("TO") && validTrialToFilter(trialLeft,trial)){
                queries.add(new IndexQuery(indexName,new Query("").setFilters("minTrials:" + trialLeft + " " + symbol + " " + trial)));
            }
            else if (validTrialFilter(symbol,trial) && !(symbol.matches("N/A") || symbol.matches("TO"))){
                queries.add(new IndexQuery(indexName,new Query("").setFilters("minTrials " + symbol + " " + trial)));
            }
            if (!region.matches("N/A")){
                String enabledString = "enabled:true";
                if (!region.matches("Not Enforced")){
                    enabledString = "enabled:false";
                }
                queries.add(new IndexQuery(indexName,new Query("").setFilters(enabledString)));
            }

            if (!active.matches("N/A")){
                    String activeString = "stillRunning:true";
                    if (!region.matches("Not Active")){
                        activeString = "stillRunning:false";
                    }
                    queries.add(new IndexQuery(indexName,new Query("").setFilters(activeString)));
            }

            if (!general.matches("")){
                queries.add(new IndexQuery(indexName,new Query(general)));
            }

            client.multipleQueriesAsync(queries,strategy,handlerMultiple);
        }


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

    public static void clearMasks(){
        masks.clear();
    }

}
