package com.hcare.homeopathy.hcare.Consultations;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

public class AllChatsActivity extends BaseActivity {

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        setRecycler();
    }

    void setRecycler() {
        try {
            DatabaseReference mDoctorsDatabase = FirebaseDatabase.getInstance()
                    .getReference().child("Private_consult").child(userID);

            mDoctorsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        findViewById(R.id.nothingHereLayout).setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            RecyclerView mDoctorList = findViewById(R.id.recycler);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            mDoctorList.setLayoutManager(linearLayoutManager);
            mDoctorsDatabase.keepSynced(true);

            //list
            FirebaseRecyclerOptions<AllChatsObject> options =
                    new FirebaseRecyclerOptions.Builder<AllChatsObject>()
                            .setQuery(mDoctorsDatabase.orderByChild("time"),
                                    AllChatsObject.class)
                            .setLifecycleOwner(this)
                            .build();

            mDoctorList.getRecycledViewPool().clear();

            AllChatsAdapter adapter = new AllChatsAdapter(options, this, userID);

            adapter.notifyDataSetChanged();
            mDoctorList.setAdapter(adapter);
        } catch (Exception ignored) { }
        findViewById(R.id.loader).setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}




