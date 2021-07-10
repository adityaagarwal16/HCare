package com.hcare.homeopathy.hcare.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.transition.Explode;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hcare.homeopathy.hcare.Consultations.ConsultationsActivity;
import com.hcare.homeopathy.hcare.FirebaseClasses.DoctorObject;
import com.hcare.homeopathy.hcare.Main.Doctors.DoctorsActivity;
import com.hcare.homeopathy.hcare.Main.Doctors.LimitedDoctorsAdapter;
import com.hcare.homeopathy.hcare.NavigationItems.OpenNavigationItems;
import com.hcare.homeopathy.hcare.NavigationItems.SetNavigationHeader;
import com.hcare.homeopathy.hcare.NewConsultation.DiseaseAdapter;
import com.hcare.homeopathy.hcare.NewConsultation.Diseases;
import com.hcare.homeopathy.hcare.Orders.AllOrdersActivity;
import com.hcare.homeopathy.hcare.R;
import com.hcare.homeopathy.hcare.BaseActivity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.hcare.homeopathy.hcare.NewConsultation.Diseases.diabetes;
import static com.hcare.homeopathy.hcare.NewConsultation.Diseases.female;
import static com.hcare.homeopathy.hcare.NewConsultation.Diseases.hair;
import static com.hcare.homeopathy.hcare.NewConsultation.Diseases.men;
import static com.hcare.homeopathy.hcare.NewConsultation.Diseases.piles;
import static com.hcare.homeopathy.hcare.NewConsultation.Diseases.renalProblems;
import static com.hcare.homeopathy.hcare.NewConsultation.Diseases.skin;
import static com.hcare.homeopathy.hcare.NewConsultation.Diseases.thyroid;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.activeConsultations;

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

        final String[] token = {""};
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isComplete()){
                token[0] = task.getResult();
                Log.i("AppConstants", "onComplete: new Token got: "+token[0] );
            }
        });

        mUserRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userID);

        new SetNavigationHeader(this);

        //drawer and toolbar
        setToolbar();
        eventListeners();
        setFlipper();
        setCoronaFlipper();

        findViewById(R.id.cart).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AllOrdersActivity.class)));

        setTopIssuesRecycler();
        setDoctorsRecycler();
        setAllCategoriesRecycler();
        findViewById(R.id.searchDisease).setOnClickListener(V -> showOrHideFragment());
        updateUserDetails();
    }

    private void updateUserDetails() {
        final String[] token = {""};
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isComplete()){
                token[0] = task.getResult();
            }
        });

        FirebaseDatabase.getInstance()
                .getReference().child("Users").child(userID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            try{
                                if(!Objects.requireNonNull(
                                        dataSnapshot.child("device_token").getValue())
                                        .toString().equals(token[0]))
                                    updateFirebase(dataSnapshot, token[0]);
                            } catch (Exception e) {
                                updateFirebase(dataSnapshot, token[0]);
                                e.printStackTrace();
                            }

                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

    }

    private void updateFirebase(DataSnapshot dataSnapshot, String token) {
        Map userMap = new HashMap();
        userMap.put("phone number", Objects.requireNonNull(dataSnapshot.child("phone number")
                .getValue()));
        userMap.put("name", Objects.requireNonNull(dataSnapshot.child("name")
                .getValue()));
        userMap.put("age", Objects.requireNonNull(dataSnapshot.child("age")
                .getValue()));
        userMap.put("sex", Objects.requireNonNull(dataSnapshot.child("sex")
                .getValue()));
        userMap.put("email", Objects.requireNonNull(dataSnapshot.child("email")
                .getValue()));
        try {
            userMap.put("image", Objects.requireNonNull(dataSnapshot
                    .child("image").getValue()));
        } catch (Exception ignored) { }
        try {
            userMap.put("consultCount", Objects.requireNonNull(dataSnapshot
                    .child("consultCount").getValue()));
        } catch (Exception ignored) { }

        userMap.put("device_token", token);
        userMap.put("status", "online");

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(userID)
                .setValue(userMap);
    }
    /*
    private void consultations() {
        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference messages =
                rootReference.child("messages").child(userID);

        messages.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Log.i("keys", snapshot.toString());
                    String key = String.valueOf(
                            new JSONObject(snapshot);

                    Toast.makeText(getApplicationContext(),key, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                messages.keepSynced(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
*/

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

                fragment.setEnterTransition(new Explode().setDuration(200));
                fragment.setExitTransition(new Explode().setDuration(200));

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
            mFlipper.setFlipInterval(3000);
            mFlipper.setAutoStart(true);

            mFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
            mFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        }
    }

    void setCoronaFlipper() {
        int[] images = {
                R.drawable.corona_banner_1,
                R.drawable.corona_banner_2,
                R.drawable.corona_banner_3
        };

        ViewFlipper mFlipper = findViewById(R.id.imageView10);
        findViewById(R.id.coronaBanner).setOnClickListener(v-> startActivity(new Intent(
                MainActivity.this,
                CoronaVirusActivity.class)));


        for (int image: images) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(image);
            imageView.setAdjustViewBounds(true);

            mFlipper.addView(imageView);

            mFlipper.setFlipInterval(3500);
            mFlipper.setAutoStart(true);

            mFlipper.setOutAnimation(this,android.R.anim.fade_out);
            mFlipper.setInAnimation(this,android.R.anim.fade_in);
        }
    }

    void eventListeners() {
        final String[] consultationID = {""};
        DatabaseReference publicConsult = FirebaseDatabase.getInstance().
                getReference().child(activeConsultations)
                .child(userID);

        FirebaseDatabase.getInstance().
                getReference().child("Consultations")
                .child(userID).orderByKey().limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            try {
                                for (DataSnapshot supportItem: snapshot.getChildren()) {
                                    consultationID[0] = String.valueOf(
                                            supportItem.child("consultationID").getValue());
                                }
                            } catch(Exception ignored) {}
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        publicConsult.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final RelativeLayout consultReq = findViewById(R.id.requestText);
                final TextView consultationText = findViewById(R.id.consultationText);
                consultationText.setText(MessageFormat
                        .format("You will be contacted by our doctor." +
                                "\nConsultation ID : {0}", consultationID[0]));
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

        Diseases[] list = {diabetes, thyroid, renalProblems, female, men,
                piles, skin, hair};
        mRecyclerView.setAdapter(new DiseaseAdapter(list,this));
    }

    void setDoctorsRecycler() {
        //Know your doctors
        try {
            ArrayList<DoctorObject> arrayList = new ArrayList<>();

            DatabaseReference mDoctorsDatabase =
                    FirebaseDatabase.getInstance().getReference()
                            .child("Doctors");

            RecyclerView mDoctorList = findViewById(R.id.doctorsRecycler);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false);
            mDoctorList.setLayoutManager(linearLayoutManager);
            mDoctorList.hasFixedSize();
            mDoctorList.setNestedScrollingEnabled(false);
            String[] list = {"sCBWYaI75xZmIk8fXIaxFBJ4v2s2",
                    "ZwthiKA5aDaXf6DNYVWVinzm0XP2", "w7sQhwsRFjN7sXBKt0Fy0p65r4o1", "viewMore"};

            LimitedDoctorsAdapter adapter = new LimitedDoctorsAdapter(arrayList, this, list);
            for (String s : list) {
                mDoctorsDatabase.child(s).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            DoctorObject obj = dataSnapshot.getValue(DoctorObject.class);
                            if(!s.equals("viewMore"))
                                Objects.requireNonNull(obj).setDoctorID(s);
                            arrayList.add(obj);
                            adapter.notifyDataSetChanged();
                        } catch (Exception ignored) { }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            mDoctorList.setAdapter(adapter);

        } catch (Exception ignored) { }
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
    public void onStart() {
        super.onStart();
        mUserRef.child("status").setValue("online");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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


