package com.hcare.homeopathy.hcare.OrderTreatment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

import java.text.MessageFormat;
import java.util.Objects;

import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.orderMedicineCost;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.pricing;

public class CartActivity extends BaseActivity {

    String doctorID, userID;

    int ONE_MEDICINE_COST = 240, DELIVERY_FEE = 0;
    int total, discount, subtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        doctorID = getIntent().getStringExtra("user_id");

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();

        DatabaseReference rootReference =  FirebaseDatabase.getInstance()
                .getReference();

        rootReference.child(pricing)
                .child(orderMedicineCost).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    int val = Objects.requireNonNull(
                            dataSnapshot.getValue(Integer.class));
                    if(val > 150 && val < 1000)
                        ONE_MEDICINE_COST = val;
                } catch(NullPointerException ignored) {}
                rootReference.child("PrescribedMedicine")
                        .child(Objects.requireNonNull(doctorID)).child(userID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                setFields(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }

            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        setRecycler();
        setHeader();
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

    public void setHeader() {
        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            String name =  Objects.requireNonNull(
                                    dataSnapshot.child("name").getValue()).toString();
                            ((TextView) findViewById(R.id.header))
                                    .setText(MessageFormat.format("{0} {1}",
                                            "Prescription for",
                                            name.substring(0, 1).toUpperCase() + name.substring(1))
                                    );
                        } catch (Exception ignored) { }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("Doctors").child(doctorID)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String doctor = Objects.requireNonNull(dataSnapshot.child("name")
                        .getValue()).toString();
                ((TextView) findViewById(R.id.doctorName))
                        .setText(MessageFormat.format("{0} {1}",
                                "Dr.", doctor));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void proceedButton(View view) {
        Intent regIntent = new Intent(this, OrderNowActivity.class);
        regIntent.putExtra("user_id" , doctorID);
        regIntent.putExtra("price", subtotal);
        regIntent.putExtra("discount", discount);
        startActivity(regIntent);
    }

    private void setFields(DataSnapshot dataSnapshot) {
        subtotal = ONE_MEDICINE_COST * (int) dataSnapshot.getChildrenCount();
        ((TextView) findViewById(R.id.subTotal)).setText(String.valueOf(subtotal));

        discount = (subtotal / 100)* 40;
        ((TextView) findViewById(R.id.discount)).setText(String.valueOf(discount));

        total = subtotal - discount;
        ((TextView) findViewById(R.id.total)).setText(String.valueOf(total));

        ((TextView) findViewById(R.id.deliveryCharge)).setText(String.valueOf(DELIVERY_FEE));
    }

    private void setRecycler() {
        DatabaseReference mDoctorsDatabase = FirebaseDatabase
                .getInstance().getReference()
                .child("PrescribedMedicine")
                .child(doctorID)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance()
                        .getCurrentUser()).getUid());

        RecyclerView medicineRecycler = findViewById(R.id.medicineRecycler);
        medicineRecycler.setLayoutManager(new LinearLayoutManager(this));
        medicineRecycler.setHasFixedSize(true);
        FirebaseRecyclerOptions<PrescriptionObject> options =
                new FirebaseRecyclerOptions.Builder<PrescriptionObject>()
                        .setQuery(mDoctorsDatabase, PrescriptionObject.class)
                        .setLifecycleOwner(this)
                        .build();
        medicineRecycler.setAdapter(new PrescriptionAdapter(options));
    }
}



