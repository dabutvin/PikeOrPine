package com.dabutvin.pikeorpine;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 3/1/2017.
 */
public class Place {
    private String name;
    private String street;
    private String image;

    public static Place ToPlace(JSONObject jsonObject){
        Place place = new Place();

        try{
            place.setName(jsonObject.getString("name"));
            place.setStreet(jsonObject.getString("street"));
            place.setImage(jsonObject.getString("image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
