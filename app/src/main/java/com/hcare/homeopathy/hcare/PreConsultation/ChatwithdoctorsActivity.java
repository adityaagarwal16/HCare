package com.hcare.homeopathy.hcare.PreConsultation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChatwithdoctorsActivity extends AppCompatActivity {

    private String mName, patientname, age, sex, message;
    private DatabaseReference userRef;
    private EditText mChatMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwithdoctors);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("consult a Doctor");
        mName = getIntent().getStringExtra("request_type1");

        TextView healthissue = (TextView) findViewById(R.id.healthissuecat);
        healthissue.setText(mName);
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        final String current_uid = mCurrentUser.getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mChatMessageView = (EditText) findViewById(R.id.diseases);
        Button mConsultingbtn = (Button) findViewById(R.id.consult_btn);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientname = dataSnapshot.child("name").getValue().toString();
                age = dataSnapshot.child("age").getValue().toString();
                sex = dataSnapshot.child("sex").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mConsultingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = mChatMessageView.getText().toString();
                if (mName != "Select a Problem Area") {
                    String date = DateFormat.getDateTimeInstance().format(new Date());
                    DateFormat dateFormat = new SimpleDateFormat("HH");
                    Date date2 = new Date();
                    String time = dateFormat.format(date2);
                    // mConsultingbtn.setEnabled(false);
                    Intent regIntent = new Intent(ChatwithdoctorsActivity.this, consultationfeeActivity.class);
                    regIntent.putExtra("details1", message);
                    regIntent.putExtra("request_type1", mName);
                    regIntent.putExtra("name", patientname);
                    regIntent.putExtra("age", age);
                    regIntent.putExtra("sex", sex);
                    startActivity(regIntent);

                } else {
                    Toast.makeText(ChatwithdoctorsActivity.this, "Please select Health Issue", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.child("status").setValue("online");
    }

    private void getdata() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.child("status").setValue("offline");
    }
}

