package com.capiyoo.screwus;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity {

    Spinner spinner;
    ArrayList<String> arrayList;
    EditText reviewEditText;
    RatingBar ratingBar;
    RadioButton radioButton1;
    RadioButton radioButton2;
    Button submit;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    DatabaseReference userDatabaseReference;
    String fromValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        spinner = findViewById(R.id.spinner);
        reviewEditText = findViewById(R.id.write_review);
        ratingBar = findViewById(R.id.rating);
        firebaseAuth = FirebaseAuth.getInstance();
        radioButton1 = findViewById(R.id.reveal);
        radioButton2 = findViewById(R.id.anonymous);
        submit = findViewById(R.id.submit);
        arrayList = new ArrayList<>();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("UserDatabase").child(firebaseAuth.getCurrentUser().getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Review").child(firebaseAuth.getCurrentUser().getUid());
        arrayList.add(0, "Select");
        arrayList.add("HR");
        arrayList.add("Manager");
        arrayList.add("Food");
        arrayList.add("Admin");
        arrayList.add("Security");
        arrayList.add("Any Other");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        spinner.setAdapter(arrayAdapter);

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(FeedbackActivity.this);
                return false;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float ratingBarValue = ratingBar.getRating();
                String area = spinner.getSelectedItem().toString();
                String _review = reviewEditText.getText().toString();
                if (!TextUtils.isEmpty(_review)) {
                    submitReview(ratingBarValue, area, _review);
                } else {
                        Toast.makeText(getApplicationContext(),"Please Write Something",Toast.LENGTH_LONG).show();
                }

            }
        });


        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null)
                {
                    String FirstNAme=dataSnapshot.child("FIRSTNAME").getValue(String.class);
                    String LastName=dataSnapshot.child("LASTNAME").getValue(String.class);

                    fromValue=FirstNAme+" "+LastName;
                    Toast.makeText(getApplicationContext(), fromValue, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    void submitReview(Float ratingValue, String area, String review) {

        Reviews reviews = new Reviews();
        reviews.setmReview(review);
        reviews.setmArea(area);

        if (radioButton1.isChecked()) {


        } else if (radioButton2.isChecked()) {
            fromValue = "Anonymous";
        }
        reviews.setmFrom(fromValue);
        reviews.setmRating(ratingValue);
        databaseReference.push().setValue(reviews).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Submitted review successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(FeedbackActivity.this,MainActivity.class));
                }

            }
        });

    }


}
