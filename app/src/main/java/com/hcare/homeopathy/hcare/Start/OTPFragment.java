package com.hcare.homeopathy.hcare.Start;

import android.annotation.SuppressLint;
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

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hcare.homeopathy.hcare.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class OTPFragment extends Fragment {

    EditText otp;
    View root;
    boolean timerOn = false;
    Toast toastMessage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_signup_otp, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        otp = root.findViewById(R.id.otp);

        //autGetOTP fn toasts if the message has been received or not
        autoGetOTP();

        //setting the otp in the edittext
        if(getArguments()!=null) {
            String SMSotpMessage = getArguments().getString("SMS OTP Message");
            otp.setText(SMSotpMessage);
        }

        root.findViewById(R.id.confirm).setOnClickListener(v -> {
            if (!otp.getText().toString().isEmpty()) {
                assert getArguments() != null;
                PhoneAuthCredential credential =
                        PhoneAuthProvider.getCredential(
                                Objects.requireNonNull(requireArguments()
                                        .getString("verificationId")),
                                        otp.getText().toString());
                requireActivity().findViewById(R.id.circleLoader).setVisibility(View.VISIBLE);
                new SignIn(requireContext()).signInWithPhoneAuthCredential(
                        root,
                        getArguments().getString("phoneNumber"),
                        credential);

            } else
                Toast.makeText(requireActivity(),
                        "Please enter OTP", Toast.LENGTH_LONG).show();
        });

        setCross();
        setCodeNotRecieved();
    }

    private void resendVerificationCode() {
        assert getArguments() != null;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Objects.requireNonNull(requireArguments()
                        .getString("phoneNumber")),        // Phone number to verify
                30,                       // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                requireActivity(),               // Activity (for callback binding)
                mCallbacks,                      // OnVerificationStateChangedCallbacks
                PhoneNumberFragment.token);      // ForceResendingToken from callbacks
        autoGetOTP();
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

    private void autoGetOTP() {
        SmsRetrieverClient client = SmsRetriever.getClient(this.getActivity());
        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "OTP auto verified successfully", Toast.LENGTH_SHORT).show());
        task.addOnFailureListener(e -> Toast.makeText(getActivity(), "Could not auto retrieve OTP", Toast.LENGTH_SHORT).show());
    }

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
                            //on finish the count down timer finish
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
}