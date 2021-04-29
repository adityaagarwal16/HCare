package com.hcare.homeopathy.hcare.Disease;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.Checkout.CheckoutActivity;
import com.hcare.homeopathy.hcare.DiseaseInfo;
import com.hcare.homeopathy.hcare.Diseases;
import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

import static com.hcare.homeopathy.hcare.Checkout.Constants.DISEASE_OBJECT;

public class DiseaseActivity extends AppCompatActivity {

    DiseaseInfo disease;
    private String patientName, age, sex;
    private DatabaseReference userRef;
    private EditText mChatMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);
        disease = new DiseaseInfo((Diseases) getIntent().getSerializableExtra("request_type1"));

        setToolbar();

        setContent();

        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        final String current_uid = Objects.requireNonNull(mCurrentUser).getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mChatMessageView = findViewById(R.id.diseases);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                age = Objects.requireNonNull(dataSnapshot.child("age").getValue()).toString();
                sex = Objects.requireNonNull(dataSnapshot.child("sex").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        findViewById(R.id.continueButton).setOnClickListener(v -> {

                if(mChatMessageView.getText().toString().equals(""))
                    Toast.makeText(this, "Please enter your health Issue", Toast.LENGTH_SHORT).show();

                else {
                    try {
                        Intent regIntent = new Intent(DiseaseActivity.this,
                                CheckoutActivity.class);
                        regIntent.putExtra(DISEASE_OBJECT,
                                (Diseases) getIntent().getSerializableExtra("request_type1"));
                        regIntent.putExtra("details1", mChatMessageView.getText().toString());
                        regIntent.putExtra("name", patientName);
                        regIntent.putExtra("age", age);
                        regIntent.putExtra("sex", sex);

                        startActivity(regIntent);
                    }
                    catch (Exception ignored) { }
                }

        });
    }

    private void setContent() {
        ((ImageView) findViewById(R.id.diseaseImage)).setImageResource(disease.getDrawable());
        ((TextView) findViewById(R.id.aboutDisease)).setText(disease.getInfo());
    }

    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((TextView) findViewById(R.id.title)).setText(disease.getDiseaseName());
        findViewById(R.id.howItWorks).setOnClickListener(v -> showHowToUseDialog());
    }

    private void showHowToUseDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_how_it_works);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        dialog.show();
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

