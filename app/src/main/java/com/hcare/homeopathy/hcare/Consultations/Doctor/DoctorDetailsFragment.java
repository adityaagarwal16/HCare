package com.hcare.homeopathy.hcare.Consultations.Doctor;

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
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.Objects;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class DoctorDetailsFragment extends Fragment {

    View root;

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
                    setImage(Objects.requireNonNull(
                            dataSnapshot.child("image").getValue()).toString());
                    setFields(dataSnapshot);
                } catch(Exception ignored) { }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    @SuppressLint("SetTextI18n")
    void setFields (DataSnapshot dataSnapshot) {

        ((TextView) root.findViewById(R.id.degree))
                .setText(dataSnapshot.child("qualification").getValue().toString());
        ((TextView) root.findViewById(R.id.aboutTheDoctor))
                .setText(dataSnapshot.child("about yourself").getValue().toString());
        ((TextView) root.findViewById(R.id.registration))
                .setText(dataSnapshot.child("register id").getValue().toString());
        ((TextView) root.findViewById(R.id.date))
                .setText(dataSnapshot.child("experience").getValue().toString());
        ((TextView) root.findViewById(R.id.languages))
                .setText(dataSnapshot.child("languages").getValue().toString());

        if (dataSnapshot.child("Availabilty")
                .getValue().toString().equals("NOT AVAILABLE"))
            ((TextView) root.findViewById(R.id.status)).setText("Not available");

    }

    void setImage(String image) {
        Picasso.get().load(image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.vector_person)
                .into(root.findViewById(R.id.profilePicture),
                        new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(image).placeholder(R.drawable.vector_person)
                                        .into((ImageView) root.findViewById(R.id.profilePicture));
                            }
                        });

        Picasso.get()
                .load(image)
                .transform(new BlurTransformation
                        (requireContext(), 25, 1))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(root.findViewById(R.id.background),
                        new Callback() {

                            @Override
                            public void onSuccess() {
                                root.findViewById(R.id.tint).setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(image)
                                        .transform(new BlurTransformation(
                                                requireContext(), 25, 1))
                                        .into((ImageView) root.findViewById(R.id.background));
                                root.findViewById(R.id.tint).setVisibility(View.VISIBLE);
                            }
                        });
    }
}
