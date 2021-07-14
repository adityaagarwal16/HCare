package com.hcare.homeopathy.hcare.NewConsultation.Checkout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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
import com.hcare.homeopathy.hcare.FirebaseClasses.ConsultationObject;
import com.hcare.homeopathy.hcare.NewConsultation.DiseaseInfo;
import com.hcare.homeopathy.hcare.NewConsultation.Diseases;
import com.hcare.homeopathy.hcare.R;
import com.razorpay.PaymentResultListener;

import java.util.Objects;
import java.util.Random;

import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.activeConsultations;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.recentConsultations;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.userConsultations;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.DISEASE_OBJECT;
import static com.hcare.homeopathy.hcare.NewConsultation.Constants.issue;

public class CheckoutActivity extends BaseActivity implements PaymentResultListener {

    String userID;
    Boolean paymentSuccessful = false;
    Diseases disease;
    static String email = "", phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Objects.requireNonNull(
                getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();

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

    private void sendRequest() {
        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();
        disease = (Diseases) getIntent().getSerializableExtra(DISEASE_OBJECT);

        String consultationID = generateConsultationID();

        String patientIssue = "";
        try {
            patientIssue = getIntent().getStringExtra(issue);
        } catch (Exception ignored) {}

        ConsultationObject consultation = new ConsultationObject();
        try {
            consultation.setConsultationID(consultationID);
            consultation.setDisease(new DiseaseInfo(disease).getDiseaseName());
            consultation.setIssue(patientIssue);
            consultation.setTime(System.currentTimeMillis());
            consultation.setUserID(userID);
        } catch (Exception e) {e.printStackTrace();}
        Log.i("consultation", String.valueOf(consultation));

        //temporary store
        FirebaseDatabase.getInstance().getReference()
                .child(activeConsultations)
                .child(userID).setValue(consultation);

        //permanent store - user
        FirebaseDatabase.getInstance().getReference()
                .child(userConsultations)
                .child(userID)
                .child(consultationID)
                .setValue(consultation);

        //permanent store - recent
        FirebaseDatabase.getInstance().getReference()
                .child(recentConsultations)
                .child(String.valueOf(consultation.getTime()))
                .setValue(consultation);
    }

    @SuppressLint("DefaultLocale")
    public String generateConsultationID() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(9999999);

        // this will convert any number sequence into 6 character.
        return "CN" + String.format("%07d", number);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            paymentSuccessful = true;
            sendRequest();
        } catch (Exception ignored) { }
    }

    //TODO CHECK
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed",
                    Toast.LENGTH_LONG).show();
        } catch (Exception  e) {
            e.printStackTrace();
        }
    }
}
