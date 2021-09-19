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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private EditText fullNameText, phoneNumberText, adressText, institutionText, subjectText, academicText;
    private RadioButton maleRadBtn, femaleRadBtn;
    private RadioGroup radGroup;
    private CheckBox tutorCheck, studentCheck;

    private String userKey;
    private String userEmail;
    private String imageUrl;

    int gender = 0; // 1-male, 2-female
    boolean isTutor = false, isStudent = false;
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
                gender = 1;
            }
        });

        femaleRadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = 2;
            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
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
//            Toast.makeText(EditProfileActivity.this, "tutor", Toast.LENGTH_SHORT).show();
        }
        else {
            isTutor = false;
        }
        if(studentCheck.isChecked()) {
            isStudent = true;
        }
        else {
            isStudent = false;
        }

        if(name.isEmpty() || phone.isEmpty() || address.isEmpty() || institution.isEmpty()
            || academic.isEmpty() || subject.isEmpty() || (!isTutor && !isStudent) || gender == 0) {
            Toast.makeText(EditProfileActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase DB = FirebaseDatabase.getInstance();

        // get reference of "users"
        DatabaseReference dRef = DB.getReference().child("users");

        User user = new User(MainActivity.user_email_id, MainActivity.user_key, name, institution, "", phone, address, academic, subject, gender, isTutor, isStudent);
        user.setImageURL(imageUrl);

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