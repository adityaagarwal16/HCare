package com.hcare.homeopathy.hcare.NavigationItems.Orders;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class OrdersActivity extends BaseActivity {

    private DatabaseReference userRef;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userID = Objects.requireNonNull(
                FirebaseAuth.getInstance().getCurrentUser()).getUid();

        userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userID);

        setRecycler();
    }

    private void setRecycler() {
        DatabaseReference mDoctorsDatabase =
                FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(userID);

        mDoctorsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    findViewById(R.id.nothingHereLayout).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        RecyclerView mDoctorList = findViewById(R.id.recycler);
        mDoctorList.setHasFixedSize(true);
        mDoctorList.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<OrdersObject> options =
                new FirebaseRecyclerOptions.Builder<OrdersObject>()
                        .setQuery(mDoctorsDatabase, OrdersObject.class)
                        .setLifecycleOwner(this)
                        .build();

        mDoctorList.setAdapter(new OrdersAdapter(options, this));
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
