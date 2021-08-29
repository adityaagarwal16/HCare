package com.hcare.homeopathy.hcare.PaymentsReferrals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.FirebaseClasses.UserObject;
import com.hcare.homeopathy.hcare.R;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.util.Objects;

public class RazorPay {

    float total;
    String email, phoneNumber, userID;
    AppCompatActivity activity;

    public RazorPay(float total, AppCompatActivity activity) {
        this.total = total;
        this.activity = activity;

        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    UserObject user = snapshot.getValue(UserObject.class);
                    email = Objects.requireNonNull(user).getEmail();
                    phoneNumber = Objects.requireNonNull(user).getPhoneNumber();
                } catch(Exception ignore) {}
                startPayment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void startPayment() {
        try {
            final Checkout co = new Checkout();
            JSONObject options = new JSONObject();
            options.put("name", "HCare");
            options.put("description", "Discount Applied");

            //You can omit the image option to fetch the image from dashboard
            // options.put("image", R.drawable.logo);
            int RAZORPAY_MULTIPLIER = 100;
            options.put("currency", "INR");
            options.put("amount", total * RAZORPAY_MULTIPLIER);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phoneNumber);

            options.put("prefill", preFill);

            co.setImage(R.drawable.logo_green);
            co.open(activity, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
