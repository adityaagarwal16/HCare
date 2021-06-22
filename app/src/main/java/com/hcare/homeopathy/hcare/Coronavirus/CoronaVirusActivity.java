package com.hcare.homeopathy.hcare.Coronavirus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

import static com.hcare.homeopathy.hcare.Coronavirus.Boosters.a;
import static com.hcare.homeopathy.hcare.Coronavirus.Boosters.b;
import static com.hcare.homeopathy.hcare.Coronavirus.Boosters.c;
import static com.hcare.homeopathy.hcare.Coronavirus.Boosters.d;



public class CoronaVirusActivity extends AppCompatActivity {

    BoosterInfo booster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona_virus);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        booster = new BoosterInfo((Boosters)
                getIntent().getSerializableExtra("request_type1"));
        setUpRecyclerView();
        setContent();
    }

    private void setContent() {
        ((TextView) findViewById(R.id.aboutBooster)).setText(booster.getInfo());
    }

    public void setUpRecyclerView(){
        RecyclerView mRecyclerView = findViewById(R.id.CovidRV);

        Boosters[] list = {a,b,c,d};
        mRecyclerView.setAdapter(new ImmunityBoosterAdapter(list,this));
    }

}