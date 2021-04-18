package com.hcare.homeopathy.hcare.Mainmenus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.PostConsultation.OrderActivity;
import com.hcare.homeopathy.hcare.PreConsultation.DiseaseSpinnerActivity;
import com.hcare.homeopathy.hcare.R;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUserRef =
                FirebaseDatabase.getInstance().getReference().child("Users").
                        child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        //drawer and toolbar
        setToolbar();

        eventListeners();

        setFlipper();


        findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(docprofileIntent);
            }
        });

        findViewById(R.id.mainImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(MainActivity.this, DiseaseSpinnerActivity.class);
                startActivity(docprofileIntent);
            }
        });

        setTopIssuesRecycler();
        setAllCategoriesRecycler();
    }

    void setFlipper() {

        int[] images = {
                R.drawable.ban1,
                R.drawable.ban2,
                R.drawable.ban3,
                R.drawable.ban4,
                R.drawable.ban5
        };

        ViewFlipper mFlipper = findViewById(R.id.imageView9);

        for (int image: images) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(image);

            mFlipper.addView(imageView);
            mFlipper.setFlipInterval(4000);
            mFlipper.setAutoStart(true);

            mFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
            mFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        }
    }


    void eventListeners() {
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                //setTitle("Hi "+ name.substring(0, 1).toUpperCase() + name.substring(1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference publicConsult = FirebaseDatabase.getInstance().
                getReference().child("public_Consulting").child(mAuth.getCurrentUser().getUid());


        final TextView consultReq = findViewById(R.id.reqtxt);
        consultReq.setVisibility(View.GONE);


        publicConsult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("name")) {
                    consultReq.setVisibility(View.VISIBLE);
                }else {
                    consultReq.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void setTopIssuesRecycler() {
        //top categories
        RecyclerView mRecyclerView = findViewById(R.id.topIssuesRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<DiseaseObject> list = new ArrayList<>();
        list.add(new DiseaseObject("Diabetes", R.drawable.diabetes));
        list.add(new DiseaseObject("Thyroid", R.drawable.thyroid));
        list.add(new DiseaseObject("Female", R.drawable.female));
        list.add(new DiseaseObject("Piles", R.drawable.newpiles));
        list.add(new DiseaseObject("Mens", R.drawable.newmens));
        list.add(new DiseaseObject("Hair", R.drawable.newhair));
        list.add(new DiseaseObject("Renal stones", R.drawable.newrenal));
        list.add(new DiseaseObject("Skin", R.drawable.newacne));


        //on click listener set in adapter
        mRecyclerView.setAdapter(new DiseaseAdapter(list,this));
    }

    void setAllCategoriesRecycler() {
        //top categories
        RecyclerView mRecyclerView = findViewById(R.id.allIssuesRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<DiseaseObject> list = new ArrayList<>();
        list.add(new DiseaseObject("Skin", R.drawable.skin));
        list.add(new DiseaseObject("Renal Problems", R.drawable.renal));
        list.add(new DiseaseObject("Weight Loss & gain", R.drawable.weight));
        list.add(new DiseaseObject("Headache", R.drawable.newheadache));
        list.add(new DiseaseObject("Hair", R.drawable.newhair));
        list.add(new DiseaseObject("mens", R.drawable.newmens));
        list.add(new DiseaseObject("Diabetes", R.drawable.newrenal));
        list.add(new DiseaseObject("Piles", R.drawable.newacne));
        list.add(new DiseaseObject("Thyroid", R.drawable.newacne));
        list.add(new DiseaseObject("Respiratory problems", R.drawable.newrespiratory));
        list.add(new DiseaseObject("Bones & joints", R.drawable.newjoints));
        list.add(new DiseaseObject("Depression", R.drawable.newdepression));
        list.add(new DiseaseObject("Growth", R.drawable.newgrowth));
        list.add(new DiseaseObject("Heart Problems", R.drawable.newheart));
        list.add(new DiseaseObject("ENT", R.drawable.newent));
        list.add(new DiseaseObject("Mouth & Teeth", R.drawable.newrenal));
        list.add(new DiseaseObject("Children", R.drawable.newacne));
        list.add(new DiseaseObject("Nutrition & health", R.drawable.newacne));
        list.add(new DiseaseObject("Maternal", R.drawable.newmetarnal));
        list.add(new DiseaseObject("Others", R.drawable.newothers));


        //on click listener set in adapter
        mRecyclerView.setAdapter(new DiseaseAdapter(list,this));

    }

    @Override
    public void onStart() {
        super.onStart();
        ///phone authentication
        mUserRef.child("status").setValue("online");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            mUserRef.child("status").setValue("offline");
        }

    }

    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(R.string.app_name);

        ((NavigationView)findViewById(R.id.navigationView)).
                setNavigationItemSelectedListener(this);

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent profileIntent;
        switch (item.getItemId()) {
            case R.id.profileSettings:
                profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                break;
            case R.id.orders:
                profileIntent = new Intent(MainActivity.this, OrderActivity.class);
                break;
            case R.id.help:
                profileIntent = new Intent(MainActivity.this, FaqActivity.class);
                break;
            case R.id.chat:
                profileIntent = new Intent(MainActivity.this, DiseaseSpinnerActivity.class);
                break;
            case R.id.consultation:
                profileIntent = new Intent(MainActivity.this, RecentmainmenuActivity.class);
                break;
            default:
                profileIntent = null;
        }
        startActivity(profileIntent);
        return false;
    }

}


