package com.hcare.homeopathy.hcare.Main.Doctors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Fade;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;

import com.hcare.homeopathy.hcare.Consultations.Doctor.DoctorDetailsFragment;
import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

public class DoctorsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_details);

        setToolbar();
        showFragment();

    }
    private void showFragment() {
        String doctorID = getIntent().getStringExtra("doctorID");
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction();

                DoctorDetailsFragment fragment = new DoctorDetailsFragment();
                Bundle args = new Bundle();
                args.putString("user_id", doctorID);
                fragment.setArguments(args);

                fragment.setEnterTransition(new Fade().setDuration(300));
                fragment.setExitTransition(new Fade().setDuration(300));

                transaction.replace(R.id.frameLayout, fragment)
                        .commit();

    }

    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("About");

        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}