package com.hcare.homeopathy.hcare.NavigationItems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hcare.homeopathy.hcare.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FaqActivity extends AppCompatActivity {
    private TextView qus1,qus2,qus3,qus4,qus5,qus6,qus7,qus8,qus9,qus10,qus11,qus12,qus13,qus14,qus15,qus16,qus17,qus18;
    private TextView ans1,ans2,ans3,ans4,ans5,ans6,ans7,ans8,ans9,ans10,ans11,ans12,ans13,ans14,ans15,ans16,ans17;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        setTitle("Help and support");

        qus1 =(TextView)findViewById(R.id.qus1);
        qus2 =(TextView)findViewById(R.id.qus2);
        qus3 =(TextView)findViewById(R.id.qus3);
        qus4 =(TextView)findViewById(R.id.qus4);
        qus5 =(TextView)findViewById(R.id.qus5);
        qus6 =(TextView)findViewById(R.id.qus6);
        qus7 =(TextView)findViewById(R.id.qus7);
        qus8 =(TextView)findViewById(R.id.qus8);
        qus9 =(TextView)findViewById(R.id.qus9);
        qus10 =(TextView)findViewById(R.id.qus10);
        qus11 =(TextView)findViewById(R.id.qus11);
        qus12 =(TextView)findViewById(R.id.qus12);
        qus13 =(TextView)findViewById(R.id.qus13);
        qus14 =(TextView)findViewById(R.id.qus14);
        qus15 =(TextView)findViewById(R.id.qus15);
        qus16 =(TextView)findViewById(R.id.qus16);
        qus17 =(TextView)findViewById(R.id.qus17);
        qus18 =(TextView)findViewById(R.id.qus18);

        ans1 =(TextView)findViewById(R.id.ans1);
        ans2 =(TextView)findViewById(R.id.ans2);
        ans3 =(TextView)findViewById(R.id.ans3);
        ans4 =(TextView)findViewById(R.id.ans4);
        ans5 =(TextView)findViewById(R.id.ans5);
        ans6 =(TextView)findViewById(R.id.ans6);
        ans7 =(TextView)findViewById(R.id.ans7);
        ans8 =(TextView)findViewById(R.id.ans8);
        ans9 =(TextView)findViewById(R.id.ans9);
        ans10 =(TextView)findViewById(R.id.ans10);
        ans11 =(TextView)findViewById(R.id.ans11);
        ans12 =(TextView)findViewById(R.id.ans12);
        ans13 =(TextView)findViewById(R.id.ans13);
        ans14 =(TextView)findViewById(R.id.ans14);
        ans15 =(TextView)findViewById(R.id.ans15);
        ans16 =(TextView)findViewById(R.id.ans16);
        ans17 =(TextView)findViewById(R.id.ans17);


        qus1.setOnClickListener(v -> {
            ans1.setVisibility(View.VISIBLE);
            ans2.setVisibility(View.GONE);
            ans3.setVisibility(View.GONE);
            ans4.setVisibility(View.GONE);
            ans5.setVisibility(View.GONE);
            ans6.setVisibility(View.GONE);
            ans7.setVisibility(View.GONE);
            ans8.setVisibility(View.GONE);
            ans9.setVisibility(View.GONE);
            ans10.setVisibility(View.GONE);
            ans11.setVisibility(View.GONE);
            ans12.setVisibility(View.GONE);
            ans13.setVisibility(View.GONE);
            ans14.setVisibility(View.GONE);
            ans15.setVisibility(View.GONE);
            ans16.setVisibility(View.GONE);
            ans17.setVisibility(View.GONE);



        });
        qus2.setOnClickListener(v -> {
            ans1.setVisibility(View.GONE);
            ans2.setVisibility(View.VISIBLE);
            ans3.setVisibility(View.GONE);
            ans4.setVisibility(View.GONE);
            ans5.setVisibility(View.GONE);
            ans6.setVisibility(View.GONE);
            ans7.setVisibility(View.GONE);
            ans8.setVisibility(View.GONE);
            ans9.setVisibility(View.GONE);
            ans10.setVisibility(View.GONE);
            ans11.setVisibility(View.GONE);
            ans12.setVisibility(View.GONE);
            ans13.setVisibility(View.GONE);
            ans14.setVisibility(View.GONE);
            ans15.setVisibility(View.GONE);
            ans16.setVisibility(View.GONE);
            ans17.setVisibility(View.GONE);



        });
        qus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.VISIBLE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.VISIBLE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.VISIBLE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.VISIBLE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.VISIBLE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.VISIBLE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.VISIBLE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.VISIBLE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.VISIBLE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.VISIBLE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.VISIBLE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.VISIBLE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.VISIBLE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.VISIBLE);
                ans17.setVisibility(View.GONE);
                 


            }
        });
        qus17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ans1.setVisibility(View.GONE);
                ans2.setVisibility(View.GONE);
                ans3.setVisibility(View.GONE);
                ans4.setVisibility(View.GONE);
                ans5.setVisibility(View.GONE);
                ans6.setVisibility(View.GONE);
                ans7.setVisibility(View.GONE);
                ans8.setVisibility(View.GONE);
                ans9.setVisibility(View.GONE);
                ans10.setVisibility(View.GONE);
                ans11.setVisibility(View.GONE);
                ans12.setVisibility(View.GONE);
                ans13.setVisibility(View.GONE);
                ans14.setVisibility(View.GONE);
                ans15.setVisibility(View.GONE);
                ans16.setVisibility(View.GONE);
                ans17.setVisibility(View.VISIBLE);
                 


            }
        });
   
        qus18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(FaqActivity.this, CustomercareActivity.class);
                startActivity(docprofileIntent);
            }
        });


    }
}
