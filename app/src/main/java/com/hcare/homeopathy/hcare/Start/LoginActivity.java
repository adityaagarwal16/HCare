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
        } catch (Exception ignored) { }
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

}