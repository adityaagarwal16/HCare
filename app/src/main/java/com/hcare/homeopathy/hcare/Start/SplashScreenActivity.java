package com.hcare.homeopathy.hcare.Start;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hcare.homeopathy.hcare.Diseases;
import com.hcare.homeopathy.hcare.MainActivity;
import com.hcare.homeopathy.hcare.PreConsultation.CheckoutActivity;
import com.hcare.homeopathy.hcare.PreConsultation.DiseaseActivity;
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
                    String userName = Objects.requireNonNull(dataSnapshot.child("Number").getValue()).toString();

                    if (userName.equals("update2")) {
                        mAuth = FirebaseAuth.getInstance();
                        final FirebaseUser currentUser = mAuth.getCurrentUser();
                        final Thread myThread = new Thread() {
                            @Override
                            public void run() {
                                Intent intent;
                                if(currentUser == null)
                                    intent = new Intent(getApplicationContext(), StartActivity.class);
                                else {

                                    intent = new Intent(getApplicationContext(), DiseaseActivity.class);
                                    intent.putExtra("request_type1", Diseases.thyroid);
                                    startActivity(intent);

                                   /* intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                                    intent.putExtra("details1", "hello");
                                    intent.putExtra("request_type1", "Thyroid");
                                    intent.putExtra("name", "hello");
                                    intent.putExtra("age", "19");
                                    intent.putExtra("sex", "male");
*/
                                    //intent = new Intent(getApplicationContext(), MainActivity.class);
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

        NotificationManager notifManager= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notifManager.cancelAll();

    }

    private void showPopup() {
        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.updatepop,
                    findViewById(R.id.popup_1));
            PopupWindow pw = new PopupWindow(layout, AbsListView.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.MATCH_PARENT);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            layout.findViewById(R.id.updatebtn).setOnClickListener(cancel);

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
