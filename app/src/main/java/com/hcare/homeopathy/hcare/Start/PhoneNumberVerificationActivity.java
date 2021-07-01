package com.hcare.homeopathy.hcare.Start;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import androidx.transition.Slide;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.R;
import com.synnapps.carouselview.CarouselView;

import java.util.Objects;

public class PhoneNumberVerificationActivity extends BaseActivity {

    public static boolean OTP_FRAGMENT_OPEN = false;
    public static PhoneAuthProvider.ForceResendingToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_verification);

        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new PhoneNumberFragment())
                    .commit();
        } catch (Exception ignored) {}

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
            GoogleSignInAccount account =
                    task.getResult(ApiException.class);
            new SignIn(this).firebaseAuthWithGoogle(Objects.requireNonNull(account));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void openPhoneFragment() {
        try {
            PhoneNumberFragment fragment = new PhoneNumberFragment();
            fragment.setEnterTransition(new
                    Slide(Gravity.START).setDuration(200));
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
        } catch (Exception ignored) {}
    }


}
