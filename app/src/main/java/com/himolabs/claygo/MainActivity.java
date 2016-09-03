package com.himolabs.claygo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.BatchUpdateException;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himolabs.claygo.Models.CommunityEvents;
import com.himolabs.claygo.Models.Constants;
import com.himolabs.claygo.Models.DirtyAreas;
import com.himolabs.claygo.Models.Restrooms;
import com.himolabs.claygo.Models.TrashBins;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Restrooms> restrooms = new ArrayList<>();
    private ArrayList<TrashBins> trashBins = new ArrayList<>();
    private ArrayList<DirtyAreas> dirtyAreas = new ArrayList<>();
    private ArrayList<CommunityEvents> communityEvents = new ArrayList<>();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enterButton = (Button)findViewById(R.id.enterButton);


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(), MapsActivity.class);
                startActivity(i);
                finish();
            }
        });

    }



    private void InitFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void GetRestrooms() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Restrooms model = new Restrooms();
                    model.RestroomId = snapshot.getKey();
                    model.latitude = snapshot.child(Constants.latitude).getValue(Double.class);
                    model.longitude = snapshot.child(Constants.longitude).getValue(Double.class);
                    model.restroom_name = snapshot.child(Constants.restroom_name).getValue(String.class);
                    model.description = snapshot.child(Constants.description).getValue(String.class);
                    restrooms.add(model);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child(Constants.restrooms).addValueEventListener(postListener);

    }

    public void GetTrashBins() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TrashBins model = new TrashBins();
                    model.TrashBinId = snapshot.getKey();
                    model.latitude = snapshot.child(Constants.latitude).getValue(Double.class);
                    model.longitude = snapshot.child(Constants.longitude).getValue(Double.class);
                    model.trash_bin_name = snapshot.child(Constants.trash_bin_name).getValue(String.class);
                    model.description = snapshot.child(Constants.description).getValue(String.class);
                    trashBins.add(model);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child(Constants.trashbins).addValueEventListener(postListener);

    }

    public void GetCommunityEvents() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CommunityEvents model = new CommunityEvents();
                    model.CommunityEventId = snapshot.getKey();
                    model.latitude = snapshot.child(Constants.latitude).getValue(Double.class);
                    model.longitude = snapshot.child(Constants.longitude).getValue(Double.class);
                    model.event_name = snapshot.child(Constants.event_name).getValue(String.class);
                    model.description = snapshot.child(Constants.description).getValue(String.class);
                    model.no_of_participants = snapshot.child(Constants.no_of_participants).getValue(String.class);
                    communityEvents.add(model);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child(Constants.communityevents).addValueEventListener(postListener);

    }

    public void GetDirtyAreas() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DirtyAreas model = new DirtyAreas();
                    model.DirtyAreaId = snapshot.getKey();
                    model.latitude = snapshot.child(Constants.latitude).getValue(Double.class);
                    model.longitude = snapshot.child(Constants.longitude).getValue(Double.class);
                    model.dirty_area_name = snapshot.child(Constants.dirty_area_name).getValue(String.class);
                    model.description = snapshot.child(Constants.description).getValue(String.class);
                    dirtyAreas.add(model);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child(Constants.dirtyareas).addValueEventListener(postListener);

    }
}
