package com.hcare.homeopathy.hcare.Orders;

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

public class AllOrdersActivity extends BaseActivity {

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                    .getCurrentUser()).getUid();
        setRecycler();
    }

    private void setRecycler() {
        try {
            DatabaseReference mDoctorsDatabase =
                    FirebaseDatabase.getInstance().getReference()
                            .child("Orders").child(userID);

            mDoctorsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if (!dataSnapshot.exists()) {
                            findViewById(R.id.nothingHereLayout).setVisibility(View.VISIBLE);
                        }
                    } catch (Exception ignored) { }
                    findViewById(R.id.loader).setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

//              :/
            String timeSort = null;
            if(mDoctorsDatabase.child("time")!=null) {
                timeSort = "time";
            }
            else {
                timeSort = "Ordertime";
            }

            RecyclerView mDoctorList = findViewById(R.id.recycler);
            mDoctorList.setHasFixedSize(true);
            mDoctorList.setLayoutManager(new LinearLayoutManager(this));
            FirebaseRecyclerOptions<AllOrdersObject> options =
                    new FirebaseRecyclerOptions.Builder<AllOrdersObject>()
                            .setQuery(mDoctorsDatabase.orderByChild(timeSort), AllOrdersObject.class)
                            .setLifecycleOwner(this)
                            .build();

            mDoctorList.setAdapter(new AllOrdersAdapter(options, this));
        } catch (Exception ignored) { }
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
