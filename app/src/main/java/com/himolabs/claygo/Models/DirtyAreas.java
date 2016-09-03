package com.himolabs.claygo.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glennespejo on 9/3/16.
 */
@IgnoreExtraProperties
public class DirtyAreas {
    public String DirtyAreaId;
    public String description;
    public double longitude;
    public double latitude;
    public String dirty_area_name;

    public DirtyAreas() {
    }

    public DirtyAreas(String description, double longitude, double latitude, String dirty_area_name) {
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dirty_area_name = dirty_area_name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.description, description);
        result.put(Constants.longitude, longitude);
        result.put(Constants.latitude, latitude);
        result.put(Constants.dirty_area_name, dirty_area_name);

        return result;
    }
}
