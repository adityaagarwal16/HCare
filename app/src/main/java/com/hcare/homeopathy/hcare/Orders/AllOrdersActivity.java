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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.FirebaseClasses.OrderObject;
import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.customerOrders;

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
            Query mDoctorsDatabase =
                    FirebaseDatabase.getInstance().getReference()
                            .child(customerOrders).child("J1D3n17pWLS9rfv6ql5vCpNgdTx2")
                            .orderByChild("time");

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

            RecyclerView mDoctorList = findViewById(R.id.recycler);
            mDoctorList.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);

            mDoctorList.setLayoutManager(linearLayoutManager);
            FirebaseRecyclerOptions<OrderObject> options =
                    new FirebaseRecyclerOptions.Builder<OrderObject>()
                            .setQuery(mDoctorsDatabase, OrderObject.class)
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
