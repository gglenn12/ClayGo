package com.himolabs.claygo.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glennespejo on 9/3/16.
 */
@IgnoreExtraProperties
public class TrashBins {
    public String TrashBinId;
    public String description;
    public double longitude;
    public double latitude;
    public String trash_bin_name;

    public TrashBins() {
    }

    public TrashBins(String description, double longitude, double latitude, String trash_bin_name) {
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.trash_bin_name = trash_bin_name;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.description, description);
        result.put(Constants.longitude, longitude);
        result.put(Constants.latitude, latitude);
        result.put(Constants.trash_bin_name, trash_bin_name);

        return result;
    }
}
