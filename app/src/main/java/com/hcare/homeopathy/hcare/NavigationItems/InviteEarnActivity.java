package com.hcare.homeopathy.hcare.NavigationItems;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
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
            findViewById(R.id.inviteButton).setOnClickListener(v -> refer());
        } catch (Exception ignore) { }
    }

    public void refer() {
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
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HCare");
            String shareMessage= "HCare - India's first only Homeopathic " +
                    "online consultation and delivery app \n"
                    + dynamicLinkUri.toString();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "How do you want to share"));
        } catch(Exception e) { e.printStackTrace(); }
    }

    public void Back(View view) {
        onBackPressed();
    }
}