package com.hcare.homeopathy.hcare.NavigationItems;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class SetNavigationHeader {

    private final View headerView;
    private final Context context;

    public SetNavigationHeader(Context context) {
        this.context = context;
        this.headerView = ((NavigationView) ((Activity) context).
                findViewById(R.id.navigationView)).getHeaderView(0);

        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            setFields(dataSnapshot);
                            setImage(Objects.requireNonNull(
                                    dataSnapshot.child("image").getValue()).toString());
                        } catch (Exception ignored) { }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    void setFields(DataSnapshot dataSnapshot) {
        try {

            ((TextView) headerView.findViewById(R.id.username))
                    .setText(Objects.requireNonNull(
                            dataSnapshot.child("name").getValue()).toString());

            ((TextView) headerView.findViewById(R.id.phoneNum))
                    .setText(Objects.requireNonNull(
                            dataSnapshot.child("phone number").getValue()).toString());

         } catch (Exception ignored) { }
    }

    void setImage(String image) {
        if (!image.equals("default")) {
            Picasso.get()
                    .load(image)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.vector_person)
                    .into(headerView.findViewById(R.id.profilePicture), new Callback() {

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image)
                                    .placeholder(R.drawable.vector_person)
                                    .into((ImageView) headerView.findViewById(R.id.profilePicture));
                        }
                    });

            Picasso.get()
                    .load(image)
                    .transform(new BlurTransformation(
                            context, 25, 1))
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(headerView.findViewById(R.id.background), new Callback() {

                        @Override
                        public void onSuccess() {
                            headerView.findViewById(R.id.tint).setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image)
                                    .transform(new BlurTransformation(
                                            context, 25, 1))
                                    .into((ImageView) headerView.findViewById(R.id.background));
                            headerView.findViewById(R.id.tint).setVisibility(View.VISIBLE);
                        }
                    });
        }
    }
}
