package com.dabutvin.pikeorpine;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 3/1/2017.
 */
public class DeserializePlacesTask extends AsyncTask<String, Void, List<Place>> {
    private PlacesCallbackInterface callback;

    DeserializePlacesTask(PlacesCallbackInterface callback){

        this.callback = callback;
    }

    @Override
    protected List<Place> doInBackground(String... params) {
        String json = params[0];

        List<Place> places = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonPlacesArray = jsonObject.getJSONArray("places");
            for(int i=0; i<jsonPlacesArray.length(); i++) {
                JSONObject jsonPlaceObject = jsonPlacesArray.getJSONObject(i);
                places.add(Place.ToPlace(jsonPlaceObject));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return places;
    }

    @Override
    protected void onPostExecute(List<Place> result) {
        Log.d("Deserializer", "Deserialized " + result.size() + " places");
        callback.onTaskFinished(result);
    }
}
