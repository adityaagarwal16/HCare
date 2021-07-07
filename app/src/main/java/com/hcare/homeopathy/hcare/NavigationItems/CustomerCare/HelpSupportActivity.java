package com.hcare.homeopathy.hcare.NavigationItems.CustomerCare;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.NavigationItems.CustomerCare.Faq.FaqActivity;
import com.hcare.homeopathy.hcare.R;

public class HelpSupportActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void Back(View view) {
        onBackPressed();
    }

    public void customerCareCard(View view) {
        startActivity(new Intent(this, CustomerCareActivity.class));
    }

    public void faqCard(View view) {
        startActivity(new Intent(this, FaqActivity.class));
    }

    public void whatsappCard(View view) {
        String phoneNum = "+91 8296903274";
        String url = "https://api.whatsapp.com/send?phone=" + phoneNum;
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch(Exception e1) {
                Toast.makeText(this ,
                        "WhatsApp app not installed in your phone", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }

    public void callCard(View view) {
        String phoneNum = "+91 79704 76060";
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
            intent.setData(Uri.parse("tel:"+phoneNum));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this,
                    "Unable to call at this time", Toast.LENGTH_SHORT).show();
        }
    }

}
