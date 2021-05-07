package com.hcare.homeopathy.hcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.Consultations.ConsultationsActivity;
import com.hcare.homeopathy.hcare.NavigationItems.OpenNavigationItems;
import com.hcare.homeopathy.hcare.NavigationItems.Orders.OrdersActivity;
import com.hcare.homeopathy.hcare.NavigationItems.SetNavigationHeader;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Objects;

import static com.hcare.homeopathy.hcare.Diseases.diabetes;
import static com.hcare.homeopathy.hcare.Diseases.female;
import static com.hcare.homeopathy.hcare.Diseases.hair;
import static com.hcare.homeopathy.hcare.Diseases.men;
import static com.hcare.homeopathy.hcare.Diseases.piles;
import static com.hcare.homeopathy.hcare.Diseases.renalProblems;
import static com.hcare.homeopathy.hcare.Diseases.skin;
import static com.hcare.homeopathy.hcare.Diseases.thyroid;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mUserRef;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();
        Log.i("userID", userID);

        mUserRef =
                FirebaseDatabase.getInstance().getReference().child("Users").
                        child(userID);

        new SetNavigationHeader(this);

        //drawer and toolbar
        setToolbar();

        eventListeners();

        setFlipper();

        findViewById(R.id.cart).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, OrdersActivity.class)));

        setTopIssuesRecycler();
        setAllCategoriesRecycler();
        findViewById(R.id.searchDisease).setOnClickListener(V -> showOrHideFragment());
    }

    private void showOrHideFragment() {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction();

        Toolbar mToolbar = findViewById(R.id.toolbar);

        try {
            if (getSupportFragmentManager().findFragmentById(R.id.searchFragment) != null) {
                transaction.remove(Objects.requireNonNull
                        (getSupportFragmentManager()
                                .findFragmentById(R.id.searchFragment))).commit();
                mToolbar.animate().translationX(0);

            } else {
                SearchFragment fragment = new SearchFragment();

                fragment.setEnterTransition(new Slide(Gravity.END).setDuration(300));
                fragment.setExitTransition(new Slide(Gravity.END).setDuration(300));

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.searchFragment, fragment)
                        .commit();

                mToolbar.animate().translationX(-1 * (mToolbar.getWidth()));
            }

        } catch(Exception ignored) { }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.searchFragment) != null) {
            showOrHideFragment();
        }
        else
            super.onBackPressed();
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
        DatabaseReference publicConsult = FirebaseDatabase.getInstance().
                getReference().child("public_Consulting")
                .child(userID);


        publicConsult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final RelativeLayout consultReq = findViewById(R.id.requestText);
                if (dataSnapshot.hasChild("name"))
                    consultReq.setVisibility(View.VISIBLE);
                else
                    consultReq.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });

    }

    void setTopIssuesRecycler() {
        //top categories
        RecyclerView mRecyclerView = findViewById(R.id.topIssuesRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));

        Diseases[] list = {diabetes, thyroid, female,
                piles, men, hair, renalProblems, skin};
        mRecyclerView.setAdapter(new DiseaseAdapter(list,this));
    }

    void setAllCategoriesRecycler() {
        //top categories
        RecyclerView mRecyclerView = findViewById(R.id.allIssuesRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(new DiseaseAdapter(
                new ArrayList<>(EnumSet.allOf(Diseases.class)),this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportFragmentManager().findFragmentById(R.id.searchFragment) != null) {
            showOrHideFragment();
        }
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
        mUserRef.child("status").setValue("offline");
    }

    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(R.string.app_name);

        ((NavigationView)findViewById(R.id.navigationView)).
                setNavigationItemSelectedListener(this);

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle mDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout,
                        mToolbar, R.string.open,R.string.close);
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        new OpenNavigationItems(this, item.getItemId());
        return false;
    }

    public void consultations(View view) {
        startActivity(new Intent(this, ConsultationsActivity.class));
    }
}


