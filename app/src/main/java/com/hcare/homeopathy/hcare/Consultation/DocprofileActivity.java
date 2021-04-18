package com.hcare.homeopathy.hcare.Consultation;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DocprofileActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView mProfileimage;
    private TextView mName,mDegree,mSpecialization,mRegid,mExperience,mLanguage,status;


    private DatabaseReference mDoctorsDatabase;


    private String mcurrent_state, name,patientname,sex,age,Availabilty,bname;

    private DatabaseReference mConsultReqDatabase,userRef;

    private FirebaseUser mCurrent_patient;
    private int count,check =3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docprofile);

        final String doctor_id = getIntent().getStringExtra("user_id");

        setTitle("About Doctor");

        mDoctorsDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors").child(doctor_id);
        mDoctorsDatabase.keepSynced(true);
        mCurrent_patient = FirebaseAuth.getInstance().getCurrentUser();
        final String a = mCurrent_patient.getUid();
        mcurrent_state ="not connected";
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(a);
        mProfileimage =(ImageView) findViewById(R.id.docimage);
        mName =(TextView) findViewById(R.id.nameinview);

        mDegree = (TextView) findViewById(R.id.degreeinview);
        mSpecialization =(TextView) findViewById(R.id.specilazationinview);
        mRegid = (TextView) findViewById(R.id.registeridinview);
        mExperience = (TextView) findViewById(R.id.experienceinview);
        mLanguage =(TextView) findViewById(R.id.languageinview);

        status =(TextView) findViewById(R.id.textView38);

        mcurrent_state ="not connected";


        mDoctorsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String image = dataSnapshot.child("image").getValue().toString();
                name = dataSnapshot.child("name").getValue().toString();
                String degree = dataSnapshot.child("qualification").getValue().toString();
                String specialization = dataSnapshot.child("about yourself").getValue().toString();
                String Regid = dataSnapshot.child("register id").getValue().toString();
                String Experience = dataSnapshot.child("experience").getValue().toString();
                String language = dataSnapshot.child("languages").getValue().toString();
                Availabilty = dataSnapshot.child("Availabilty").getValue().toString();

                mName.setText(name);
                mDegree.setText(degree);
                mSpecialization.setText(specialization);
                mRegid.setText(Regid);
                mExperience.setText(Experience);
                mLanguage.setText(language);
                if (Availabilty.equals("NOT AVAILABLE")) {


                    status.setText("Not available");
                }else {
                    status.setText("Available");

                }
                Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.doctor).into(mProfileimage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(image).placeholder(R.drawable.doctor).into(mProfileimage);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
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
