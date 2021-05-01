package com.hcare.homeopathy.hcare.Disease;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.Checkout.CheckoutActivity;
import com.hcare.homeopathy.hcare.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DiseaseSpinnerActivity extends AppCompatActivity {

    String[] spinnerTitles;
    int[] spinnerImages;
    private String mName, patientname, age, sex,message;
    private DatabaseReference userRef;
    private EditText mChatMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultbtn);

        setTitle("Consult a doctor");

        spinnerTitles = new String[]
                {"Select a Problem Area", "Hair", "Skin", "Weight Loss & gain",
                "Headache", "Renal Problems", "Respiratory", "Male", "Female","Digestive Problems",
                "Diabetes & HyperTension", "Thyroid","Ear, Nose & Throat","Mouth & Teeth","Growth",
                "Bones & joints","Nutrition & health","Eye", "Heart Problems","Children",
                "Mental Health","Other"};

        spinnerImages = new int[] {
                R.drawable.newothers,
                R.drawable.newhair,
                R.drawable.newacne,
                R.drawable.newweight,
                R.drawable.newheadache,
                R.drawable.newrenal,
                R.drawable.newrespiratory,
                R.drawable.newmens , R.drawable.newwomen,
                R.drawable.newdigestive, R.drawable.newdiabetes,
                R.drawable.newthyroid, R.drawable.newent,
                R.drawable.newmouth, R.drawable.newgrowth,
                R.drawable.newjoints, R.drawable.newnutrition,
                R.drawable.neweye, R.drawable.newheart,
                R.drawable.newchild, R.drawable.newdepression,
                R.drawable.newothers};

        Spinner mSpinner = findViewById(R.id.spinner);
        CustomAdapter mCustomAdapter = new CustomAdapter
                (DiseaseSpinnerActivity.this, spinnerTitles, spinnerImages);
        mSpinner.setAdapter(mCustomAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                mName = spinnerTitles[i];
                Toast.makeText(DiseaseSpinnerActivity.this, spinnerTitles[i], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }

        });

        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        final String current_uid = Objects.requireNonNull(mCurrentUser).getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mChatMessageView = (EditText) findViewById(R.id.diseases);
        Button mConsultingbtn = (Button) findViewById(R.id.consult_btn);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientname = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                age = Objects.requireNonNull(dataSnapshot.child("age").getValue()).toString();
                sex = Objects.requireNonNull(dataSnapshot.child("sex").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


            mConsultingbtn.setOnClickListener(v -> {
                message = mChatMessageView.getText().toString();
                if (!mName.equals("Select a Problem Area")) {
                    Intent regIntent = new Intent(this, CheckoutActivity.class);
                    regIntent.putExtra("details1", message);
                    regIntent.putExtra("request_type1", mName);
                    regIntent.putExtra("name", patientname);
                    regIntent.putExtra("age", age);
                    regIntent.putExtra("sex", sex);
                    startActivity(regIntent);

                } else
                    Toast.makeText(this, "Please select Health Issue", Toast.LENGTH_LONG).show();
            });

    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.child("status").setValue("online");
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.child("status").setValue("offline");
    }

}

