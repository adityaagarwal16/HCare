package com.hcare.homeopathy.hcare.PreConsultation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.DiseaseInfo;
import com.hcare.homeopathy.hcare.Diseases;
import com.hcare.homeopathy.hcare.MainActivity;
import com.hcare.homeopathy.hcare.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import static com.hcare.homeopathy.hcare.PreConsultation.Constants.CONSULTATION_FEE;
import static com.hcare.homeopathy.hcare.PreConsultation.Constants.DISCOUNT;
import static com.hcare.homeopathy.hcare.PreConsultation.Constants.DISEASE_OBJECT;
import static com.hcare.homeopathy.hcare.PreConsultation.Constants.FIRST_100;
import static com.hcare.homeopathy.hcare.PreConsultation.Constants.FIRST_100_COUPON;
import static com.hcare.homeopathy.hcare.PreConsultation.Constants.RS50_COUPON;
import static com.hcare.homeopathy.hcare.PreConsultation.Constants.TEST_COUNT;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultListener {

    private CardView payNowBtn;
    private DatabaseReference userRef;
    private String phoneNum, mail, current_uid;
    private int totalAmount = 0;
    String patientName, patientIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        current_uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        patientName = getIntent().getStringExtra("name");
        patientIssue = getIntent().getStringExtra("details1");

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phoneNum = (String) dataSnapshot.child("phone number").getValue();
                mail  = (String) dataSnapshot.child("email").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        payNowBtn = findViewById(R.id.payNowButton);
        FirebaseDatabase.getInstance()
                .getReference().child("public_Consulting")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(current_uid)) {
                    payNowBtn.setEnabled(false);
                    showPopup();
                }
                else {
                    payNowBtn.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        userRef.child("consultCount").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((int) dataSnapshot.getChildrenCount() == TEST_COUNT) {
                    setFields(FIRST_100, FIRST_100_COUPON);
                    totalAmount = CONSULTATION_FEE - FIRST_100;
                } else {
                    setFields(DISCOUNT, RS50_COUPON);
                    totalAmount = CONSULTATION_FEE - DISCOUNT;
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        payNowBtn.setOnClickListener(v -> startPayment());

        flipper();
        setHeaders();
    }

    void setHeaders() {
        try {
            DiseaseInfo disease = new DiseaseInfo((Diseases) getIntent().getSerializableExtra(DISEASE_OBJECT));
            ((TextView) findViewById(R.id.header)).setText
                    (MessageFormat.format("{0} {1}", "Consultation for",
                            patientName.substring(0, 1).toUpperCase() + patientName.substring(1)));
            Log.i("disease", String.valueOf(disease));
            ((TextView) findViewById(R.id.diseaseName)).setText(disease.getDiseaseName());
            ((ImageView) findViewById(R.id.diseaseImage)).setImageResource(disease.getDrawable());
            if(!patientIssue.equals(""))
                ((TextView) findViewById(R.id.patientIssue)).setText(patientIssue);
        } catch(Exception ignored) {}
    }
    
    void setFields(int discount, String coupon) {
        ((TextView) findViewById(R.id.subTotal)).setText(String.valueOf(CONSULTATION_FEE));
        ((TextView) findViewById(R.id.discount)).setText(String.valueOf(discount));
        ((TextView) findViewById(R.id.total)).setText(String.valueOf(CONSULTATION_FEE - discount));
        ((TextView) findViewById(R.id.couponHeader)).setText(String.valueOf(coupon));
    }

    void flipper() {
        int[] images ={R.drawable.review1,
                R.drawable.review2,
                R.drawable.review3,
                R.drawable.review4};
        ViewFlipper mFlipper = findViewById(R.id.imageFlipper);

        for (int image: images) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(image);

            mFlipper.addView(imageView);
            mFlipper.setFlipInterval(4000);
            mFlipper.setAutoStart(true);

            mFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
            mFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        }
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
        String date = DateFormat.getDateTimeInstance().format(new Date());
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date2 = new Date();
        String time = dateFormat.format(date2);

        String requestType =  getIntent().getStringExtra("request_type1");

        HashMap<String, String> notifyData = new HashMap<>();
        notifyData.put("details1", patientIssue);
        notifyData.put("request_type1", requestType);
        notifyData.put("date", date);
        notifyData.put("name", patientName);
        notifyData.put("age", getIntent().getStringExtra("age"));
        notifyData.put("sex", getIntent().getStringExtra("sex"));
        notifyData.put("Time",time);

        userRef.child("consultCount").push().child(patientName).setValue(requestType);

        DatabaseReference mConsultReqDatabase = FirebaseDatabase.getInstance()
                .getReference().child("public_Consulting").child(current_uid);
        mConsultReqDatabase.setValue(notifyData);

        Intent regIntent = new Intent(CheckoutActivity.this, MainActivity.class);
        regIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(regIntent);
        finish();
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final AppCompatActivity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Hcare");
            options.put("description", "discount applied");
            //You can omit the image option to fetch the image from dashboard
           // options.put("image", R.drawable.hcarehori);
            options.put("currency", "INR");

            int RAZORPAY_MULTIPLIER = 100;
            options.put("amount", totalAmount * RAZORPAY_MULTIPLIER);

            JSONObject preFill = new JSONObject();
            preFill.put("email", mail);
            preFill.put("contact", phoneNum);

            options.put("prefill", preFill);

            co.open(activity, options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            sendRequest();
            Toast.makeText(getApplicationContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
        } catch (Exception ignored) { }
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_LONG).show();
    }

    private PopupWindow pw;
    private void showPopup() {
        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.paymentpop,
                    findViewById(R.id.popup_1));

            pw = new PopupWindow(
                    layout,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true
            );

            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Button Close = layout.findViewById(R.id.close_popup);

            Close.setOnClickListener(close);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener close = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
            Intent regIntent = new Intent(CheckoutActivity.this, MainActivity.class);
            regIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(regIntent);
            finish();

        }
    };
}
