package com.hcare.homeopathy.hcare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.core.content.pm.PackageInfoCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.Main.MainActivity;
import com.hcare.homeopathy.hcare.NewConsultation.Checkout.CheckoutActivity;
import com.hcare.homeopathy.hcare.NewConsultation.Constants;
import com.hcare.homeopathy.hcare.NewConsultation.Diseases;
import com.hcare.homeopathy.hcare.Start.LoginActivity;

import java.util.Objects;

import static com.hcare.homeopathy.hcare.NewConsultation.Constants.issue;

public class SplashScreenActivity extends BaseActivity {

    int versionCode = 0;
    boolean signInOpen = false;
    DatabaseReference versionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Context context = getApplicationContext();
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = (int) PackageInfoCompat.getLongVersionCode(info);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versionReference = FirebaseDatabase.getInstance().getReference().child("Version");
        versionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    long firebaseVersion  = (long) Objects.requireNonNull(
                            dataSnapshot.child("VersionNum").getValue());
                    if (versionCode >= firebaseVersion)
                        new Thread() {
                            @Override
                            public void run() {
                                Intent intent;
                                if (FirebaseAuth.getInstance().
                                        getCurrentUser() == null)
                                    intent = new Intent(getApplicationContext(),
                                            LoginActivity.class);
                                else {
                                    /* intent = new Intent(getApplicationContext(),
                                                    DiseaseActivity.class);
                                            intent.putExtra("request_type1", Diseases.thyroid);
                                            */
                                    intent = new Intent(getApplicationContext(),
                                            CheckoutActivity.class);
                                    intent.putExtra(Constants.DISEASE_OBJECT, Diseases.thyroid);
                                    intent.putExtra("details1", "hello");
                                    intent.putExtra(issue, "Thyroid");
                                    intent.putExtra("name", "hello");
                                    intent.putExtra("age", "19");
                                    intent.putExtra("sex", "male");
                                            
                                    /*
                                            intent = new Intent(getApplicationContext(),
                                                    ConsultationsActivity.class);

                                            intent = new Intent(getApplicationContext(),
                                                    MainDoctorActivity.class);
                                            intent.putExtra("user_id",
                                                    "AQtq6nwXN6cjsvm0GqDdB49rH8u2");

                                            intent = new Intent(getApplicationContext(),
                                                    FaqActivity.class);
                                            intent = new Intent(getApplicationContext(),
                                                    OrderNowActivity.class);
                                            intent.putExtra("price", 200);

                                            intent = new Intent(
                                                    getApplicationContext(),
                                                    AllOrdersActivity.class);
                                            intent = new Intent(getApplicationContext(),
                                                    OrderActivity.class);
                                            AllOrdersObject object =  new AllOrdersObject();
                                            object.setOrderId("Hcr409803");
                                            intent.putExtra("order", object);
                                            intent.putExtra("discount", 360);
                                            intent.putExtra("price", 600);
                                            intent = new Intent(
                                                    getApplicationContext(),
                                                    CoronaVirusActivity.class);*/
                                    intent = new Intent(
                                            getApplicationContext(),
                                            MainActivity.class);
                                   /* intent = new Intent(
                                            getApplicationContext(),
                                            DoctorsActivity.class);
                                    intent.putExtra("doctorID",
                                            "AQtq6nwXN6cjsvm0GqDdB49rH8u2");*/

                                }
                                startActivity(intent);
                                signInOpen = true;
                                finish();
                            }
                        }.start();
                    else
                        showPopup();
                } catch(Exception e) {
                    e.printStackTrace();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(!signInOpen) {
                    startActivity(new Intent(getApplicationContext(),
                            LoginActivity.class));
                    finish();
                }
            }

        });
    }

    private void showPopup() {
        try {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") View layout =
                    inflater.inflate(R.layout.dialog_update_app, null);
            PopupWindow pw = new PopupWindow(layout, AbsListView.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.MATCH_PARENT);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            versionReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean crossVisible;
                    try {
                        crossVisible = (Boolean) Objects.requireNonNull(
                                dataSnapshot.child("crossVisible").getValue());
                    } catch(Exception e) {crossVisible = true;}
                    if(crossVisible)
                        layout.findViewById(R.id.close).setVisibility(View.VISIBLE);
                    else
                        layout.findViewById(R.id.close).setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }

            });


            layout.findViewById(R.id.updateButton).setOnClickListener(v -> {
                Intent updateIntent =new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" +
                                "com.hcare.homeopathy.hcare&hl=en"));
                startActivity(updateIntent);
            });

            layout.findViewById(R.id.close).setOnClickListener(v -> {
                startActivity(new Intent(getApplicationContext(),
                        MainActivity.class));
                finish();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
