package com.hcare.homeopathy.hcare.PreConsultation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.MainActivity;
import com.hcare.homeopathy.hcare.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultListener {

    private Button paynowBtn;

    private DatabaseReference userRef;
    private DatabaseReference mConsultReqDatabase;
    private String phoneno,mail,details,patientname,reqtype,age,sex;
    private int count;
    private final int testcount =0;
    private TextView total,net,offertxt;
    private int netprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Checkout");

        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        total =(TextView)findViewById(R.id.discountfeetxt);
        net =(TextView)findViewById(R.id.conspayfeetxt);
        offertxt =(TextView)findViewById(R.id.offertext);
        final String current_uid = mCurrentUser.getUid();

        paynowBtn = (Button)findViewById(R.id.paynowntn);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        DatabaseReference mConsultvarify =
                FirebaseDatabase.getInstance().getReference().child("public_Consulting");
        mConsultReqDatabase = FirebaseDatabase.getInstance()
                .getReference().child("public_Consulting").child(current_uid);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phoneno =  (String) dataSnapshot.child("phone number").getValue();
                mail  = (String) dataSnapshot.child("email").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        mConsultvarify.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(current_uid)) {
                    paynowBtn.setEnabled(false);
                    showPopup();
                }
                else {
                    paynowBtn.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        details = getIntent().getStringExtra("details1");
        reqtype = getIntent().getStringExtra("request_type1");
        patientname = getIntent().getStringExtra("name");
        age = getIntent().getStringExtra("age");
        sex = getIntent().getStringExtra("sex");

        userRef.child("consultCount").addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = (int) dataSnapshot.getChildrenCount();
                if (count == testcount) {
                    total.setText(""+100);
                    net.setText(""+99);
                    netprice = 99*100;
                    offertxt.setText("First 50");

                } else {
                    total.setText(""+50);
                    net.setText(""+149);
                    netprice = 149*100;
                    offertxt.setText("50RSOFF");
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        paynowBtn.setOnClickListener(v -> startPayment());

        flipper();
    }

    void flipper() {
        int[] images ={R.drawable.review1, R.drawable.review2,R.drawable.review3,R.drawable.review4};
        ViewFlipper mFlipper = findViewById(R.id.imageView9);

        for (int image: images) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(image);

            mFlipper.addView(imageView);
            mFlipper.setFlipInterval(4000);
            mFlipper.setAutoStart(true);

            mFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
            mFlipper.setInAnimation(this,android.R.anim.slide_in_left);        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendRequest(){
        String date = DateFormat.getDateTimeInstance().format(new Date());
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date2 = new Date();
        String time = dateFormat.format(date2);

        HashMap<String, String> notifdata = new HashMap<>();
        notifdata.put("details1", details);
        notifdata.put("request_type1", reqtype);
        notifdata.put("date", date);
        notifdata.put("name", patientname);
        notifdata.put("age", age);
        notifdata.put("sex", sex);
        notifdata.put("Time",time);

        userRef.child("consultCount").push().child(patientname).setValue(reqtype);
        mConsultReqDatabase.setValue(notifdata);
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
            options.put("amount", netprice);

            JSONObject preFill = new JSONObject();
            preFill.put("email", mail);
            preFill.put("contact", phoneno);

            options.put("prefill", preFill);

            co.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            sendRequest();
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        } catch (Exception ignored) { }
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment failed: "+ response, Toast.LENGTH_SHORT).show();
    }

    private PopupWindow pw;

    private void showPopup() {
        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.paymentpop,
                    (ViewGroup) findViewById(R.id.popup_1));

            pw = new PopupWindow(
                    layout,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true
            );

            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Button Close = (Button) layout.findViewById(R.id.close_popup);

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
