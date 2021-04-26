package com.hcare.homeopathy.hcare.NavigationItems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

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
        Intent profileIntent;
        switch (id) {
            case R.id.profileSettings:
                profileIntent = new Intent(context, ProfileActivity.class);
                break;
            case R.id.orders:
                profileIntent = new Intent(context, OrderActivity.class);
                break;
            case R.id.help:
                profileIntent = new Intent(context, FaqActivity.class);
                break;
            case R.id.chat:
                profileIntent = new Intent(context, DiseaseSpinnerActivity.class);
                break;
            case R.id.consultation:
                profileIntent = new Intent(context, ConsultationsActivity.class);
                break;
            default:
                profileIntent = null;
        }
        context.startActivity(profileIntent);
    }

}
