package com.hcare.homeopathy.hcare.OrderTreatment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
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
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class OrderNowActivity extends AppCompatActivity implements PaymentResultListener {

    private DatabaseReference reference, userRef;
    private String doctorID, userID;

    private String name, email, phoneNumber;
    private String bpinCode;
    private String bFlat;
    private String bTown;
    private String bState;
    int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_now);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();

        userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userID);

        doctorID = getIntent().getStringExtra("user_id");
        reference = FirebaseDatabase.getInstance().getReference();

        setFields();
        setAddress();


    }

    private void setFields() {
        totalPrice = getIntent().getIntExtra("price", 0);

        ((TextView) findViewById(R.id.total)).setText(String.valueOf(totalPrice));
        ((TextView) findViewById(R.id.subTotal)).setText(String.valueOf(totalPrice));
        ((TextView) findViewById(R.id.deliveryCharge)).setText(String.valueOf(0));

        ((TextView) findViewById(R.id.savings))
                .setText(MessageFormat.format("{0} {1}",
                        "Your Savings : ₹ ",
                        getIntent().getIntExtra("discount", 0))
                );

        reference.child("Users").child(userID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            name = Objects.requireNonNull(
                                    dataSnapshot.child("name").getValue()).toString();
                            ((EditText) findViewById(R.id.name))
                                    .setText(name);

                            phoneNumber = Objects.requireNonNull(
                                    dataSnapshot.child("phone number").getValue()).toString();
                            ((EditText) findViewById(R.id.phoneNumber))
                                    .setText(phoneNumber);

                        } catch (Exception ignored) { }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void setAddress() {

    }

    public void placeOrder(View view) {
        /*pincode = (EditText) findViewById(R.id.PinCode);
        Flat =(EditText) findViewById(R.id.FlatAddress);
        Towm =(EditText) findViewById(R.id.City);
        state =(EditText) findViewById(R.id.state);*/
        name = ((EditText) findViewById(R.id.name)).getText().toString();
        phoneNumber = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();

        try {
            if (!name.isEmpty()) {
                if (!phoneNumber.isEmpty()) {
                    /*bpinCode = pincode.getText().toString();
                    if (!TextUtils.isEmpty(bpinCode)) {
                        bFlat = Flat.getText().toString();
                        if (!TextUtils.isEmpty(bFlat)) {
                            bTown = Towm.getText().toString();
                            if (!TextUtils.isEmpty(bTown)) {
                                bState = state.getText().toString();
                                if (!TextUtils.isEmpty(bState)) {


                                    // payedorder();
                                    startPayment();
                                    Toast.makeText(this, "pay now ",
                                            Toast.LENGTH_LONG).show();


                                } else {
                                    Toast.makeText(this, "please enter your State ",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(this, "please enter your City ",
                                        Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(this, "please enter your Flat No ",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "please enter your Pin Code ",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "please enter your Phone Number ",
                            Toast.LENGTH_LONG).show();
                }
*/
                }
            } else {
                Toast.makeText(this, "please enter your name ", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e){
            Toast.makeText(this, "Error, please try again", Toast.LENGTH_LONG).show();
        }
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

    private void showPopup() {
        PopupWindow pw;
        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View layout = inflater.inflate(R.layout.addresspop, null);
            pw = new PopupWindow(layout,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true);

            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Button Close = (Button) layout.findViewById(R.id.close_popup);
            Close.setOnClickListener(v -> {
                pw.dismiss();
                Intent regIntent = new Intent(this, MainActivity.class);
                regIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(regIntent);
                finish();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPayment() {
        final Checkout co = new Checkout();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    email = (String) dataSnapshot.child("email").getValue();
                    JSONObject options = new JSONObject();
                    options.put("name", "Medicine");
                    options.put("description", "40% discount applied");

                    //You can omit the image option to fetch the image from dashboard
                    // options.put("image", R.drawable.logo);
                    int RAZORPAY_MULTIPLIER = 100;
                    options.put("currency", "INR");
                    options.put("amount", totalPrice * RAZORPAY_MULTIPLIER);

                    JSONObject preFill = new JSONObject();
                    preFill.put("email", email);
                    preFill.put("contact", (String) dataSnapshot.child("phone number").getValue());

                    options.put("prefill", preFill);

                    co.open(OrderNowActivity.this, options);
                } catch (Exception e) {
                    Toast.makeText(OrderNowActivity.this ,
                            "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            orderSuccessful();
        } catch (Exception e) {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
    }

    private void orderSuccessful() {
        String OrderId ="Hcr"+getRandomNumberString();
        String time = DateFormat.getDateTimeInstance().format(new Date());

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("FullName", name);
        userMap.put("PhoneNumber", phoneNumber);
        userMap.put("PinCode", bpinCode);
        userMap.put("Address", bFlat);
        userMap.put("PatientId", userID);
        userMap.put("City", bTown);
        userMap.put("State", bState);
        userMap.put("Doctor", doctorID);
        userMap.put("emailId", email);
        userMap.put("Amount", String.valueOf(totalPrice));
        userMap.put("OrderStatus", "Recieved");
        userMap.put("orderId", OrderId);
        userMap.put("Ordertime", time);

        reference.child("neworder").child(OrderId).setValue(userMap);
        reference.child("Orders").child(userID)
                .child(OrderId).setValue(userMap).addOnCompleteListener(task -> {
            reference.child("Doctors").child(doctorID)
                    .child("count").push().setValue(userID);

           // userRef.child("consultCount").removeValue();
            reference.child("messages").child(userID).child(doctorID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        child.getRef().child("ordering").setValue("ordered");
                        reference.child("messages").child(doctorID).child(userID)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    child.getRef().child("ordering").setValue("ordered");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
        showPopup();
    }

    @SuppressLint("DefaultLocale")
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }


}
