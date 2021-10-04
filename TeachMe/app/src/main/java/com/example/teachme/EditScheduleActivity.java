package com.example.teachme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditScheduleActivity extends AppCompatActivity {

    private String sunday = "false", monday = "false", tuesday = "false", wednesday = "false", thursday = "false", friday = "false", saturday = "false";
    private CheckBox sun, mon, tue, wed, thu, fri, sat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        sun = findViewById(R.id.sundayCheckBoxID);
        mon = findViewById(R.id.mondayCheckBoxID);
        tue = findViewById(R.id.tuesdayCheckBoxID);
        wed = findViewById(R.id.wednesdayCheckBoxID);
        thu = findViewById(R.id.thursdayCheckBoxID);
        fri = findViewById(R.id.fridayCheckBoxID);
        sat = findViewById(R.id.saturdayCheckBoxID);

        update2();

        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update("sunday", sun);
            }
        });
        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update("monday", mon);
            }
        });
        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update("tuesday", tue);
            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update("wednesday", wed);
            }
        });
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update("thursday", thu);
            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update("friday", fri);
            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update("saturday", sat);
            }
        });

    }

    private void update(String day, CheckBox checkBox) {

        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DatabaseReference dRef = DB.getReference().child("schedule").child(MainActivity.user_key);

        if(checkBox.isChecked()) {
            dRef.child(day).setValue("true");
        }
        else {
            dRef.child(day).setValue("false");
        }

    }

    private void update2() {

        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DatabaseReference dRef = DB.getReference().child("schedule").child(MainActivity.user_key);

//        Toast.makeText(ShowScheduleActivity.this, dRef.getKey(), Toast.LENGTH_SHORT).show();

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        String day = dataSnapshot.getKey();
                        String val = dataSnapshot.getValue().toString();
//                        Toast.makeText(ShowScheduleActivity.this, day + " -> " + val, Toast.LENGTH_SHORT).show();
                        if(day.equals("sunday")) {
                            if(val.equals("true")) sun.setChecked(true);
                        }
                        else if(day.equals("monday")) {
                            if(val.equals("true")) mon.setChecked(true);
                        }
                        else if(day.equals("tuesday")) {
                            if(val.equals("true")) tue.setChecked(true);
                        }
                        else if(day.equals("wednesday")) {
                            if(val.equals("true")) wed.setChecked(true);
                        }
                        else if(day.equals("thursday")) {
                            if(val.equals("true")) thu.setChecked(true);
                        }
                        else if(day.equals("friday")) {
                            if(val.equals("true")) fri.setChecked(true);
                        }
                        else if(day.equals("saturday")) {
                            if(val.equals("true")) sat.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}