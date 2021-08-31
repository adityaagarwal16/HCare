package com.hcare.homeopathy.hcare.NavigationItems;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

public class InviteEarnActivity extends AppCompatActivity {

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_earn);
        try {
            uid = Objects.requireNonNull(FirebaseAuth.getInstance()
                    .getCurrentUser()).getUid();
        } catch (Exception ignore) { }
    }

    String shareText() {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://hcare.com/?invitedby=" + uid))
                .setDomainUriPrefix("https://hcare.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();
        Uri dynamicLinkUri = dynamicLink.getUri();

        return
                "Download the HCare - Online Homeopathy Consultancy app using my" +
                        " link to get ₹15 added to your Wallet\n\n"
                + dynamicLinkUri.toString();
    }

    void refer() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HCare");

            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText());
            startActivity(Intent.createChooser(shareIntent, "How do you want to share"));
        } catch(Exception e) { e.printStackTrace(); }
    }

    public void Back(View view) {
        onBackPressed();
    }

    public void invite(View view) {
        refer();
    }

    void shareWhatsApp() {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareText());

        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
        }
    }

    public void whatsapp(View view) {
        shareWhatsApp();
    }
}