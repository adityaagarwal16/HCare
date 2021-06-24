package com.hcare.homeopathy.hcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.OrderTreatment.OrderNowActivity;
import com.hcare.homeopathy.hcare.OrderTreatment.PrescriptionAdapter;
import com.hcare.homeopathy.hcare.OrderTreatment.PrescriptionObject;

import java.util.Objects;

import static com.hcare.homeopathy.hcare.FirebaseConstants.coronaVirus;

public class CoronaVirusActivity extends AppCompatActivity {

    static final int ONE_MEDICINE_COST = 300, DELIVERY_FEE = 0;
    int total, discount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona_virus);

        FirebaseDatabase.getInstance()
                .getReference().child("PrescribedMedicine")
                .child(Objects.requireNonNull(coronaVirus))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Log.i("data", child.toString());
                        }
                        setFields(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        setRecycler();
    }


    private void setFields(DataSnapshot dataSnapshot) {
        final int subtotal = ONE_MEDICINE_COST * (int) dataSnapshot.getChildrenCount();
        ((TextView) findViewById(R.id.subTotal)).setText(String.valueOf(subtotal));

        discount = (subtotal / 100)* 40;
        ((TextView) findViewById(R.id.discount)).setText(String.valueOf(discount));

        total = subtotal - discount;
        ((TextView) findViewById(R.id.total)).setText(String.valueOf(total));

        ((TextView) findViewById(R.id.deliveryCharge)).setText(String.valueOf(DELIVERY_FEE));
    }

    public void proceedButton(View view) {
        Intent regIntent = new Intent(this, OrderNowActivity.class);
        regIntent.putExtra("user_id" , coronaVirus);
        regIntent.putExtra("price", total);
        regIntent.putExtra("discount", discount);
        startActivity(regIntent);
    }

    private void setRecycler() {
        DatabaseReference mDoctorsDatabase = FirebaseDatabase
                .getInstance().getReference()
                .child("PrescribedMedicine")
                .child(Objects.requireNonNull(coronaVirus));

        RecyclerView medicineRecycler = findViewById(R.id.CovidRV);
        medicineRecycler.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<PrescriptionObject> options =
                new FirebaseRecyclerOptions.Builder<PrescriptionObject>()
                        .setQuery(mDoctorsDatabase, PrescriptionObject.class)
                        .setLifecycleOwner(this)
                        .build();
        medicineRecycler.setAdapter(new PrescriptionAdapter(options));
    }

    public void Back(View view) {
        onBackPressed();
    }
}