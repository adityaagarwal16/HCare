package com.hcare.homeopathy.hcare.Start;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.hcare.homeopathy.hcare.R;

public class LoginHomeFragment extends Fragment {

    EditText phoneNumber;
    View root;
    public static final int RC_SIGN_IN = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_login_home_ph_number, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        phoneNumber = root.findViewById(R.id.phoneNumber);
        phoneNumber.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setClass(requireActivity(), PhoneNumberVerificationActivity.class);
            startActivity(intent);
        });

        root.findViewById(R.id.googleSignIn).setOnClickListener
                (v -> signInWithGoogle());

    }

    protected void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient =
                GoogleSignIn.getClient(requireContext(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        root.findViewById(R.id.circleLoader).setVisibility(View.VISIBLE);
    }
}
