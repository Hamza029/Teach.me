package com.example.teachme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class ShowProfileActivity extends AppCompatActivity {

    private ImageView likeUserIcon;
    private TextView totalLikes, nameText, institutionText, emailText, phoneText, boroNamText;
    private ImageView imgV;

    String clickedUserKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        likeUserIcon = findViewById(R.id.likeUserID);
        totalLikes = findViewById(R.id.totalLikesID);
        nameText = findViewById(R.id.nameID);
        institutionText = findViewById(R.id.institutionID);
        emailText = findViewById(R.id.emailID);
        phoneText = findViewById(R.id.phoneID);
        imgV = findViewById(R.id.imgID);
        boroNamText = findViewById(R.id.boroNamID);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            clickedUserKey = extras.getString("clickedUserKey");
        }

        countAndSetTotalLikes();
        setSingleLike(false);
        setUserInfo();

        likeUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ShowProfileActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                setSingleLike(true);
                countAndSetTotalLikes();
//                DrawableCompat.setTint(likeUserIcon.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.like_blue));
            }
        });
    }

    private void setUserInfo() {

        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DatabaseReference dRef = DB.getReference().child("users").child(clickedUserKey);

        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    boroNamText.setText(snapshot.child("name").getValue().toString());
                    nameText.setText(snapshot.child("name").getValue().toString());
                    emailText.setText(snapshot.child("email").getValue().toString());
                    institutionText.setText(snapshot.child("institution").getValue().toString());
                    phoneText.setText(snapshot.child("phone").getValue().toString());

//                    Toast.makeText(ShowProfileActivity.this, snapshot.child("imageURL").getValue().toString(), Toast.LENGTH_SHORT).show();

                    Glide.with(ShowProfileActivity.this)
                            .load(snapshot.child("imageURL").getValue().toString())
                            .override(200, 200)
                            .centerCrop()
                            .into(imgV);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    void countAndSetTotalLikes() {

        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DatabaseReference dRef = DB.getReference().child("likes").child(clickedUserKey);

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    totalLikes.setText(String.valueOf(snapshot.getChildrenCount()-1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void setSingleLike(boolean change) {

        FirebaseDatabase DB = FirebaseDatabase.getInstance();
        DatabaseReference dRef = DB.getReference().child("likes").child(clickedUserKey);

        dRef.child(MainActivity.user_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(change) dRef.child(MainActivity.user_key).removeValue();
                    if(change) DrawableCompat.setTint(likeUserIcon.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.gray));
                    else DrawableCompat.setTint(likeUserIcon.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.like_blue));
                }
                else {
                    if(change) dRef.child(MainActivity.user_key).setValue("true");
                    if(change) DrawableCompat.setTint(likeUserIcon.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.like_blue));
                    else DrawableCompat.setTint(likeUserIcon.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.gray));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}