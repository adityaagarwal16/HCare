package com.hcare.homeopathy.hcare.Main.Doctors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.Consultations.Doctor.DoctorDetailsFragment;
import com.hcare.homeopathy.hcare.FirebaseClasses.DoctorObject;
import com.hcare.homeopathy.hcare.FirebaseClasses.OrderObject;
import com.hcare.homeopathy.hcare.Orders.TrackOrderActivity;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.coronaVirus;

public class DoctorsAdapter extends FirebaseRecyclerAdapter<DoctorObject, DoctorsAdapter.MyViewHolder> {

    private final Context context;


    public DoctorsAdapter(@NonNull FirebaseRecyclerOptions<DoctorObject> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_doctors, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull DoctorObject model) {

        String doctorID = Objects.requireNonNull(getRef(position).getKey());

        holder.doctorName("Dr. " + model.getName());
        holder.doctorDegree(model.getQualification());
        holder.doctorExperience(model.getExperience() + " experience");
        holder.setImage(model.getThumb_image());
        holder.showFragment(context, doctorID);

    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void doctorName(String doctorName) {
            ((TextView) mView.findViewById(R.id.doctorName))
                    .setText(doctorName);
        }
        public void doctorDegree(String doctorName) {
            ((TextView) mView.findViewById(R.id.doctorDegree))
                    .setText(doctorName);
        }
        public void doctorExperience(String doctorName) {
            ((TextView) mView.findViewById(R.id.doctorExperience))
                    .setText(doctorName);

        }
        public void setImage(String image) {
            Picasso.get().load(image)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.vector_person)
                    .into(mView.findViewById(R.id.doctorImage),
                            new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(image).placeholder(R.drawable.vector_person)
                                            .into((ImageView) mView.findViewById(R.id.doctorImage));
                                }
                            });
        }
        public void showFragment(Context context, String doctorID) {
            mView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DoctorsDetailsActivity.class);
                intent.putExtra("doctorID", doctorID);
                context.startActivity(intent);
            });
        }
    }
}
