package com.hcare.homeopathy.hcare.NavigationItems;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ConsultationsActivity extends AppCompatActivity {


    private RecyclerView mDoctorList;
    private DatabaseReference messageArrived;
    private DatabaseReference userRef;

    private DatabaseReference mDoctorDatabase;
    private String mCurrentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultations);

        setTitle("Consultations");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        messageArrived = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mDoctorsDatabase = FirebaseDatabase.getInstance()
                .getReference().child("Private_consult").child(mCurrentUserId);

        mDoctorDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUserId);
        mDoctorList = (RecyclerView) findViewById(R.id.doctor_list);

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

        final FirebaseRecyclerAdapter<Publicreq,DoctorsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Publicreq,DoctorsViewHolder>(options) {

            @NonNull
            @Override
            public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new DoctorsViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.old_consultslayout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull final DoctorsViewHolder viewHolder,
                                            int position, @NonNull Publicreq model) {

                getRef(position).getKey();


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
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) { }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
                h.keepSynced(true);

                mDoctorDatabase.child(user_ids).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userName = dataSnapshot.child("name").getValue().toString();
                        String userImage = dataSnapshot.child("thumb_image").getValue().toString();
                        viewHolder.setName(userName);
                        viewHolder.setDoctorImage(userImage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });


                viewHolder.mView.setOnClickListener(v -> {
                    Intent docprofileIntent =
                            new Intent(ConsultationsActivity.this, ChatActivity.class);
                    docprofileIntent.putExtra("user_id",user_ids);
                    startActivity(docprofileIntent);

                });
            }
        };

        mDoctorList.getRecycledViewPool().clear();
        firebaseRecyclerAdapter.notifyDataSetChanged();
        mDoctorList.setAdapter(firebaseRecyclerAdapter);

    }
    public static class DoctorsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public DoctorsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setSeen(boolean seen){
            ImageView messageView = (ImageView)mView.findViewById(R.id.newmessage);
            if(seen) messageView.setVisibility(View.INVISIBLE);
            else messageView.setVisibility(View.VISIBLE);
        }

        public void setName(String name){
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.doctor_single_name);
            doctorsNameView.setText(name);
        }

        public void setDoctorImage(final String thumb_image){
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




