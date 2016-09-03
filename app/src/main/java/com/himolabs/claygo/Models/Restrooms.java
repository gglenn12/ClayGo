package com.himolabs.claygo.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glennespejo on 9/3/16.
 */
@IgnoreExtraProperties
public class Restrooms {
    public String RestroomId;
    public String description;
    public double longitude;
    public double latitude;
    public String restroom_name;

    public Restrooms() {
    }

    public Restrooms(String description, double longitude, double latitude, String restroom_name) {
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.restroom_name = restroom_name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.description, description);
        result.put(Constants.longitude, longitude);
        result.put(Constants.latitude, latitude);
        result.put(Constants.restroom_name, restroom_name);

        return result;
    }
}
