package com.hcare.homeopathy.hcare.Start;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import androidx.transition.Slide;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.Objects;

public class LoginActivity extends BaseActivity {

//    public static boolean OTP_FRAGMENT_OPEN = false;
//    public static PhoneAuthProvider.ForceResendingToken token;
    CarouselView customCarouselView;
    int carousel_Num_of_pages = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            getSupportFragmentManager()
                    .beginTransaction()
//                    .replace(R.id.frameLayout, new PhoneNumberFragment())
                    .replace(R.id.frameLayout, new LoginHomeFragment())
                    .commit();
        } catch (Exception ignored) {}
        carouselView();

    }

//    @Override
//    public void onBackPressed() {
//        if(OTP_FRAGMENT_OPEN)
//            openPhoneFragment();
//        else
//            super.onBackPressed();
//    }

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

//    void openPhoneFragment() {
//        try {
//            PhoneNumberFragment fragment = new PhoneNumberFragment();
//            fragment.setEnterTransition(new
//                    Slide(Gravity.START).setDuration(200));
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.frameLayout, fragment)
//                    .commit();
//        } catch (Exception ignored) {}
//    }

    public void carouselView() {
        customCarouselView = (CarouselView) findViewById(R.id.carouselView);
        customCarouselView.setPageCount(carousel_Num_of_pages);
        customCarouselView.setViewListener(viewListener);
    }
    ViewListener viewListener = position -> {
        View customView;
        if(position == 0){
            customView = getLayoutInflater().inflate(R.layout.login_carousel_1, null);
        }
        else if(position == 1) {
            customView = getLayoutInflater().inflate(R.layout.login_carousel_2, null);
        }
        else{
            customView = getLayoutInflater().inflate(R.layout.login_carousel_3, null);
        }
        return customView;
    };

}