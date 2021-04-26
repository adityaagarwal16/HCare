package com.hcare.homeopathy.hcare.NavigationItems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.hcare.homeopathy.hcare.Consultations.ConsultationsActivity;
import com.hcare.homeopathy.hcare.NavigationItems.Faq.FaqActivity;
import com.hcare.homeopathy.hcare.PostConsultation.OrderActivity;
import com.hcare.homeopathy.hcare.PreConsultation.DiseaseSpinnerActivity;
import com.hcare.homeopathy.hcare.R;

public class OpenNavigationItems {

    Context context;
    int id;

    public OpenNavigationItems(Context context, int id) {
        this.context = context;
        this.id = id;
        open();
    }

    @SuppressLint("NonConstantResourceId")
    public void open() {
        Intent intent;
        switch (id) {
            case R.id.profileSettings:
                intent = new Intent(context, ProfileActivity.class);
                break;
            case R.id.orders:
                intent = new Intent(context, OrderActivity.class);
                break;
            case R.id.help:
                intent = new Intent(context, FaqActivity.class);
                break;
            case R.id.chat:
                intent = new Intent(context, DiseaseSpinnerActivity.class);
                break;
            case R.id.consultations:
                intent = new Intent(context, ConsultationsActivity.class);
                break;
            case R.id.invite:
                intent = new Intent(context, InviteActivity.class);
                break;
            case R.id.customerCare:
                intent = new Intent(context, CustomercareActivity.class);
                break;
            default:
                intent = null;
        }
        context.startActivity(intent);
    }

}
