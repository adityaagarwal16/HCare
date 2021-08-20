package com.hcare.homeopathy.hcare.NavigationItems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.hcare.homeopathy.hcare.Consultations.AllChatsActivity;
import com.hcare.homeopathy.hcare.Main.CoronaVirusActivity;
import com.hcare.homeopathy.hcare.Main.Doctors.DoctorsActivity;
import com.hcare.homeopathy.hcare.NavigationItems.CustomerCare.HelpSupportActivity;
import com.hcare.homeopathy.hcare.Orders.AllOrdersActivity;
import com.hcare.homeopathy.hcare.R;

public class OpenNavigationItems {

    Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    Uri mInvitationUrl;
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
            default:
                intent = null;
        }
        if(intent != null)
            context.startActivity(intent);
    }

    public void refer() {

//        String link = "https://hcare.com/?invitedby=" + uid;
//        FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse(link))
//                .setDomainUriPrefix("https://hcare.page.link") // after registering on firebase, add link here
//                .setAndroidParameters(
//                        new DynamicLink.AndroidParameters.Builder("com.hcare.android")
//                                .build())
//                .setIosParameters(
//                        new DynamicLink.IosParameters.Builder("com.hcare.homeopathy.hcare.ios")
//                                .build())
//                .buildShortDynamicLink()
//                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
//                    @Override
//                    public void onSuccess(@NonNull ShortDynamicLink shortDynamicLink) {
//                        mInvitationUrl = shortDynamicLink.getShortLink();
//                        // ...
//                    }
//                });
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://hcare.com/?invitedby=" + uid))
                .setDomainUriPrefix("https://hcare.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();


        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
            String shareMessage= "Check out HCare!! India's first only Homeopathic " +
                    "online consultation and delivery app \n\n"
                      + dynamicLinkUri.toString();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "How do you want to share"));

        } catch(Exception e) { e.printStackTrace(); }
    }

}
