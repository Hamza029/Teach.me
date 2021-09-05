package com.example.teachme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText emailInputText, passwordInputText;
    private Button loginBtn, createAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize all IDs
        initializeIDs();

        // go to sigup page
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeIDs() {
        emailInputText = findViewById(R.id.emailInputTextID);
        passwordInputText = findViewById(R.id.passwordInputTextID);
        loginBtn = findViewById(R.id.loginBtnID);
        createAccountBtn = findViewById(R.id.createAccountBtnID);
    }
}