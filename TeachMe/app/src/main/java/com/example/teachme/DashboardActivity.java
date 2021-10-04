package com.example.teachme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton logoutBtn;
    private CardView editProfileCard, teacherCardID, studentCard, editScheduleCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        logoutBtn = findViewById(R.id.logoutBtnID);
        editProfileCard = findViewById(R.id.editProfileCardID);
        teacherCardID = findViewById(R.id.teachersCardID);
        studentCard = findViewById(R.id.studentsCardID);
        editScheduleCard = findViewById(R.id.editScheduleCardID);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        editProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(DashboardActivity.this).toBundle();
                startActivity(intent, bundle);
            }
        });

        teacherCardID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ShowTeacher.class);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(DashboardActivity.this).toBundle();
                startActivity(intent, bundle);
            }
        });

        studentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ShowStudent.class);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(DashboardActivity.this).toBundle();
                startActivity(intent, bundle);
            }
        });

        editScheduleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EditScheduleActivity.class);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(DashboardActivity.this).toBundle();
                startActivity(intent, bundle);
            }
        });

    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(DashboardActivity.this).toBundle();
        startActivity(intent, bundle);
        finish();
    }
}