package com.hcare.homeopathy.hcare.Consultations.Doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

import java.util.Objects;

public class DoctorDetailsFragment extends Fragment {

    private ImageView mProfileimage;
    private TextView mName,mDegree,mSpecialization,mRegid,mExperience,mLanguage,status;
    private String name;
    private String Availabilty;

    private DatabaseReference userRef;
    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_docprofile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String doctor_id = getArguments().getString("user_id");

        DatabaseReference mDoctorsDatabase = FirebaseDatabase.getInstance().getReference()
                .child("Doctors").child(Objects.requireNonNull(doctor_id));
        mDoctorsDatabase.keepSynced(true);
        FirebaseUser mCurrent_patient = FirebaseAuth.getInstance().getCurrentUser();
        final String a = Objects.requireNonNull(mCurrent_patient).getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(a);
        mProfileimage = root.findViewById(R.id.docimage);
        mName = root.findViewById(R.id.nameinview);

        mDegree =  root.findViewById(R.id.degreeinview);
        mSpecialization = root.findViewById(R.id.specilazationinview);
        mRegid =  root.findViewById(R.id.registeridinview);
        mExperience =  root.findViewById(R.id.experienceinview);
        mLanguage = root.findViewById(R.id.languageinview);

        status = root.findViewById(R.id.textView38);

        mDoctorsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                    public void onSuccess() { }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(image).placeholder(R.drawable.doctor).into(mProfileimage);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }
}
