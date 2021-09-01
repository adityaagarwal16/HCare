package com.hcare.homeopathy.hcare.PaymentsReferrals;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class WalletOperations {

    DatabaseReference reference;
    String userID;

    public WalletOperations() {
        reference = FirebaseDatabase.getInstance().getReference();
        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();
    }

    void removeMoneyFromWallet(int moneyToRemove) {
        if(moneyToRemove > 0)  {
            reference.child("Users").child(userID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int moneyInWallet = 0;
                            try {
                                moneyInWallet = Objects.requireNonNull(
                                        snapshot.child("Wallet").getValue(Integer.class));

                            } catch (Exception ignore) { }
                            moneyInWallet -= moneyToRemove;
                            reference.child("Users").child(userID)
                                    .child("Wallet").setValue(moneyInWallet);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

    void addMoneyToWallet(String referredByUserID, int moneyToAdd) {
        if(PaymentSuccessful.moneyToReferredByUsersWallet > 0)  {
            reference.child("Users").child(referredByUserID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int moneyInWallet = 0;
                            try {
                                moneyInWallet = Objects.requireNonNull(
                                        snapshot.child("Wallet").getValue(Integer.class));
                            } catch (Exception ignore) { }
                            moneyInWallet += moneyToAdd;
                            reference.child("Users").child(referredByUserID)
                                    .child("Wallet").setValue(moneyInWallet);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

}
