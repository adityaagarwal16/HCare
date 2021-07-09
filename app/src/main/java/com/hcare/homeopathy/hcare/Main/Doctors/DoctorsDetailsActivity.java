package com.hcare.homeopathy.hcare.Main.Doctors;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.hcare.homeopathy.hcare.FirebaseClasses.DoctorObject;
import com.hcare.homeopathy.hcare.R;

import java.text.MessageFormat;
import java.util.Objects;

public class DoctorsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_details);

        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        showFragment();
    }

    private void showFragment() {
        DoctorObject doctor = (DoctorObject) getIntent().getSerializableExtra("doctor");
        try {
            setTitle(MessageFormat.format("Dr. {0}", Objects.requireNonNull(doctor).getName()));
        } catch(Exception ignored) {}
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        DoctorDetailsFragment fragment = new DoctorDetailsFragment();
        Bundle args = new Bundle();
        args.putString("user_id", Objects.requireNonNull(doctor).getDoctorID());
        fragment.setArguments(args);

        transaction.replace(R.id.frameLayout, fragment)
                .commit();
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
