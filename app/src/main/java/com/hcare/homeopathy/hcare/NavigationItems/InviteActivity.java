package com.hcare.homeopathy.hcare.NavigationItems;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.hcare.homeopathy.hcare.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class InviteActivity extends AppCompatActivity {
    private final int REQUEST_Code=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        findViewById(R.id.invitebtn).setOnClickListener(v -> {
            Intent intent = new AppInviteInvitation.IntentBuilder("Hcare")
                    .setMessage("Consult a homeopathic doctor now")
                    .setDeepLink(Uri.parse("https://play.google.com/store/apps/details?id=com.hcare.homeopathy.hcare"))
                   // .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                    .setCallToActionText("invitation cta")
                    .build();
            startActivityForResult(intent, REQUEST_Code);

        });


        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, data -> {
                    if (data == null) {
                      //  Log.d(TAG, "getInvitation: no data");
                        Toast.makeText(this,"getInvitation: no data", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Get the deep link
                    Uri deepLink = data.getLink();

                    // Extract invite
                    FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
                    if (invite != null) {
                        String invitationId = invite.getInvitationId();
                    }

                    // Handle the deep link
                    // ...
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    //    Log.w(TAG, "getDynamicLink:onFailure", e);
                        Toast.makeText(InviteActivity.this,
                                "getDynamicLink:onFailure", Toast.LENGTH_LONG).show();
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        Toast.makeText(this,"onActivityResult: requestCode=" + requestCode +
                ", resultCode=" + resultCode, Toast.LENGTH_LONG).show();
        if (requestCode == REQUEST_Code && resultCode == RESULT_OK) {
            // Get the invitation IDs of all sent messages
            String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
            for (String id : ids) {
                //  Log.d(TAG, "onActivityResult: sent invitation " + id);
                Toast.makeText(this,"onActivityResult: sent invitation " + id, Toast.LENGTH_LONG).show();
            }
        }
    }
}
