package com.hcare.homeopathy.hcare.Consultations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.Consultations.Doctor.MainDoctorActivity;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

class AllChatsAdapter extends FirebaseRecyclerAdapter<
        AllChatsObject, AllChatsAdapter.DoctorsViewHolder>  {

    private final Context context;
    private final String userID;
    String userName = "";

    public AllChatsAdapter(@NonNull FirebaseRecyclerOptions<AllChatsObject> options,
                           Context context, String userID) {
        super(options);
        this.context = context;
        this.userID = userID;
    }

    @NonNull
    @Override
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_consultations, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull DoctorsViewHolder holder, int position
            , @NonNull AllChatsObject model) {
        getRef(position).getKey();
        try {
            String doctorID = Objects.requireNonNull(getRef(position).getKey());

            DatabaseReference messages =
                    FirebaseDatabase.getInstance().getReference()
                            .child("messages")
                            .child(userID)
                            .child(doctorID);

            messages.limitToLast(1).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.hasChild("seen")) {
                        try {
                            boolean data = (boolean) dataSnapshot.child("seen").getValue();
                            Log.i("data", String.valueOf(data));

                            if (Objects.requireNonNull(dataSnapshot.child("from").getValue())
                                    .toString().equals(userID))
                                //if last text is by user then set seen is true
                                holder.setSeen(true);
                            else
                                holder.setSeen(data);

                        } catch (Exception e) {
                            holder.setSeen(true);
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                    //mDoctorList.scrollToPosition(1);
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

            messages.keepSynced(true);

            FirebaseDatabase.getInstance()
                    .getReference().child("Doctors").child(doctorID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                userName = Objects.requireNonNull(
                                        dataSnapshot.child("name").getValue()).toString();
                                String userImage = Objects.requireNonNull(
                                        dataSnapshot.child("thumb_image").getValue()).toString();
                                holder.setName(userName);
                                holder.setDoctorImage(userImage);
                            }
                            catch (Exception ignored) { }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });

            holder.mView.setOnClickListener(v -> {
                Intent docprofileIntent =
                        new Intent(context, MainDoctorActivity.class);
                docprofileIntent.putExtra("user_id", doctorID);
                context.startActivity(docprofileIntent);

            });
        } catch (Exception ignored) {}
    }

    public static class DoctorsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public DoctorsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        @SuppressLint("SetTextI18n")
        public void setSeen(boolean seen) {
            ImageView unreadDot = mView.findViewById(R.id.newMessage);
            TextView messageTextView = mView.findViewById(R.id.newMessageText);

            if(seen) {
                unreadDot.setVisibility(View.INVISIBLE);
                messageTextView.setText("No new messages");
            }
            else {
                messageTextView.setText("Unread Messages");
                unreadDot.setVisibility(View.VISIBLE);
            }
        }

        public void setName(String name) {
            ((TextView) mView.findViewById(R.id.doctorName)).setText(name);
        }


        public void setDoctorImage(final String thumb_image) {
            if (!thumb_image.equals("doctor image")) {
                Picasso.get().load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.vector_doctor_male)
                        .into(mView.findViewById(R.id.doctorImage),
                                new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get().load(thumb_image)
                                                .into((ImageView) mView.findViewById(R.id.doctorImage));
                                    }
                                });
            } else {
                ((ImageView)  mView.findViewById(R.id.doctorImage))
                        .setImageResource(R.drawable.vector_doctor_male);
            }
        }
    }
}