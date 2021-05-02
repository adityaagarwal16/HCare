package com.hcare.homeopathy.hcare.NavigationItems;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

public class RateUs {

    Context context;

    public RateUs(Context context) {
        this.context = context;
        createDialog();
    }

    void createDialog() {
        try {
            ReviewManager manager = ReviewManagerFactory.create(context);
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // We can get the ReviewInfo object
                    Task<Void> flow = manager.launchReviewFlow((Activity) context, task.getResult());
                    flow.addOnCompleteListener(task1 -> { });
                }
                else
                    openPlayStore();
            });
        } catch (Exception e) {
            openPlayStore();
        }
    }

    void openPlayStore() {
        try {
            final String appPackageName = context.getPackageName();
            try {
                context.startActivity
                        (new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        } catch (Exception ex) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
