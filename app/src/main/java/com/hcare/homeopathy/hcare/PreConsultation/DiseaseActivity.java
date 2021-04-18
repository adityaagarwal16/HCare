package com.hcare.homeopathy.hcare.PreConsultation;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.gson.Gson;
import com.hcare.homeopathy.hcare.Mainmenus.DiseaseObject;
import com.hcare.homeopathy.hcare.R;

public class DiseaseActivity extends AppCompatActivity {

    DiseaseObject object;
    private String patientName, age, sex;
    private DatabaseReference userRef;
    private EditText mChatMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwithdoctors);

        Gson gson = new Gson();
        object = gson.fromJson(getIntent().getStringExtra("request_type1"), DiseaseObject.class);

        setToolbar();

        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        final String current_uid = mCurrentUser.getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mChatMessageView = (EditText) findViewById(R.id.diseases);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientName = dataSnapshot.child("name").getValue().toString();
                age = dataSnapshot.child("age").getValue().toString();
                sex = dataSnapshot.child("sex").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        findViewById(R.id.consult_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent regIntent = new Intent(DiseaseActivity.this, consultationfeeActivity.class);
                    regIntent.putExtra("details1", mChatMessageView.getText().toString());
                    regIntent.putExtra("request_type1", object.getDiseaseName());
                    regIntent.putExtra("name", patientName);
                    regIntent.putExtra("age", age);
                    regIntent.putExtra("sex", sex);

                    startActivity(regIntent);
                } catch (Exception ignored) { }
            }
        });
    }

    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((TextView) findViewById(R.id.title)).setText(object.getDiseaseName());
        findViewById(R.id.howItWorks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHowToUseDialog();
            }
        });
    }

    private void showHowToUseDialog() {
        final Dialog dialog = new Dialog(this);


        dialog.setContentView(R.layout.dialog_how_it_works);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
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

