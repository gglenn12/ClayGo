package com.himolabs.claygo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.games.event.Event;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.vision.text.Text;
import com.himolabs.claygo.Models.CommunityEvents;
import com.himolabs.claygo.Models.DirtyAreas;

/**
 * Created by WGI on 03/09/2016.
 */
public class PointsDialog {

    private int points = 0;
    public void showMessDialog(Context e, Object c, DirtyAreas a){
       // MapsActivity cc = (MapsActivity)c;
        final Dialog dialog = new Dialog(e);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dirty_area_dialog);

        TextView name = (TextView) dialog.findViewById(R.id.tv_name);
        name.setText(a.dirty_area_name);
        TextView desc = (TextView) dialog.findViewById(R.id.tv_description);
        desc.setText(a.description);
        Button create = (Button) dialog.findViewById(R.id.btn_create_event);
        Button close = (Button) dialog.findViewById(R.id.btn_close);
        final Context eee = e;

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(eee, CreateEventActivity.class);
                eee.startActivity(i);
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    public void showBinDialog(Context c, String msg){
        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dirty_area_dialog);

        //TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
//        text.setText(msg);

//        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
//        dialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();

    }

    public void showEventDialog(Context e, Object c, CommunityEvents a){
        // MapsActivity cc = (MapsActivity)c;
        final Dialog dialog = new Dialog(e);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dirty_area_dialog);

        TextView name = (TextView) dialog.findViewById(R.id.tv_name);
        name.setText(a.event_name);
        TextView desc = (TextView) dialog.findViewById(R.id.tv_description);
        desc.setText(a.description);
        TextView sched = (TextView) dialog.findViewById(R.id.tv_schedule);
        sched.setText(a.start_date+ a.end_date);
        TextView participants = (TextView) dialog.findViewById(R.id.tv_participants);
        participants.setText(a.no_of_participants);
        Button create = (Button) dialog.findViewById(R.id.btn_create_event);
        Button close = (Button) dialog.findViewById(R.id.btn_close);
        final Context eee = e;

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(eee, CreateEventActivity.class);
                eee.startActivity(i);
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


}
