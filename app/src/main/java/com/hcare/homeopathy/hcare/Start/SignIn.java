package com.hcare.homeopathy.hcare.Start;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hcare.homeopathy.hcare.Main.MainActivity;
import com.hcare.homeopathy.hcare.NavigationItems.ProfileActivity;
import com.hcare.homeopathy.hcare.R;

import java.util.HashMap;
import java.util.Objects;

public class SignIn {

    Context context;
    String userID;

    public SignIn(Context context) {
        this.context = context;
    }

    private void login() {
        Toast.makeText(context,
                "Sign in successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public void signInWithPhoneAuthCredential(View root, String phoneNumber,
                                              PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful())
                        taskSuccessful().child("Users")
                                .child(userID)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.i("account", phoneNumber);
                                        if (dataSnapshot.exists())
                                            login();
                                        else
                                            newAccount(phoneNumber,"", "");
                                        root.findViewById(R.id.circleLoader)
                                                .setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                });
                    else {
                        root.findViewById(R.id.circleLoader).setVisibility(View.GONE);
                        if (task.getException()
                                instanceof FirebaseAuthInvalidCredentialsException)
                            Toast.makeText(context,
                                    "Invalid OTP", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(context,
                                    "Verification failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private DatabaseReference taskSuccessful() {
        FirebaseUser user = FirebaseAuth
                .getInstance().getCurrentUser();

        userID = Objects.requireNonNull(user).getUid();

        return FirebaseDatabase.getInstance().getReference();
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential
                (account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful())
                        taskSuccessful().child("Users").child(userID)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.i("account", account.getDisplayName()
                                                +"\n"+account.getEmail());
                                        if (dataSnapshot.exists())
                                            login();
                                        else newAccount("",
                                                account.getDisplayName(),
                                                account.getEmail());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                });
                    else
                        Toast.makeText(context,"Error : " + task.getException(),
                                Toast.LENGTH_SHORT).show();

                });
    }

    private void newAccount(String phoneNumber, String name, String email) {
        final String[] token = {""};
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isComplete()){
                token[0] = task.getResult();
                HashMap<String, String> userMap = new HashMap<>();
                userMap.put("phone number", phoneNumber);
                userMap.put("name", name);
                userMap.put("age", "");
                userMap.put("sex", "Male");
                userMap.put("image", "default");
                userMap.put("email", email);
                userMap.put("device_token", token[0]);
                userMap.put("status", "online");

                FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child(userID)
                        .setValue(userMap);
            }
        });



        HashMap<String, String> loggedInData = new HashMap<>();
        loggedInData.put("phone_number", phoneNumber);
        loggedInData.put("email", email);
        loggedInData.put("name", name);
        loggedInData.put("age", "");
        loggedInData.put("sex", "Male");
        FirebaseDatabase.getInstance().getReference()
                .child("loggedin")
                .child(userID)
                .setValue(loggedInData);

        Intent intent = new Intent(context, MainActivity.class);

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        Intent profile = new Intent(context, ProfileActivity.class);
        profile.putExtra("newUser", true);
        profile.putExtra("name", name);
        profile.putExtra("phoneNumber", phoneNumber);
        profile.putExtra("email", email);
        context.startActivity(profile);

        ((Activity) context).finish();
    }

}
