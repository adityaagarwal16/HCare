package com.hcare.homeopathy.hcare.Main.Doctors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcare.homeopathy.hcare.FirebaseClasses.DoctorObject;
import com.hcare.homeopathy.hcare.NewConsultation.DiseaseAdapter;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class LimitedDoctorsAdapter extends RecyclerView.Adapter<LimitedDoctorsAdapter.MyViewHolder> {

    private final ArrayList<DoctorObject> arrayList;
    Context context;
    private final String[] list;

    public LimitedDoctorsAdapter(ArrayList<DoctorObject> arrayList, Context context, String[] list) {
        this.arrayList = arrayList;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LimitedDoctorsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LimitedDoctorsAdapter.MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_doctors, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LimitedDoctorsAdapter.MyViewHolder holder, int position) {

        DoctorObject model = null;
        model = arrayList.get(position);

        String doctorID = list[position];

        holder.doctorName("Dr. " + model.getName());
        holder.doctorDegree(model.getQualification());
        holder.doctorExperience(model.getExperience() + " experience");
        holder.setImage(model.getThumb_image());
        holder.showFragment(context, doctorID);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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
