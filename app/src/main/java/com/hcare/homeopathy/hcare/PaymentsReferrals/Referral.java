package com.hcare.homeopathy.hcare.PaymentsReferrals;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

public class Referral {

    private final Context context;
    static private final int initialMoneyToWallet = 15;

    public Referral(Context context) {
        this.context = context;
        checkFirstOpen();
    }

    private void checkFirstOpen() {
        SharedPreferences sharedpreferences =
                context.getSharedPreferences(
                        context.getString(R.string.app_name), Context.MODE_PRIVATE);

        String prevStarted = "prevStarted";
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, true);
            editor.apply();

            //check if user has already been referred
            FirebaseDatabase.getInstance()
                    .getReference().child("Users")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                String referredBy = Objects.requireNonNull(
                                        snapshot.child("ReferredBy").getValue())
                                        .toString();
                                if(referredBy.isEmpty())
                                    retrieveReferral();
                                else
                                    Toast.makeText(context, "Already used Referral",
                                            Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                retrieveReferral();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
        }
    }

    private void retrieveReferral() {
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();
        if(IntentStatic.intent != null) {
            try {
                FirebaseDynamicLinks.getInstance()
                        .getDynamicLink(IntentStatic.intent)
                        .addOnSuccessListener((Activity) context, pendingDynamicLinkData -> {
                            try {
                                // Adding the details of referredTo user to the db of referredBy user
                                Uri deepLink;
                                deepLink = pendingDynamicLinkData.getLink();
                                String referLink = Objects.requireNonNull(deepLink).toString();
                                referLink = referLink.substring(referLink.lastIndexOf("%") + 1);
                                String referredByUserID =
                                        referLink.substring(referLink.lastIndexOf("=") + 1);

                                if (!referredByUserID.isEmpty()) {
                                    //check if user has referred self
                                    if (!userID.equals(referredByUserID)) {
                                        referralOperation(userID, referredByUserID);
                                    } else
                                        Toast.makeText(context, "Can't refer Self",
                                                Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception ignore) {
                            }
                        });
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void referralOperation(String userID, String referredByUserID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference();
        DatabaseReference referredByUser =
                databaseReference.child("Users")
                        .child(referredByUserID);

        referredByUser.child("Referrals").child("Users").child(userID)
                .setValue(System.currentTimeMillis());

        // Adding details of referredBy user to db of current user
        databaseReference.child("Users").child(userID).child("ReferredBy")
                .setValue(referredByUserID);

        //add 15 to current user's wallet
        try {
            new WalletOperations().addMoneyToWallet(userID, initialMoneyToWallet);
            Toast.makeText(context,
                    "Congratulations! ₹15 has been successfully added to your HCare Wallet",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context,
                    "Error in Adding HCare Money, please try again!",
                    Toast.LENGTH_LONG).show();
        }
    }

}
