package com.capiyoo.screwus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText mobile;
    EditText email;
    EditText passwd;
    EditText cnfPasswd;
    Button signupBtn;
    CircleImageView circleImageView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private StorageReference mStorageRef;
    private static final String USER_DATABASE_REFERENCE = "UserDatabase";
    private static final int RESULT_LOAD_IMG=101;
    ProgressDialog progressDialog;
    String profilepicurl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.activity_signup);
        circleImageView = findViewById(R.id.profileImage);
        firstName = findViewById(R.id.userName);
        lastName = findViewById(R.id.userLastName);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        passwd = findViewById(R.id.password);
        cnfPasswd = findViewById(R.id.cnfPassword);
        signupBtn = findViewById(R.id.signup);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(USER_DATABASE_REFERENCE);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    //  startActivity(new Intent(SignupActivity.this, MainActivity.class));
                } else {


                }
            }
        };

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftKeyboard(SignupActivity.this);
                return false;
            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _email = email.getText().toString().trim();
                String _password = passwd.getText().toString().trim();
                String _cnfPassword = cnfPasswd.getText().toString().trim();
                String _mobile = mobile.getText().toString().trim();
                String _firstName = firstName.getText().toString().trim();
                String _laseName = lastName.getText().toString().trim();

                if (TextUtils.isEmpty(_cnfPassword) || TextUtils.isEmpty(_email) || TextUtils.isEmpty(_password) || TextUtils.isEmpty(_mobile) || TextUtils.isEmpty(_firstName) || TextUtils.isEmpty(_laseName)) {
                    Toast.makeText(getApplicationContext(), "Fields are empty, Kindly Check", Toast.LENGTH_LONG).show();
                    return;
                } else {

                    if (_password.equals(_cnfPassword)) {
                        if(_password.length()>6&&_cnfPassword.length()>6)
                        signup(_email, _password, _cnfPassword, _firstName, _laseName, _mobile);
                        else
                            Toast.makeText(getApplicationContext(),"Please make Stronger Password, Min 6 character",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match, Please Check", Toast.LENGTH_LONG).show();

                    }


                }

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

    }

    void signup(final String email, String password, String ConfirmPassword, final String firstName, final String lastName, final String MobileNumber) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Map<String, String> map = new HashMap<>();
                    map.put("FIRSTNAME", firstName);
                    map.put("LASTNAME", lastName);
                    map.put("MOBILE", MobileNumber);
                    map.put("EMAIL", email);
                    map.put("PROFILEURL", profilepicurl);
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_LONG).show();

                    databaseReference.child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            }
                        }
                    });


                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                progressDialog.show();
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                StorageReference storageReference=mStorageRef.child("Photos").child(imageUri.getLastPathSegment());
                storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       profilepicurl= taskSnapshot.getDownloadUrl().toString();

                        circleImageView.setImageBitmap(selectedImage);
                        progressDialog.hide();

                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(SignupActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(SignupActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}
