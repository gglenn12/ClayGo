package com.himolabs.claygo.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by glennespejo on 9/3/16.
 */
@IgnoreExtraProperties
public class CommunityEvents {
    public String CommunityEventId;
    public String description;
    public String start_date;
    public String end_date;
    public double longitude;
    public double latitude;
    public String event_name;
    public String no_of_participants;

    public CommunityEvents() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public CommunityEvents(String description, String start_date, String end_date, double longitude, double latitude, String event_name) {
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.event_name = event_name;
        this.no_of_participants = no_of_participants;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(Constants.description, description);
        result.put(Constants.event_name, event_name);
        result.put(Constants.start_date_time, start_date);
        result.put(Constants.end_date_time, end_date);
        result.put(Constants.longitude, longitude);
        result.put(Constants.latitude, latitude);
        result.put(Constants.no_of_participants, no_of_participants);

        return result;
    }
}
