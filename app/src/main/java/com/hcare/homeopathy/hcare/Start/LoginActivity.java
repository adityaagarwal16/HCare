package com.hcare.homeopathy.hcare.Start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.R;
import com.hcare.homeopathy.hcare.Start.Home.LoginHomeFragment;

import java.util.Objects;

public class LoginActivity extends BaseActivity {

    String prevStarted = "prevStarted";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            retrieveReferral();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
        }

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
  private void retrieveReferral() {
      FirebaseDynamicLinks.getInstance()
              .getDynamicLink(getIntent())
              .addOnSuccessListener(this, pendingDynamicLinkData -> {
                  Uri deepLink;
                  if (pendingDynamicLinkData != null) {
                      deepLink = pendingDynamicLinkData.getLink();
                      String referLink = deepLink.toString();
                      referLink = referLink.substring(referLink.lastIndexOf("%")+1);
                      String custID = referLink.substring(referLink.lastIndexOf("=")+1);

//                        cust id retrieved
//                        Toast.makeText(LoginActivity.this, custID, Toast.LENGTH_LONG).show();
              }
              });

  }

}