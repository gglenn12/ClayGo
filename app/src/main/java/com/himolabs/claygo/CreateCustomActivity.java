package com.himolabs.claygo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.himolabs.claygo.Models.CommunityEvents;
import com.himolabs.claygo.Models.DirtyAreas;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael S on 9/4/2016.
 */
public class CreateCustomActivity extends AppCompatActivity {

    private EditText et_name, et_description;
    private ImageView iv_image;
    private Button btn_save;
    private double lat = 0, lng =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_custom);
        InitFirebase();

        if(getIntent().hasExtra("lat"))
            lat = getIntent().getDoubleExtra("lat", 0);
        if(getIntent().hasExtra("long"))
            lng = getIntent().getDoubleExtra("long", 0);

        et_name = (EditText) findViewById(R.id.et_name);
        et_description = (EditText) findViewById(R.id.et_description);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        btn_save = (Button) findViewById(R.id.btn_save_custom);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveToFirebase();
                finish();
            }
        });
    }

    private DatabaseReference mDatabase;
    private void InitFirebase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }




    private void SaveToFirebase() {

        DirtyAreas model = new DirtyAreas();
        model.dirty_area_name = et_name.getText().toString();
        model.description = et_description.getText().toString();
        model.latitude = lat;
        model.longitude = lng;

        String key = mDatabase.child("dirty_areas").push().getKey();
        Map<String, Object> postValues = model.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/dirty_areas/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
}
