package com.hcare.homeopathy.hcare.Start;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.Consultations.ChatActivity;
import com.hcare.homeopathy.hcare.MainActivity;
import com.hcare.homeopathy.hcare.Consultations.ConsultationsActivity;
import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        DatabaseReference updatepop = FirebaseDatabase.getInstance().getReference().child("Version");
        updatepop.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Number")){
                    String updateVersion =
                            Objects.requireNonNull(dataSnapshot.child("Number").getValue()).toString();

                    if (updateVersion.equals("update2")) {
                        mAuth = FirebaseAuth.getInstance();
                        final FirebaseUser currentUser = mAuth.getCurrentUser();
                        final Thread myThread = new Thread() {
                            @Override
                            public void run() {
                                Intent intent;
                                if(currentUser == null)
                                    intent = new Intent(getApplicationContext(), StartActivity.class);
                                else {

                                    /*intent = new Intent(getApplicationContext(), DiseaseActivity.class);
                                    intent.putExtra("request_type1", Diseases.thyroid);
                                    startActivity(intent);
*/
                                    /*intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                                    intent.putExtra(Constants.DISEASE_OBJECT, Diseases.thyroid);
                                    intent.putExtra("details1", "hello");
                                    intent.putExtra("request_type1", "Thyroid");
                                    intent.putExtra("name", "hello");
                                    intent.putExtra("age", "19");
                                    intent.putExtra("sex", "male");*/

                                    intent = new Intent(getApplicationContext(), MainActivity.class);

                                    intent = new Intent(getApplicationContext(), ConsultationsActivity.class);

                                    intent = new Intent(getApplicationContext(), ChatActivity.class);
                                    intent.putExtra("user_id", "AQtq6nwXN6cjsvm0GqDdB49rH8u2");

                                    //intent = new Intent(getApplicationContext(), FaqActivity.class);
                                    //intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                }
                                startActivity(intent);
                                finish();
                            }
                        };
                        myThread.start();
                    } else {
                        showPopup();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });

        NotificationManager notifyManager=
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notifyManager.cancelAll();

    }

    private void showPopup() {
        try {

            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") View layout =
                    inflater.inflate(R.layout.update_popup, null);
            PopupWindow pw = new PopupWindow(layout, AbsListView.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.MATCH_PARENT);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            layout.findViewById(R.id.updateButton).setOnClickListener(cancel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final View.OnClickListener cancel = v -> {
        Intent updateIntent =new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.hcare.homeopathy.hcare&hl=en"));
        startActivity(updateIntent);
        //  pw.dismiss();
    };

    @Override
    public void onBackPressed() {

    }
}
