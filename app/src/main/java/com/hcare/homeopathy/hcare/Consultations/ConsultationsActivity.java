package com.hcare.homeopathy.hcare.Consultations;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

public class ConsultationsActivity extends AppCompatActivity {

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultations);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String mCurrentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        DatabaseReference mDoctorsDatabase = FirebaseDatabase.getInstance()
                .getReference().child("Private_consult").child(mCurrentUserId);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUserId);
        RecyclerView mDoctorList = findViewById(R.id.doctor_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mDoctorList.setLayoutManager(linearLayoutManager);

        mDoctorsDatabase.keepSynced(true);

        //list
        FirebaseRecyclerOptions<ConsultationsObject> options =
                new FirebaseRecyclerOptions.Builder<ConsultationsObject>()
                        .setQuery(mDoctorsDatabase.orderByChild("time"), ConsultationsObject.class)
                        .setLifecycleOwner(this)
                        .build();

        mDoctorList.getRecycledViewPool().clear();

        ConsultationsAdapter adapter = new ConsultationsAdapter
                (options, this,
                        FirebaseDatabase.getInstance().getReference(),
                        FirebaseDatabase.getInstance().getReference().child("Doctors"),
                        mCurrentUserId);

        adapter.notifyDataSetChanged();
        mDoctorList.setAdapter(adapter);

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




