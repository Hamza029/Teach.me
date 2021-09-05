package com.example.teachme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout emailInputTextLayout, passwordInputTextLayout;
    private TextInputEditText emailInputText, passwordInputText;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeIDs();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegistration();
            }
        });
    }

    private void initializeIDs() {
        emailInputTextLayout = findViewById(R.id.emailInputTextLayoutID);
        passwordInputTextLayout = findViewById(R.id.passwordInputTextLayoutID);
        emailInputText = findViewById(R.id.emailInputTextID);
        passwordInputText = findViewById(R.id.passwordInputTextID);
        signUpBtn = findViewById(R.id.signupBtnID);
    }

    private void userRegistration() {
        String email = emailInputText.getText().toString().trim();
        String pass = passwordInputText.getText().toString().trim();

        boolean emailError = false;
        boolean passError = false;

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputTextLayout.setError("Enter a valid email adress!");
            emailError = true;
//            return;
        }
        if(pass.length()<6) {
            passwordInputTextLayout.setError("Must be minimum 6 characters long");
            passError = true;
//            return;
        }

        if(!emailError) {
            emailInputTextLayout.setError(null);
        }
        else {
            return;
        }
        if(!passError) {
            passwordInputTextLayout.setError(null);
        }
        else {
            return;
        }
    }
}