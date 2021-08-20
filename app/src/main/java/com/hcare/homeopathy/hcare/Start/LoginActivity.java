package com.hcare.homeopathy.hcare.Start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.R;
import com.hcare.homeopathy.hcare.Start.Home.LoginHomeFragment;

import java.util.Objects;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new LoginHomeFragment())
                    .commit();
        } catch (Exception ignored) {
        }
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

  /*  public void carouselView() {
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
    };*/

}