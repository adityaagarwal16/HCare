package com.hcare.homeopathy.hcare.Start;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hcare.homeopathy.hcare.R;

import java.util.concurrent.TimeUnit;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class PhoneNumberFragment extends Fragment {

    EditText phoneNumber;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_phone_number, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginActivity.OTP_FRAGMENT_OPEN = false;
        phoneNumber = root.findViewById(R.id.phoneNumber);
        phoneNumber.setText("9876543210");

        submitButton();
        setCross();
    }

    private void submitButton() {
        root.findViewById(R.id.submit).setOnClickListener(v -> {
            if (phoneNumber.getText().length() == 10) {

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + phoneNumber.getText().toString(),
                        10,
                        TimeUnit.SECONDS,
                        requireActivity(),
                        mCallbacks);

                root.findViewById(R.id.otpFailedLayout).setVisibility(View.GONE);
                root.findViewById(R.id.loader).setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(requireActivity(),
                        "Please enter 10 digit Phone number",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    void setCross() {
        ImageView cross = root.findViewById(R.id.cross);
        if(phoneNumber.getText().length() != 0)
            cross.setVisibility(View.VISIBLE);

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(""))
                    cross.setVisibility(View.GONE);
                else
                    cross.setVisibility(View.VISIBLE);
            }
        });

        cross.setOnClickListener(v -> {
            final InputMethodManager manager = (InputMethodManager)
                    requireContext().getSystemService(INPUT_METHOD_SERVICE);
            phoneNumber.requestFocus();
            phoneNumber.setText("");
            manager.showSoftInput(phoneNumber, 1);
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks
            = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) { }

        @SuppressLint("SetTextI18n")
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            TextView otpFailedLayout =  root.findViewById(R.id.otpFailedText);
            if (e instanceof FirebaseAuthInvalidCredentialsException)
                otpFailedLayout.setText("INVALID NUMBER");
            else
                otpFailedLayout.setText("OTP FAILED");
            root.findViewById(R.id.otpFailedLayout).setVisibility(View.VISIBLE);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            root.findViewById(R.id.otpSentLayout).setVisibility(View.VISIBLE);

            LoginActivity.token = token;

            OTPFragment fragment = new OTPFragment();
            Bundle args = new Bundle();
            args.putString("phoneNumber", phoneNumber.getText().toString());
            args.putString("verificationId", verificationId);
            fragment.setArguments(args);

            fragment.setEnterTransition(new Slide(Gravity.END).setDuration(200));

            new Handler(Looper.getMainLooper()).postDelayed(() ->
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, fragment)
                            .commit(), 700);
        }
    };
}