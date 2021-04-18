package com.hcare.homeopathy.hcare.Mainmenus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.Consultation.ChatActivity;
import com.hcare.homeopathy.hcare.JvFiles.Publicreq;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecentmainmenuActivity extends AppCompatActivity {


    private RecyclerView mDoctorList;
    private DatabaseReference messageArrived;
    private DatabaseReference userRef;

    private DatabaseReference mDoctorDatabase;
    private String mCurrentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recentmainmenu);

        setTitle("Consultations");

       /* final int abTitleId = getResources().getIdentifier("action_bar_title", "id", "android");
        findViewById(abTitleId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent docprofileIntent = new Intent(RecentmainmenuActivity.this, MainActivity.class);
                docprofileIntent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(docprofileIntent);
            }
        });*/

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        messageArrived = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mDoctorsDatabase = FirebaseDatabase.getInstance().getReference().child("Private_consult").child(mCurrentUserId);

        mDoctorDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUserId);
        mDoctorList = (RecyclerView)findViewById(R.id.doctor_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mDoctorList.setHasFixedSize(true);
        mDoctorList.setLayoutManager(linearLayoutManager);
        mDoctorsDatabase.keepSynced(true);
        mDoctorDatabase.keepSynced(true);


        Query docquervy = mDoctorsDatabase.orderByChild("time");
        FirebaseRecyclerOptions<Publicreq> options =
                new FirebaseRecyclerOptions.Builder<Publicreq>()
                        .setQuery(docquervy, Publicreq.class)
                        .setLifecycleOwner(this)
                        .build();
        final FirebaseRecyclerAdapter<Publicreq,DoctorsVeiwHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Publicreq,DoctorsVeiwHolder>(
                options
        ) {
            @NonNull
            @Override
            public DoctorsVeiwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new DoctorsVeiwHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.old_consultslayout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull final DoctorsVeiwHolder viewHolder, int position, @NonNull Publicreq model) {



                final String user_id = getRef(position).getKey();


                final String user_ids = getRef(position).getKey();

                DatabaseReference h = messageArrived.child("messages").child(mCurrentUserId).child(user_ids);

                h.limitToLast(1).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                        if (dataSnapshot.hasChild("seen")) {
                            boolean data = (boolean) dataSnapshot.child("seen").getValue();
                            viewHolder.setSeen(data);

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        mDoctorList.scrollToPosition(1);
                        notifyDataSetChanged();

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                h.keepSynced(true);

                mDoctorDatabase.child(user_ids).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String userName = dataSnapshot.child("name").getValue().toString();
                        String userNam = dataSnapshot.child("thumb_image").getValue().toString();
                        viewHolder.setName(userName);
                        viewHolder.setDoctorImage(userNam,getApplicationContext());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent docprofileIntent = new Intent(RecentmainmenuActivity.this, ChatActivity.class);
                        docprofileIntent.putExtra("user_id",user_ids);
                        startActivity(docprofileIntent);

                    }
                });
            }
        };
        mDoctorList.getRecycledViewPool().clear();
        firebaseRecyclerAdapter.notifyDataSetChanged();
        mDoctorList.setAdapter(firebaseRecyclerAdapter);

    }
    public static class DoctorsVeiwHolder extends RecyclerView.ViewHolder{

        View mView;
        public DoctorsVeiwHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setSeen(boolean seen){


            ImageView messageView = (ImageView)mView.findViewById(R.id.newmessage);

            if(seen ==false){

                messageView.setVisibility(View.VISIBLE);



            } else if (seen==true){

                messageView.setVisibility(View.INVISIBLE);

            }

        }

        public void setName(String name){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.doctor_single_name);
            doctorsNameView.setText(name);
        }

        public void setDoctorImage(final String thumb_image, final Context ctx){
            final ImageView userImageView =(ImageView) mView.findViewById(R.id.doctorImage);

            Picasso.get().load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.doctor).into(userImageView ,new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(thumb_image).into(userImageView);
                }
            });
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        userRef.child("status").setValue("online");
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.child("status").setValue("offline");
    }
}




