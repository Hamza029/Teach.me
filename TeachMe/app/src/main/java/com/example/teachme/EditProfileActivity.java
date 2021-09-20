package com.example.teachme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import com.example.teachme.MainActivity;

public class EditProfileActivity extends AppCompatActivity {

    private Button selectImageBtn;
    private Button updateProfileBtn;
    private ShapeableImageView profileImg;
    private Uri selectedImageUri;
    private TextInputEditText fullNameText, phoneNumberText, adressText, institutionText, subjectText, academicText;
    private RadioButton maleRadBtn, femaleRadBtn;
    private RadioGroup radGroup;
    private CheckBox tutorCheck, studentCheck;

    private String userKey;
    private String userEmail;
    private String imageUrl = "";

    String gender = ""; // male or female
    private boolean isTutor = false, isStudent = false;
    private String tutor = "", student = "";
//    boolean

    FirebaseStorage storage;
    StorageReference storageReference;

    private final int SELECT_PICTURE = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // set IDs
        selectImageBtn = findViewById(R.id.selectImgBtnID);
        profileImg = findViewById(R.id.profileImgID);
        updateProfileBtn = findViewById(R.id.updateProfileBtnID);
        fullNameText = findViewById(R.id.fullNameTextInputID);
        phoneNumberText = findViewById(R.id.phoneTextInputID);
        adressText = findViewById(R.id.adressTextInputID);
        institutionText = findViewById(R.id.institutionTextInputID);
        subjectText = findViewById(R.id.subjectTextInputID);
        academicText = findViewById(R.id.academicTextInputID);

        maleRadBtn = findViewById(R.id.radioMaleID);
        femaleRadBtn = findViewById(R.id.radioFemaleID);
        radGroup = findViewById(R.id.genderRadioGroupID);

        tutorCheck = findViewById(R.id.teacherCheckBoxID);
        studentCheck = findViewById(R.id.studentCheckBoxID);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        setInformations();

//        userKey = MainActivity.user_key;
//        userEmail = MainActivity.user_email_id;
//        Toast.makeText(EditProfileActivity.this, MainActivity.user_key, Toast.LENGTH_SHORT).show();

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImg();
            }
        });

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
//                Toast.makeText(EditProfileActivity.this, "OK", Toast.LENGTH_SHORT).show();
            }
        });

        maleRadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "male";
            }
        });

        femaleRadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "female";
            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

    }

    private void setInformations() {

//        Toast.makeText(EditProfileActivity.this, "key = " + MainActivity.user_key, Toast.LENGTH_SHORT).show();


        FirebaseDatabase DB = FirebaseDatabase.getInstance();

        // get reference of "users"
        DatabaseReference dRef = DB.getReference().child("users");

        DatabaseReference dRef2 = DB.getReference().child("users").child(MainActivity.user_key);
        dRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {


//                    String username= snapshot.child("username").getValue().toString();
//                    Toast.makeText(EditProfileActivity.this, username, Toast.LENGTH_LONG).show();
//                    emailText.setText(username);

                    String name = snapshot.child("name").getValue().toString();
                    if(!name.isEmpty()) {
                        fullNameText.setText(name);

//                        Toast.makeText(EditProfileActivity.this, name, Toast.LENGTH_SHORT).show();
//                        fullNameText, phoneNumberText, adressText, institutionText, subjectText, academicText;
                        phoneNumberText.setText(snapshot.child("phone").getValue().toString());
                        adressText.setText(snapshot.child("adress").getValue().toString());
                        institutionText.setText(snapshot.child("institution").getValue().toString());
                        subjectText.setText(snapshot.child("subject").getValue().toString());
                        academicText.setText(snapshot.child("academic").getValue().toString());
                        String gender = snapshot.child("userGender").getValue().toString();
                        Toast.makeText(EditProfileActivity.this, gender+"", Toast.LENGTH_SHORT).show();
                        if(gender.equals("male")) {
                            radGroup.check(R.id.radioMaleID);
                        }
                        else {
                            radGroup.check(R.id.radioFemaleID);
                        }
                        String teacherStr = snapshot.child("isTeacher").getValue().toString();
                        String studentStr = snapshot.child("isStudent").getValue().toString();
                        if(teacherStr.equals("true")) {
                            tutorCheck.setChecked(true);
                        }
                        if(studentStr.equals("true")) {
                            studentCheck.setChecked(true);
                        }
                    }
                }
                else {
                    Toast.makeText(EditProfileActivity.this, "not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateProfile() {

        String name = fullNameText.getText().toString().trim();
        String phone = phoneNumberText.getText().toString().trim();
        String address = adressText.getText().toString().trim();
        String institution = institutionText.getText().toString().trim();
        String academic = academicText.getText().toString().trim();
        String subject = subjectText.getText().toString().trim();

        if(tutorCheck.isChecked()) {
            isTutor = true;
            tutor = "true";
//            Toast.makeText(EditProfileActivity.this, "tutor", Toast.LENGTH_SHORT).show();
        }
        else {
            isTutor = false;
            tutor = "false";
        }
        if(studentCheck.isChecked()) {
            isStudent = true;
            student = "true";
        }
        else {
            isStudent = false;
            student = "false";
        }

        if(name.isEmpty() || phone.isEmpty() || address.isEmpty() || institution.isEmpty()
            || academic.isEmpty() || subject.isEmpty() || (!isTutor && !isStudent) || gender.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
            // handle if something is empty
//            return;
        }

        FirebaseDatabase DB = FirebaseDatabase.getInstance();

        // get reference of "users"
        DatabaseReference dRef = DB.getReference().child("users");

        User user = new User(MainActivity.user_email_id, MainActivity.user_key, name, institution, "", phone, address, academic, subject, 0, isTutor, isStudent);
        user.setImageURL(imageUrl);
        user.setUserGender(gender);
        user.setIsTeacher(tutor);
        user.setIsStudent(student);

        dRef.child(MainActivity.user_key).setValue(user);
//        Toast.makeText(EditProfileActivity.this, userKey, Toast.LENGTH_SHORT).show();

    }

    private void chooseImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
//                    profileImg.setImageURI(selectedImageUri);

                    // upload in firebase storage
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                getContentResolver(),
                                selectedImageUri
                        );
                        profileImg.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void uploadImage()
    {
        if (selectedImageUri != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(selectedImageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("download uri", "onSuccess: "+uri.toString()); // check on logcat - debug
                                            imageUrl = uri.toString();
                                        }
                                    });

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(EditProfileActivity.this, "Image uploaded!", Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        // Progress Listener for loading
                        // percentage on the dialog box
                        @Override
                        public void onProgress(
                                UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });
        }
    }
}