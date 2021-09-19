package com.example.teachme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText emailInputText, passwordInputText;
    private Button loginBtn, createAccountBtn;
    public static String user_email_id;
    public static String user_key;

    private FirebaseAuth auth;

    public static ArrayList<User> students = new ArrayList<>();
    public static ArrayList<User> teachers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize all IDs
        initializeIDs();

        initializeArrayLists();

        // transparent status bar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        // transparent status bar ends

        auth = FirebaseAuth.getInstance();

        // go to sigup page
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                startActivity(intent, bundle);
            }
        });

        // login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void initializeArrayLists() {

//        ArrayList<User> users = new ArrayList<>();

        // get database instance
        FirebaseDatabase DB = FirebaseDatabase.getInstance();

        // get reference of "users"
        DatabaseReference dRef = DB.getReference().child("users");

        // traverse all users
        dRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String userKey = snapshot.getKey();
//                String username = snapshot.child("name").getValue().toString();
//                Toast.makeText(ShowTeacher.this, userKey + " -> " + username, Toast.LENGTH_SHORT).show();
                String name = snapshot.child("name").getValue().toString();
                if(!name.isEmpty()) {
                    String institution = snapshot.child("institution").getValue().toString();
                    String address = snapshot.child("adress").getValue().toString();
                    String imageUrl = snapshot.child("imageURL").getValue().toString();
                    User user = new User(name, institution, imageUrl, address, userKey);
                    teachers.add(user);
                    Toast.makeText(MainActivity.this, teachers.size()+"", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeIDs() {
        emailInputText = findViewById(R.id.emailInputTextID);
        passwordInputText = findViewById(R.id.passwordInputTextID);
        loginBtn = findViewById(R.id.loginBtnID);
        createAccountBtn = findViewById(R.id.createAccountBtnID);
    }

    private void loginUser() {
        String email = emailInputText.getText().toString().trim();
        String pass = passwordInputText.getText().toString().trim();

        if(email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(MainActivity.this, "Wrong email adress or password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    user_email_id = email;
                    getUserKey();
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                    startActivity(intent, bundle);
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this, "Wrong email adress or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUserKey() {

        // Realtime database testing
        FirebaseDatabase DB = FirebaseDatabase.getInstance();

        // get reference of "users"
        DatabaseReference dRef = DB.getReference().child("users");

        // check user
        Query checkUser = dRef.orderByChild("email").equalTo(user_email_id);

        // do query
        checkUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()) {
                    String str = snapshot.getKey();
                    user_key = str;
                    Toast.makeText(MainActivity.this, user_key, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    // transparent status bar
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    // transparent status bar ends
}