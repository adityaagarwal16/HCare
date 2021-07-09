package com.hcare.homeopathy.hcare.Main.Doctors;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.FirebaseClasses.DoctorObject;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.Objects;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class DoctorDetailsFragment extends Fragment {

    View root;
    DoctorObject doctor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_doctor_details, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        final String doctor_id = getArguments().getString("user_id");

        DatabaseReference mDoctorsDatabase = FirebaseDatabase.getInstance().getReference()
                .child("Doctors").child(Objects.requireNonNull(doctor_id));
        mDoctorsDatabase.keepSynced(true);


        mDoctorsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    doctor = dataSnapshot.getValue(DoctorObject.class);
                    setImage();
                    setFields();
                } catch(Exception ignored) { }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    @SuppressLint("SetTextI18n")
    void setFields () {
        ((TextView) root.findViewById(R.id.degree)).setText(doctor.getQualification());
        ((TextView) root.findViewById(R.id.aboutTheDoctor)).setText(doctor.getAboutYourself());
        ((TextView) root.findViewById(R.id.registration)).setText(doctor.getRegisterID());
        ((TextView) root.findViewById(R.id.date)).setText(MessageFormat.format("{0} years",
                        doctor.getExperience()));
        ((TextView) root.findViewById(R.id.languages)).setText(doctor.getLanguages());

        if (doctor.getAvailability().equals("NOT AVAILABLE"))
            ((TextView) root.findViewById(R.id.status)).setText("Not available");

    }

    void setImage() {
        int drawable = R.drawable.vector_doctor_male;
        if(doctor.getSex().equals("Female"))
            drawable = R.drawable.vector_doctor_female;

        int finalDrawable = drawable;
        Picasso.get().load(doctor.getImage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(drawable)
                .into(root.findViewById(R.id.profilePicture),
                        new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                ((ImageView) root.findViewById(R.id.profilePicture))
                                        .setImageResource(finalDrawable);
                            }
                        });

        Picasso.get()
                .load(doctor.getImage())
                .transform(new BlurTransformation
                        (requireContext(), 25, 1))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(root.findViewById(R.id.background),
                        new Callback() {

                            @Override
                            public void onSuccess() { }

                            @Override
                            public void onError(Exception e) {
                                ((ImageView) root.findViewById(R.id.background))
                                        .setImageResource(finalDrawable);
                            }
                        });
    }
}
