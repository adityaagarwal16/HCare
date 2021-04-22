package com.hcare.homeopathy.hcare.Start;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hcare.homeopathy.hcare.R;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {

    //phone no auth
    EditText MobileNumber, OTPEditview;
    Button Submit, OTPButton;
    TextView Textview, Otp,noview;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private DatabaseReference mDatabase;

    boolean mVerificationInProgress = false;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    //phone auth end

    //private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //phone auth start
        MobileNumber = findViewById(R.id.mobileNumber);
        Submit = findViewById(R.id.submit);
        OTPEditview =  findViewById(R.id.otp_editText);
        OTPButton = findViewById(R.id.otp_button);
        Textview = findViewById(R.id.textView);
        Otp = findViewById(R.id.otp);
        noview =findViewById(R.id.textView41);

        mAuth = FirebaseAuth.getInstance();

    /*    mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading ");
        mProgressDialog.setMessage("Please wait ");
        mProgressDialog.setCanceledOnTouchOutside(false);
*/

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(StartActivity.this, "verification done",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                //mProgressDialog.dismiss();
                Toast.makeText(StartActivity.this, "verification fail" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(StartActivity.this, "invalid mob no", Toast.LENGTH_LONG).show();
                    MobileNumber.setVisibility(View.VISIBLE);
                    Submit.setVisibility(View.VISIBLE);
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Toast.makeText(StartActivity.this, "quota over", Toast.LENGTH_LONG).show();
                    // [END_EXCLUDE]
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(StartActivity.this, "Verification code sent to mobile", Toast.LENGTH_LONG).show();
                //mProgressDialog.dismiss();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                MobileNumber.setVisibility(View.GONE);
                Submit.setVisibility(View.GONE);
                Textview.setVisibility(View.GONE);
                noview.setVisibility(View.GONE);
                OTPButton.setVisibility(View.VISIBLE);
                OTPEditview.setVisibility(View.VISIBLE);
                Otp.setVisibility(View.VISIBLE);
            }
        };

        Submit.setOnClickListener(v -> {
            if (!TextUtils.isEmpty( MobileNumber.getText().toString())) {
                Toast.makeText(getApplicationContext(), "hello",
                        Toast.LENGTH_SHORT).show();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + MobileNumber.getText().toString()
                        ,// Phone number to verify
                        30, TimeUnit.SECONDS,
                        // Unit of timeout
                        StartActivity.this,
                        // Activity (for callback binding)
                        mCallbacks);
                // OnVerificationStateChangedCallbacks
                //mProgressDialog.show();
            } else {
                Toast.makeText(StartActivity.this,
                        "Please enter your Number",
                        Toast.LENGTH_LONG).show();
            }
        });


        OTPButton.setOnClickListener(v -> {
            if (!TextUtils.isEmpty( MobileNumber.getText().toString())) {
                PhoneAuthCredential credential =
                        PhoneAuthProvider
                                .getCredential(mVerificationId,
                                        OTPEditview.getText().toString());
                signInWithPhoneAuthCredential(credential);
                //mProgressDialog.show();
            }else {
                Toast.makeText(StartActivity.this,
                        "Please enter OTP", Toast.LENGTH_LONG).show();
            }

        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String phone = MobileNumber.getText().toString();
                        FirebaseUser current_user = FirebaseAuth
                                .getInstance().getCurrentUser();
                        assert current_user != null;
                        String uid = current_user.getUid();
                        String device_token = FirebaseInstanceId
                                .getInstance().getToken();

                        mDatabase = FirebaseDatabase.getInstance()
                                .getReference().child("Users").child(uid);
                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("phone number", phone);
                        userMap.put("name", "patient");
                        userMap.put("age", "age of patient");
                        userMap.put("sex", "male or female");
                        userMap.put("thumb_image", "default");
                        userMap.put("image", "default");
                        userMap.put("email", "Email id");
                        userMap.put("device_token",device_token);
                        userMap.put("status","online");

                        mDatabase.setValue(userMap);

                        // Sign in success, update UI with the
                        // signed-in user's information
                        //mProgressDialog.dismiss();

                        Toast.makeText(StartActivity.this,
                                "Verification done", Toast.LENGTH_LONG).show();

                        FirebaseUser user = Objects.requireNonNull(
                                task.getResult()).getUser();

                        Intent regIntent = new Intent(
                                StartActivity.this,
                                ProfileSettingActivity.class);
                        startActivity(regIntent);
                        regIntent.addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        // Sign in failed, display a message and update the UI
                        //mProgressDialog.dismiss();

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(StartActivity.this, "Verification failed code invalid", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

//phone auth end
