package com.hcare.homeopathy.hcare.NavigationItems.Orders;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.OrderTreatment.PrescriptionAdapter;
import com.hcare.homeopathy.hcare.OrderTreatment.PrescriptionObject;
import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

public class PrescriptionActivity extends BaseActivity {

    DatabaseReference userRef;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        Objects.requireNonNull(getSupportActionBar())
                .setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();

        userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userID);

        setRecycler();
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

    private void setRecycler() {

        DatabaseReference mDoctorsDatabase = FirebaseDatabase
                .getInstance().getReference()
                .child("PrescribedMedicine")
                .child(getIntent().getStringExtra("user_id"))
                .child(Objects.requireNonNull(FirebaseAuth.getInstance()
                        .getCurrentUser()).getUid());

        RecyclerView medicineRecycler = findViewById(R.id.recycler);
        medicineRecycler.setLayoutManager
                (new LinearLayoutManager(this));
        medicineRecycler.setHasFixedSize(true);
        FirebaseRecyclerOptions<PrescriptionObject> options =
                new FirebaseRecyclerOptions.Builder<PrescriptionObject>()
                        .setQuery(mDoctorsDatabase, PrescriptionObject.class)
                        .setLifecycleOwner(this)
                        .build();
        medicineRecycler.setAdapter(new PrescriptionAdapter(options));
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


