package com.hcare.homeopathy.hcare.NavigationItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

public class HCareWalletActivity extends AppCompatActivity {

    TextView moneyInWallet;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hcare_wallet);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();

        moneyInWallet = findViewById(R.id.moneyInWalletTV);
        FirebaseDatabase.getInstance()
                    .getReference().child("Users").child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        String amount = Objects
                                .requireNonNull(snapshot.child("Wallet")
                                        .getValue(Integer.class)).toString();
                        moneyInWallet.setText("₹ " + amount);
                    } catch (Exception ignored) {}
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
}