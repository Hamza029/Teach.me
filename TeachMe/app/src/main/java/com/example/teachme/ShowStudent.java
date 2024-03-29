package com.example.teachme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowStudent extends AppCompatActivity {

    private RecyclerView userRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);


        userRecView = findViewById(R.id.userRV);
        userRecView.setHasFixedSize(true);
        userRecView.setLayoutManager(new LinearLayoutManager(this));


        ArrayList<User> users = new ArrayList<>();

        // set adapter
        UserRecViewAdapter adapter = new UserRecViewAdapter(this);
//        Toast.makeText(ShowTeacher.this, users.size()+"", Toast.LENGTH_SHORT).show();
        adapter.setUsers(users);
        userRecView.setAdapter(adapter);

        // get database instance
        FirebaseDatabase DB = FirebaseDatabase.getInstance();

        // get reference of "users"
        DatabaseReference dRef = DB.getReference().child("users");

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    String userKey = dataSnapshot.getKey();
                    String name = dataSnapshot.child("name").getValue().toString();
                    String role = dataSnapshot.child("isStudent").getValue().toString();
//                String username = snapshot.child("name").getValue().toString();
//                    Toast.makeText(ShowTeacher.this, userKey + " ---> " + name, Toast.LENGTH_SHORT).show();
                    if(!name.isEmpty() && role.equals("true")) {
                        String institution = dataSnapshot.child("institution").getValue().toString();
                        String address = dataSnapshot.child("adress").getValue().toString();
                        String imageUrl = dataSnapshot.child("imageURL").getValue().toString();
                        User user = new User(name, institution, imageUrl, address, userKey);
//                        User user = new User(name, institution, "", address, userKey);
                        users.add(user);
//                        Toast.makeText(ShowTeacher.this, users.size()+"", Toast.LENGTH_SHORT).show();
                    }
                }
//
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}