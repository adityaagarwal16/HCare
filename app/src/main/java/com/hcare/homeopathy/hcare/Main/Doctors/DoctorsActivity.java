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

    int DOCTORS_TO_DISPLAY = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setRecycler();
    }

    private void setRecycler() {
        try {
            Query mDoctorsDatabase =
                    FirebaseDatabase.getInstance().getReference()
                            .child("Doctors").orderByChild("experience").limitToLast(DOCTORS_TO_DISPLAY);

            RecyclerView mDoctorList = findViewById(R.id.recycler);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

            mDoctorList.setLayoutManager(layoutManager);
            mDoctorList.hasFixedSize();
            FirebaseRecyclerOptions<DoctorObject> options =
                    new FirebaseRecyclerOptions.Builder<DoctorObject>()
                            .setQuery(mDoctorsDatabase, DoctorObject.class)
                            .setLifecycleOwner(this)
                            .build();

            mDoctorList.setAdapter(new DoctorsAdapter(options, this));
            findViewById(R.id.loader).setVisibility(View.GONE);
        } catch (Exception ignored) {
            findViewById(R.id.loader).setVisibility(View.GONE);
        }
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

