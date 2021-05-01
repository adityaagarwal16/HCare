package com.hcare.homeopathy.hcare.Checkout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.Diseases;
import com.hcare.homeopathy.hcare.R;
import com.razorpay.PaymentResultListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import static com.hcare.homeopathy.hcare.Checkout.Constants.DISEASE_OBJECT;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultListener {

    String userID;
    Boolean paymentSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        FirebaseDatabase.getInstance()
                .getReference().child("public_Consulting")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            if(paymentSuccessful)
                                transaction.replace(R.id.frameLayout, new CheckoutSuccessfulFragment())
                                        .commit();
                            else {
                                if (!dataSnapshot.hasChild(userID))
                                    transaction.replace(R.id.frameLayout, new CheckoutDisableFragment())
                                            .commit();
                                else {
                                    CheckoutFragment fragment = new CheckoutFragment();
                                    Bundle args = new Bundle();
                                    args.putSerializable(DISEASE_OBJECT,
                                            (Diseases) getIntent().getSerializableExtra(DISEASE_OBJECT));
                                    args.putString("details1", getIntent().getStringExtra("details1"));
                                    args.putString("name", getIntent().getStringExtra("name"));
                                    args.putString("age", getIntent().getStringExtra("age"));
                                    args.putString("sex", getIntent().getStringExtra("sex"));

                                    fragment.setArguments(args);
                                    transaction.replace(R.id.frameLayout, fragment).commit();
                                }
                            }
                        } catch(Exception ignored) {}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
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

    private void sendRequest() {
        paymentSuccessful = true;

        String date = DateFormat.getDateTimeInstance().format(new Date());
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date2 = new Date();
        String time = dateFormat.format(date2);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();
        String patientName = getIntent().getStringExtra("name");
        String patientIssue = getIntent().getStringExtra("details1");
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference().child("Users").child(userID);

        HashMap<String, String> notifyData = new HashMap<>();
        notifyData.put("details1", patientIssue);
        notifyData.put("request_type1", patientName);
        notifyData.put("date", date);
        notifyData.put("name", patientName);
        notifyData.put("age", getIntent().getStringExtra("age"));
        notifyData.put("sex", getIntent().getStringExtra("sex"));
        notifyData.put("Time",time);

        userRef.child("consultCount").push().child(Objects.requireNonNull(patientName))
                .setValue(patientName);

        DatabaseReference mConsultReqDatabase = FirebaseDatabase.getInstance()
                .getReference().child("public_Consulting").child(userID);
        mConsultReqDatabase.setValue(notifyData);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            sendRequest();
        } catch (Exception ignored) { }
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_LONG).show();
    }
}
