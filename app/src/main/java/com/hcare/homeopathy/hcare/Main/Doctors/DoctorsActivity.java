package com.hcare.homeopathy.hcare.Main.Doctors;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.FirebaseClasses.DoctorObject;
import com.hcare.homeopathy.hcare.R;

import java.text.MessageFormat;
import java.util.Objects;


public class DoctorsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        setRecycler();
        setToolbar();
    }

    private void setRecycler() {
        try {
            Query mDoctorsDatabase =
                    FirebaseDatabase.getInstance().getReference()
                            .child("Doctors");

            RecyclerView mDoctorList = findViewById(R.id.recycler);
            mDoctorList.setLayoutManager(new LinearLayoutManager(this));
            mDoctorList.hasFixedSize();
            FirebaseRecyclerOptions<DoctorObject> options =
                    new FirebaseRecyclerOptions.Builder<DoctorObject>()
                            .setQuery(mDoctorsDatabase, DoctorObject.class)
                            .setLifecycleOwner(this)
                            .build();

            mDoctorList.setAdapter(new DoctorsAdapter(options, this));
        } catch (Exception ignored) {}
    }
    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("Doctors");

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
