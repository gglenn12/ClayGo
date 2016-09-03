package com.himolabs.claygo.Models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WGI on 03/09/2016.
 */
public class UsersPoints {

    public String UsersPointsId;
    public String last_name;
    public String first_name;
    public int points;

    public UsersPoints() {
    }

    public UsersPoints(String lastname, String firstname, int points) {
        this.last_name = lastname;
        this.first_name = firstname;
        this.points = points;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.lastname, last_name);
        result.put(Constants.firstname, first_name);
        result.put(Constants.points, points);

        return result;
    }
}
