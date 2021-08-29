package com.hcare.homeopathy.hcare.NewConsultation.Checkout;

import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.activeConsultations;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.DISEASE_OBJECT;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.issue;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.NewConsultation.Diseases;
import com.hcare.homeopathy.hcare.PaymentsReferrals.PaymentSuccessful;
import com.hcare.homeopathy.hcare.R;
import com.razorpay.PaymentResultListener;

import java.util.Objects;

public class CheckoutActivity extends BaseActivity implements PaymentResultListener {

    String userID;
    Boolean paymentSuccessful = false;
    Diseases disease;
    static String email = "", phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Objects.requireNonNull(getSupportActionBar())
                .setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();
        disease = (Diseases) getIntent().getSerializableExtra(DISEASE_OBJECT);

        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userID)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {email = (String) dataSnapshot
                        .child("email").getValue(); }
                catch (Exception ignored) { }
                try { phoneNumber = (String) dataSnapshot
                        .child("phone number").getValue(); }
                catch (Exception ignored) { }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        FirebaseDatabase.getInstance()
                .getReference().child(activeConsultations)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            if(paymentSuccessful)
                                showCheckoutSuccessfulFragment();
                            else {
                                //TODO CHECK
                                FragmentTransaction transaction = getSupportFragmentManager()
                                        .beginTransaction();
                                if (dataSnapshot.hasChild(userID))
                                    transaction.replace(R.id.frameLayout,
                                            new CheckoutDisableFragment())
                                            .commit();
                                else {
                                    CheckoutFragment fragment = new CheckoutFragment();
                                    Bundle args = new Bundle();
                                    args.putSerializable(DISEASE_OBJECT,
                                            getIntent().getSerializableExtra(DISEASE_OBJECT));
                                    args.putString(issue, getIntent().getStringExtra(issue));

                                    fragment.setArguments(args);
                                    transaction.replace(R.id.frameLayout, fragment).commit();
                                }
                            }
                        } catch(Exception ignored) {}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });


        //TODO CHECK
    }

    private void showCheckoutSuccessfulFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new CheckoutSuccessfulFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        if(paymentSuccessful)
            showCheckoutSuccessfulFragment();
        super.onResume();
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
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            paymentSuccessful = true;
            new PaymentSuccessful(userID, disease, getIntent().getStringExtra(issue));
        } catch (Exception ignored) { }
    }

    //TODO CHECK
    @Override
    public void onPaymentError(int code, String response) {
        try {
            paymentSuccessful = true;
            //sendRequest();
            Toast.makeText(this, "Payment failed",
                    Toast.LENGTH_LONG).show();
        } catch (Exception  e) {
            e.printStackTrace();
        }
    }
}
