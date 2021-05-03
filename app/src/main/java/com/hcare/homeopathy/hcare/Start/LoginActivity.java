package com.hcare.homeopathy.hcare.Start;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Slide;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hcare.homeopathy.hcare.MainActivity;
import com.hcare.homeopathy.hcare.NavigationItems.ProfileActivity;
import com.hcare.homeopathy.hcare.R;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    public static boolean OTP_FRAGMENT_OPEN = false;
    public static PhoneAuthProvider.ForceResendingToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new PhoneNumberFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if(OTP_FRAGMENT_OPEN)
            openPhoneFragment();
        else
            super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account =
                        task.getResult(ApiException.class);
                firebaseAuthWithGoogle(Objects.requireNonNull(account));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential
                (account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the
                        // signed-in user's information

                        Log.d("TAG", "signInWithCredential:success");
                        FirebaseUser user = FirebaseAuth
                                .getInstance().getCurrentUser();

                        String uid = Objects.requireNonNull(user).getUid();
                        String device_token = FirebaseInstanceId
                                .getInstance().getToken();

                        DatabaseReference mDatabase = FirebaseDatabase
                                .getInstance()
                                .getReference().child("Users")
                                .child(uid);

                        Intent intent;
                        if (mDatabase == null) {
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("phone number", "");
                            userMap.put("name", account.getDisplayName());
                            userMap.put("age", "");
                            userMap.put("sex", "Male");
                            userMap.put("thumb_image", "default");
                            userMap.put("image", "default");
                            userMap.put("email", account.getEmail());
                            userMap.put("device_token", device_token);
                            userMap.put("status", "online");
                            mDatabase.setValue(userMap);

                            intent = new Intent(this, MainActivity.class);
                            intent.addFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            startActivity(new Intent(this, ProfileActivity.class));

                            finish();
                        } else {
                            Toast.makeText(this,
                                    "Sign in successful", Toast.LENGTH_SHORT).show();
                            intent = new Intent(this, MainActivity.class);
                            intent.addFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                    }

                });
    }

    void openPhoneFragment() {
        PhoneNumberFragment fragment = new PhoneNumberFragment();
        fragment.setEnterTransition(new Slide(Gravity.START).setDuration(200));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}