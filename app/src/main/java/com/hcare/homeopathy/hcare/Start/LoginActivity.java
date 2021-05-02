package com.hcare.homeopathy.hcare.Start;

import android.os.Bundle;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Slide;

import com.google.firebase.auth.PhoneAuthProvider;
import com.hcare.homeopathy.hcare.R;

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

    void openPhoneFragment() {
        PhoneNumberFragment fragment = new PhoneNumberFragment();
        fragment.setEnterTransition(new Slide(Gravity.START).setDuration(200));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}