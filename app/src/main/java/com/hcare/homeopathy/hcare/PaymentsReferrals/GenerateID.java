package com.hcare.homeopathy.hcare.PaymentsReferrals;

import android.annotation.SuppressLint;

import java.util.Random;

public class GenerateID {

    @SuppressLint("DefaultLocale")
    public String getID(Service service) {
        Random random = new Random();
        int number = random.nextInt(9999999);
        if(service == Service.Order)
            return "Hcr" + String.format("%07d", number);
        else if(service == Service.Consultation)
            return "CN" + String.format("%07d", number);
        else
            return "CN" + String.format("%07d", number);
    }

}
