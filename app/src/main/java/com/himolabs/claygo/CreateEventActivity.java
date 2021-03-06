package com.himolabs.claygo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.himolabs.claygo.Models.CommunityEvents;

import java.util.HashMap;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {

    private EditText et_name, et_description, et_startdatetime, et_enddatetime, et_noofparticipants;
    private Button btn_save;
    private ImageView iv_image;
    private double lat = 0, lng =0;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        InitFirebase();
        if(getIntent().hasExtra("lat"))
            lat = getIntent().getDoubleExtra("lat", 0);
        if(getIntent().hasExtra("long"))
            lng = getIntent().getDoubleExtra("long", 0);


        if(getIntent().hasExtra("messid"))
            id = getIntent().getStringExtra("messid");
        else
            id = "";

        et_name = (EditText) findViewById(R.id.et_name);
        et_description = (EditText) findViewById(R.id.et_description);
        et_startdatetime = (EditText) findViewById(R.id.et_startDateTime);
        et_enddatetime = (EditText) findViewById(R.id.et_endDateTime);
        et_noofparticipants = (EditText) findViewById(R.id.et_noOfParticipants);
        btn_save = (Button) findViewById(R.id.btn_save);
        iv_image = (ImageView) findViewById(R.id.iv_image);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveToFirebase();
                if(!id.isEmpty())
                    deletemess();
            }
        });
    }


    private DatabaseReference mDatabase;
    private void InitFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void SaveToFirebase() {

        CommunityEvents model = new CommunityEvents();
        model.event_name = et_name.getText().toString();
        model.description = et_description.getText().toString();
        model.start_date = et_startdatetime.getText().toString();
        model.end_date = et_enddatetime.getText().toString();
        model.latitude = lat;
        model.longitude = lng;
        model.no_of_participants = et_noofparticipants.getText().toString();

        String key = mDatabase.child("community_events").push().getKey();
        Map<String, Object> postValues = model.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/community_events/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        finish();
    }

    private void deletemess()
    {
        mDatabase.child("dirty_areas").child(id).removeValue();
    }
}
