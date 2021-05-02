package com.hcare.homeopathy.hcare.NavigationItems;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;

import androidx.drawerlayout.widget.DrawerLayout;

import com.hcare.homeopathy.hcare.Consultations.ConsultationsActivity;
import com.hcare.homeopathy.hcare.NavigationItems.Faq.FaqActivity;
import com.hcare.homeopathy.hcare.NavigationItems.Orders.OrdersActivity;
import com.hcare.homeopathy.hcare.Disease.DiseaseSpinnerActivity;
import com.hcare.homeopathy.hcare.R;

public class OpenNavigationItems {

    Context context;
    int id;

    public OpenNavigationItems(Context context, int id) {
        this.context = context;
        this.id = id;
        open();
    }

    @SuppressLint({"NonConstantResourceId", "RtlHardcoded"})
    public void open() {
        Intent intent;
        switch (id) {
            case R.id.profileSettings:
                intent = new Intent(context, ProfileActivity.class);
                break;
            case R.id.orders:
                intent = new Intent(context, OrdersActivity.class);
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
                intent = null;
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
                    String shareMessage= "Check out HCare!! India's first only Homeopathic " +
                            "online consultation and delivery app \n\n"
                            + "https://play.google.com/store/apps/details?id=com.hcare.homeopathy.hcare";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    context.startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) { e.printStackTrace(); }
                break;

            case R.id.customerCare:
                intent = new Intent(context, CustomerCareActivity.class);
                break;

            case R.id.rateUs:
                intent = null;
                new RateUs(context);
                break;

            case R.id.logout:
                intent = null;
                ((DrawerLayout) ((Activity) context).findViewById(R.id.drawer))
                        .closeDrawer(Gravity.LEFT);
                new LogoutDialog(context);
                break;
            default:
                intent = null;
        }
        if(intent != null)
            context.startActivity(intent);
    }

}
