package com.example.teachme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText emailInputText, passwordInputText;
    private Button loginBtn, createAccountBtn;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize all IDs
        initializeIDs();

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
}