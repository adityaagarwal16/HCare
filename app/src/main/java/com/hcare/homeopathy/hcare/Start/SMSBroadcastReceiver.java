package com.hcare.homeopathy.hcare.Start;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import org.apache.commons.lang.StringUtils;

import java.util.Objects;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            assert extras != null;
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch(Objects.requireNonNull(status).getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    String SMSMessage = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    assert SMSMessage != null;
                    //passing the first 6 characters of the sms to otp fragment
                    if (!SMSMessage.isEmpty()) {
                        String OTP = StringUtils.substring(SMSMessage, 0, 6);
                        Bundle otpBundle = new Bundle();
                        otpBundle.putString("SMS OTP Message", OTP);
                        OTPFragment objects = new OTPFragment();
                        objects.setArguments(otpBundle);
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // toast SMS timed out
                    Toast.makeText(context, "SMS timed out", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
