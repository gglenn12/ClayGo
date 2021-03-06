package com.himolabs.claygo;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;
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
import com.himolabs.claygo.Models.UsersPoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements   OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private ArrayList<Restrooms> restrooms = new ArrayList<>();
    private ArrayList<TrashBins> trashBins = new ArrayList<>();
    private ArrayList<DirtyAreas> dirtyAreas = new ArrayList<>();
    private ArrayList<CommunityEvents> communityEvents = new ArrayList<>();
    private ArrayList<UsersPoints> userpoints = new ArrayList<>();
    private DatabaseReference mDatabase;

    private FloatingActionButton createEventFB;

    private GoogleMap mMap;
    Marker currLocationMarker;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
    private static final int MY_PERMISSION_ACCESS_CAMERA = 22;
    private boolean didZoomtoSelf = false;
    private int points = 0;
    private TextView pointsTV;
    private static enum markertype
    {
        bin,
        event,
        mess
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pointsTV = (TextView)findViewById(R.id.pointsTV);
        createEventFB = (FloatingActionButton)findViewById(R.id.create_btn);

        createEventFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(), CreateEventActivity.class);
                i.putExtra("lat", latLng.latitude);
                i.putExtra("long", latLng.longitude);
                startActivity(i);
            }
        });

        //GetData from Firebase
        InitFirebase();
        GetRestrooms(); // will store data to local variable restrooms
        GetTrashBins(); // will store data to local variable trashbins
        GetCommunityEvents(); // will store data to local variable communityEvents
        GetDirtyAreas(); // will store data to local variable dirtyAreas
        GetPoints();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
        // The ACCESS_FINE_LOCATION is denied, then I request it and manage the result in
        // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        int a = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int b = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (a == PackageManager.PERMISSION_GRANTED || b == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String t = marker.getTitle();
                //Intent i = new Intent(getApplication(), GetPoints.class);

                if(t.contains("bin")){
                    String tt  =t.replace("bin", "");
                    int tint = Integer.parseInt(tt);
                    pakganern(tint, markertype.bin);
                }else if(t.contains("mess")) {
                    String tt  =t.replace("mess", "");
                    int tint = Integer.parseInt(tt);
                    pakganern(tint, markertype.mess);
                }else if(t.contains("event")) {
                    String tt  =t.replace("event", "");
                    int tint = Integer.parseInt(tt);
                    pakganern(tint, markertype.event);
                }
                //startActivity(i);
                return false;
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                AddEvent(latLng);
            }
        });
    }

    private void AddEvent(LatLng ltlng){
        Intent i = new Intent(getApplication(), CreateCustomActivity.class);
        i.putExtra("lat", ltlng.latitude);
        i.putExtra("long", ltlng.longitude);
        startActivity(i);
    }
    private void pakganern(int tint, markertype r){
        if(r == markertype.mess){
            PointsDialog a = new PointsDialog();
            DirtyAreas b = dirtyAreas.get(tint);
            a.showMessDialog(this, this, b, userpoints.get(0));
        }else if (r == markertype.bin) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSION_ACCESS_CAMERA);
            } else {
                startActivityForResult(new Intent(getApplicationContext(), GetPoints.class), 1);
            }

        }else if(r == markertype.event){
            PointsDialog a = new PointsDialog();
            CommunityEvents b = communityEvents.get(tint);
            a.showEventDialog(this, this, b, userpoints.get(0));
        }
    }
    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {
        //place marker at current position
        mMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("me");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = mMap.addMarker(markerOptions);

        //Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //zoom to current position:
        if(!didZoomtoSelf) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(19).build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            didZoomtoSelf = true;
        }
        populateStops();
      //  insertMarker(new LatLng((double)14.551050, (double)121.049198), markertype.bin);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Toast.makeText(this,"onConnected", Toast.LENGTH_SHORT).show();
        int a = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int b = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (a == PackageManager.PERMISSION_GRANTED || b == PackageManager.PERMISSION_GRANTED) {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                //place marker at current position
                //mGoogleMap.clear();
                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                currLocationMarker = mMap.addMarker(markerOptions);
            }

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000); //5 seconds
            mLocationRequest.setFastestInterval(3000); //3 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        //Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        //Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    private void insertMarker(LatLng coor, markertype m, int id )
    {
        if(mMap != null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(coor);


            if (m == m.bin) {
                markerOptions.title(id+"bin");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_bin));
            } else if (m == m.event) {
                markerOptions.title(id+"event");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_event));
            } else if (m == m.mess) {
                markerOptions.title(id+"mess");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_mess));
            }

            mMap.addMarker(markerOptions);
        }
    }

    private void populateStops() {
        if(mMap != null){
            for (CommunityEvents c:communityEvents) {
                insertMarker(new LatLng(c.latitude, c.longitude), markertype.event, communityEvents.indexOf(c));
            }
            for (TrashBins t:trashBins) {
                insertMarker(new LatLng(t.latitude, t.longitude), markertype.bin, trashBins.indexOf(t));
            }
            for (DirtyAreas d:dirtyAreas) {
                insertMarker(new LatLng(d.latitude, d.longitude), markertype.mess, dirtyAreas.indexOf(d));
            }
        }

    }

    private void InitFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
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
                populateStops();
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
                communityEvents.clear();
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

                populateStops();
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
                dirtyAreas.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DirtyAreas model = new DirtyAreas();
                    model.DirtyAreaId = snapshot.getKey();
                    model.latitude = snapshot.child(Constants.latitude).getValue(Double.class);
                    model.longitude = snapshot.child(Constants.longitude).getValue(Double.class);
                    model.dirty_area_name = snapshot.child(Constants.dirty_area_name).getValue(String.class);
                    model.description = snapshot.child(Constants.description).getValue(String.class);
                    dirtyAreas.add(model);
                }
                populateStops();
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

    public void GetPoints(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userpoints.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    UsersPoints model = new UsersPoints();
                    model.UsersPointsId = snapshot.getKey();
                    model.last_name = snapshot.child(Constants.lastname).getValue(String.class);
                    model.first_name = snapshot.child(Constants.firstname).getValue(String.class);
                    model.points = snapshot.child(Constants.points).getValue(int.class);
                    userpoints.add(model);

                }
                pointsTV.setText(userpoints.get(0).points+ " pts");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(Constants.users).addValueEventListener(postListener);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "You gained 10 pts!", Toast.LENGTH_SHORT).show();
            SaveToFirebase();
            GetPoints();}
        }
    }

    private void SaveToFirebase() {

        UsersPoints model = new UsersPoints();
        model.first_name = userpoints.get(0).first_name;
        model.last_name = userpoints.get(0).last_name;
        model.points = userpoints.get(0).points+10  ;

        String key = "1";
        Map<String, Object> postValues = model.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(new Intent(getApplicationContext(), GetPoints.class), 1);

                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
