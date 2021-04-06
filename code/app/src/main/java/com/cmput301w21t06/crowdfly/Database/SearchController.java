package com.cmput301w21t06.crowdfly.Database;

import android.util.Log;

import androidx.annotation.Nullable;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;

import org.json.JSONObject;

import java.util.Map;

public class SearchController {
    private static Client client = new Client("QJJ0ZUSNNJ","9236b464caa13c4420830ff5b68cdae0");
    private static Index index = client.getIndex("experiments_data");
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


}
