package com.hcare.homeopathy.hcare.Consultations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.Objects;

import static com.hcare.homeopathy.hcare.Consultations.Constants.doctorName;

class ConsultationsAdapter extends FirebaseRecyclerAdapter<
        ConsultationsObject, ConsultationsAdapter.DoctorsViewHolder>  {

    private final Context context;
    private final DatabaseReference mDoctorDatabase, messageArrived;
    private final String mCurrentUserId;
    String userName = "";

    public ConsultationsAdapter(@NonNull FirebaseRecyclerOptions<ConsultationsObject> options,
                                Context context,
                                DatabaseReference messageArrived, DatabaseReference mDoctorDatabase,
                                String userID) {
        super(options);
        this.messageArrived = messageArrived;
        this.context = context;
        this.mCurrentUserId = userID;
        this.mDoctorDatabase = mDoctorDatabase;
    }

    @NonNull
    @Override
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_consultations, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull DoctorsViewHolder holder, int position
            , @NonNull ConsultationsObject model) {

        getRef(position).getKey();
        final String user_ids = getRef(position).getKey();
        DatabaseReference h = messageArrived.child("messages").child(mCurrentUserId).
                child(Objects.requireNonNull(user_ids));

        h.limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.hasChild("seen")) {
                    boolean data = (boolean) dataSnapshot.child("seen").getValue();
                    holder.setSeen(data);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                //mDoctorList.scrollToPosition(1);
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
                    new Intent(context, ChatActivity.class);
            docprofileIntent.putExtra("user_id", user_ids);
            context.startActivity(docprofileIntent);
        });
    }

    public static class DoctorsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public DoctorsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        @SuppressLint("SetTextI18n")
        public void setSeen(boolean seen) {
            ImageView messageView = mView.findViewById(R.id.newMessage);
            TextView messageTextView = mView.findViewById(R.id.newMessageText);

            if(seen) {
                messageView.setVisibility(View.INVISIBLE);
                messageTextView.setText("No new messages");
            }
            else messageView.setVisibility(View.VISIBLE);
        }

        public void setName(String name) {
            ((TextView) mView.findViewById(R.id.doctorName)).setText(name);
        }

        public void setDoctorImage(final String thumb_image) {

            Picasso.get().load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.doctor).into(mView.findViewById(R.id.doctorImage),
                    new Callback() {
                @Override
                public void onSuccess() { }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(thumb_image)
                            .into((ImageView) mView.findViewById(R.id.doctorImage));
                }
            });
        }
    }
}