package com.hcare.homeopathy.hcare.Start;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcare.homeopathy.hcare.MainActivity;
import com.hcare.homeopathy.hcare.R;

import java.util.HashMap;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileSettingActivity extends AppCompatActivity {

    private TextInputLayout maName;

    private TextView maPhone_number;
    private TextInputLayout maAge;
    private TextInputLayout maEmail;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private String phone_value;

    //Firebase
    private DatabaseReference meditDatabase, loggedin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesetting);

        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mCurrentUser != null;
        String current_uid = mCurrentUser.getUid();

        meditDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        loggedin = FirebaseDatabase.getInstance().getReference().child("loggedin").child(current_uid);


        setTitle("Profile Setting");

        String name_value = getIntent().getStringExtra("name_value");
         phone_value = getIntent().getStringExtra("phone_value");
        String age_value = getIntent().getStringExtra("age_value");
        String email_value = getIntent().getStringExtra("email_value");

        maName = (TextInputLayout)findViewById(R.id.name_Input);
        maPhone_number =(TextView) findViewById(R.id.phone_input);
        maAge = (TextInputLayout)findViewById(R.id.age_input);
        maEmail = findViewById(R.id.email_input);
        radioSexGroup=(RadioGroup)findViewById(R.id.radioGroup);
        Button mSavebtn = (Button) findViewById(R.id.save_btn);

        Objects.requireNonNull(maName.getEditText()).setText(name_value);
        maPhone_number.setText(phone_value);
        Objects.requireNonNull(maAge.getEditText()).setText(age_value);
        Objects.requireNonNull(maEmail.getEditText()).setText(email_value);

        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String bname = maName.getEditText().getText().toString();
                if (!TextUtils.isEmpty(bname)) {
                        String bage = maAge.getEditText().getText().toString();
                        if (!TextUtils.isEmpty(bage)) {
                            String bemail = maEmail.getEditText().getText().toString();
                            if (!TextUtils.isEmpty(bemail)) {
                                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                                radioSexButton = (RadioButton) findViewById(selectedId);
                                String bsex = radioSexButton.getText().toString();
                                String bphone_number = maPhone_number.getText().toString();
                                Intent regIntent = new Intent(ProfileSettingActivity.this, MainActivity.class);
                                meditDatabase.child("name").setValue(bname);
                               // meditDatabase.child("phone number").setValue(bphone_number);
                                meditDatabase.child("age").setValue(bage);
                                meditDatabase.child("email").setValue(bemail);
                                meditDatabase.child("sex").setValue(bsex);


                                HashMap<String, String> notifdata = new HashMap<>();
                                notifdata.put("phone_number", phone_value);
                                notifdata.put("name", bname);
                                notifdata.put("age", bage);
                                notifdata.put("sex", bsex);
                                loggedin.setValue(notifdata);
                                regIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(regIntent);
                                finish();
                            }else {
                                Toast.makeText(ProfileSettingActivity.this,"Please enter your Email",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(ProfileSettingActivity.this,"Please enter your age",Toast.LENGTH_LONG).show();
                        }

                }else {
                    Toast.makeText(ProfileSettingActivity.this,"Please enter your name",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        meditDatabase.child("status").setValue("online");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        meditDatabase.child("status").setValue("online");
    }
}
