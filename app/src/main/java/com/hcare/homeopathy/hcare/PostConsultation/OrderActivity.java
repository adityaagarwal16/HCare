package com.hcare.homeopathy.hcare.PostConsultation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcare.homeopathy.hcare.Mainmenus.MainActivity;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView mDoctorList;
    private DatabaseReference mDoctorsDatabase,userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.doctors_appBar);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mDoctorsDatabase = FirebaseDatabase.getInstance().getReference().child("Orders").child(current_uid);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mDoctorList = (RecyclerView) findViewById(R.id.doctor_list);
        mDoctorList.setHasFixedSize(true);
        mDoctorList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        userRef.child("status").setValue("online");
        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>()
                        .setQuery(mDoctorsDatabase, Orders.class)
                        .setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<Orders,DoctorsVeiwHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Orders, DoctorsVeiwHolder>(
               options
        ) {
            @NonNull
            @Override
            public DoctorsVeiwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new DoctorsVeiwHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ordersinglelayout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull DoctorsVeiwHolder viewHolder, int position, @NonNull Orders model) {


                viewHolder.Name(model.getOrderId());
                viewHolder.Degree(model.getOrderStatus());
                viewHolder.experience(model.getOrdertime());
                viewHolder.setDoctorImage(model,getApplicationContext());

                final String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //  Intent docprofileIntent = new Intent(OrderActivity.this,DocprofileActivity.class);
                        //  startActivity(docprofileIntent);
                    }
                });

            }
        };

        mDoctorList.setAdapter(firebaseRecyclerAdapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent startIntent = new Intent(  OrderActivity.this, MainActivity.class);
        startIntent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startIntent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.child("status").setValue("offline");
    }

    public static class DoctorsVeiwHolder extends RecyclerView.ViewHolder{

        View mView;
        public DoctorsVeiwHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void Degree(String name){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.doctor_degree);
            doctorsNameView.setText(name);
        }

        public void experience(String name){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.experience);
            doctorsNameView.setText(name);
        }

        public void Name(String name){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.doctor_single_name);
            doctorsNameView.setText(name);
        }

        public void setDoctorImage(Orders thumb_image, Context ctx){
            CircleImageView userImageView =(CircleImageView) mView.findViewById(R.id.orderBtn);

             Picasso.get().load(R.drawable.orders).placeholder(R.drawable.orders).into(userImageView);
        }
    }
}
