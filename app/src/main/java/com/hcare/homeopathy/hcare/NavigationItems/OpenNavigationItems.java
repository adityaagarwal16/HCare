package com.hcare.homeopathy.hcare.NavigationItems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.hcare.homeopathy.hcare.Consultations.AllChatsActivity;
import com.hcare.homeopathy.hcare.Main.CoronaVirusActivity;
import com.hcare.homeopathy.hcare.Main.Doctors.DoctorsActivity;
import com.hcare.homeopathy.hcare.NavigationItems.CustomerCare.HelpSupportActivity;
import com.hcare.homeopathy.hcare.Orders.AllOrdersActivity;
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
                intent = new Intent(context, AllOrdersActivity.class);
                break;
            case R.id.wallet:
                intent = new Intent(context, HCareWalletActivity.class);
                break;
            case R.id.covid:
                intent = new Intent(context, CoronaVirusActivity.class);
                break;

            case R.id.doctors:
                intent = new Intent(context, DoctorsActivity.class);
                break;

            case R.id.helpSupport:
                intent = new Intent(context, HelpSupportActivity.class);
                break;
            case R.id.consultations:
                intent = new Intent(context, AllChatsActivity.class);
                break;
            case R.id.invite:
                intent = null;
                refer();
                break;

            case R.id.rateUs:
                intent = null;
                new RateUs(context);
                break;

            case R.id.logout:
                intent = null;
                new LogoutDialog(context);
                break;

            case R.id.referAndEarn:
                intent = new Intent(context, InviteEarnActivity.class);
                break;

            default:
                intent = null;
        }
        if(intent != null)
            context.startActivity(intent);
    }

    public void refer() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
            String shareMessage= "Check out HCare!! India's first only Homeopathic " +
                    "online consultation and delivery app \n\n"
                    + "https://play.google.com/store/apps/details?id=com.hcare.homeopathy.hcare";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "How do you want to share"));
        } catch(Exception e) { e.printStackTrace(); }
    }

}
