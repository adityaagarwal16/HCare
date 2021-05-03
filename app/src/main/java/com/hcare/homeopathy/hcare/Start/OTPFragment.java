package com.hcare.homeopathy.hcare.Start;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hcare.homeopathy.hcare.MainActivity;
import com.hcare.homeopathy.hcare.NavigationItems.ProfileActivity;
import com.hcare.homeopathy.hcare.R;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class OTPFragment extends Fragment {

    EditText otp;
    View root;
    boolean timerOn = false;
    Toast toastMessage;
    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_otp, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginActivity.OTP_FRAGMENT_OPEN = true;
        otp = root.findViewById(R.id.otp);

        root.findViewById(R.id.confirm).setOnClickListener(v -> {
            if (!otp.getText().toString().isEmpty()) {
                assert getArguments() != null;
                PhoneAuthCredential credential =
                        PhoneAuthProvider
                                .getCredential(
                                        Objects.requireNonNull(getArguments()
                                                .getString("verificationId")),
                                        otp.getText().toString());

                signInWithPhoneAuthCredential(credential);
            } else {
                Toast.makeText(requireActivity(),
                        "Please enter OTP", Toast.LENGTH_LONG).show();
            }

        });

        setCross();
        setCodeNotRecieved();
    }

    private void resendVerificationCode() {
        assert getArguments() != null;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Objects.requireNonNull(getArguments()
                        .getString("phoneNumber")),        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                requireActivity(),               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                LoginActivity.token);             // ForceResendingToken from callbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks
            = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) { }

        @SuppressLint("SetTextI18n")
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) { }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
        }
    };

    void setCross() {
        ImageView cross = root.findViewById(R.id.cross);

        otp.addTextChangedListener(new TextWatcher() {
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
            otp.requestFocus();
            otp.setText("");
            manager.showSoftInput(otp, 1);
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    void setCodeNotRecieved() {
        TextView codeNotRecieved = root.findViewById(R.id.otpNotReceived);
        codeNotRecieved.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                codeNotRecieved.setTextColor(requireContext().getColor(R.color.colorSecondary));
                if(!timerOn) {
                    resendVerificationCode();
                    Toast.makeText(requireActivity(),
                            "Resent OTP", Toast.LENGTH_SHORT).show();
                    timerOn = true;
                    new  CountDownTimer(30000, 1000)
                    {
                        @Override
                        public void onTick(long l) {

                        }
                        @Override
                        public void onFinish() {
                            timerOn = false;
                            //on finish the count down timer finsih
                        }

                    }.start();
                } else {
                    if (toastMessage!= null)
                        toastMessage.cancel();

                    toastMessage = Toast.makeText(requireActivity(),
                            "OTP already sent", Toast.LENGTH_SHORT);
                    toastMessage.show();
                }

                return true;
            }
            else if(event.getAction() == MotionEvent.ACTION_DOWN) {
                codeNotRecieved.setTextColor(Color.LTGRAY);
                return true;
            }
            return false;
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser current_user = FirebaseAuth
                                .getInstance().getCurrentUser();

                        String uid = Objects.requireNonNull
                                (current_user).getUid();
                        String device_token = FirebaseInstanceId
                                .getInstance().getToken();

                        mDatabase = FirebaseDatabase.getInstance()
                                .getReference().child("Users").child(uid);

                        Intent intent;
                        if(mDatabase == null) {
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("phone number", getArguments().getString("phoneNumber"));
                            userMap.put("name", "New user");
                            userMap.put("age", "");
                            userMap.put("sex", "Male");
                            userMap.put("thumb_image", "default");
                            userMap.put("image", "default");
                            userMap.put("email", "");
                            userMap.put("device_token", device_token);
                            userMap.put("status", "online");
                            mDatabase.setValue(userMap);

                            intent = new Intent(requireActivity(),
                                    MainActivity.class);
                            intent.addFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            startActivity(new Intent(requireActivity(),
                                    ProfileActivity.class));

                            requireActivity().finish();
                        }
                        else{
                            Toast.makeText(requireActivity(),
                                    "Sign in successful", Toast.LENGTH_SHORT).show();
                            intent = new Intent(requireActivity(),
                                    MainActivity.class);
                            intent.addFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(requireActivity(),
                                    "Invalid OTP", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(requireActivity(),
                                    "Verification failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}