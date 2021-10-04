package com.example.teachme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ShowScheduleActivity extends AppCompatActivity {

    private String clickedUserKey;
    private TextView sun, mon, tue, wed, thu, fri, sat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_schedule);

        sun = findViewById(R.id.sundayTextID);
        mon = findViewById(R.id.mondayTextID);
        tue = findViewById(R.id.tuesdayTextID);
        wed = findViewById(R.id.wednesdayTextID);
        thu = findViewById(R.id.thursdayTextID);
        fri = findViewById(R.id.fridayTextID);
        sat = findViewById(R.id.saturdayTextID);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            clickedUserKey = extras.getString("clickedUserKey");
        }

        update();

    }

    private void update() {

        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DatabaseReference dRef = DB.getReference().child("schedule").child(clickedUserKey);

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
                            if(val.equals("true")) sun.setText("✅");
                            else sun.setText("❌");
                        }
                        else if(day.equals("monday")) {
                            if(val.equals("true")) mon.setText("✅");
                            else mon.setText("❌");
                        }
                        else if(day.equals("tuesday")) {
                            if(val.equals("true")) tue.setText("✅");
                            else tue.setText("❌");
                        }
                        else if(day.equals("wednesday")) {
                            if(val.equals("true")) wed.setText("✅");
                            else wed.setText("❌");
                        }
                        else if(day.equals("thursday")) {
                            if(val.equals("true")) thu.setText("✅");
                            else thu.setText("❌");
                        }
                        else if(day.equals("friday")) {
                            if(val.equals("true")) fri.setText("✅");
                            else fri.setText("❌");
                        }
                        else if(day.equals("saturday")) {
                            if(val.equals("true")) sat.setText("✅");
                            else sat.setText("❌");
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