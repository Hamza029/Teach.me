package com.example.teachme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout emailInputTextLayout, passwordInputTextLayout;
    private TextInputEditText emailInputText, passwordInputText;
    private Button signUpBtn;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeIDs();

        auth = FirebaseAuth.getInstance();

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
        if(!passError) {
            passwordInputTextLayout.setError(null);
        }
        if(emailError || passError) {
            return;
        }

        // register user
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    FirebaseDatabase DB = FirebaseDatabase.getInstance();

                    // get reference of "users"
                    DatabaseReference dRef = DB.getReference().child("users");

                    String userKey = dRef.push().getKey();

                    User user = new User(email, userKey, "", "", "", "", "", "", "", 0, false, false);

                    user.setImageURL("");
                    user.setIsStudent("");
                    user.setIsTeacher("");
                    user.setUserGender("");

                    dRef.child(userKey).setValue(user);

                    DatabaseReference dRefLikes = DB.getReference().child("likes").child(userKey).child("testID");
                    dRefLikes.setValue("false");

                    DatabaseReference dRefS = DB.getReference().child("schedule").child(userKey);
//                    WeekDays days = new WeekDays("false", "false", "false", "false", "false", "false", "false");
                    dRefS.child("sunday").setValue("false");
                    dRefS.child("monday").setValue("false");
                    dRefS.child("tuesday").setValue("false");
                    dRefS.child("wednesday").setValue("false");
                    dRefS.child("thursday").setValue("false");
                    dRefS.child("friday").setValue("false");
                    dRefS.child("saturday").setValue("false");

                    Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}