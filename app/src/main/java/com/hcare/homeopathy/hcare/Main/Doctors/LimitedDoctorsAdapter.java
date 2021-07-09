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
import com.hcare.homeopathy.hcare.Main.MainActivity;
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
                .inflate(R.layout.adapter_doctors_limited, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LimitedDoctorsAdapter.MyViewHolder holder, int position) {
        if(position == arrayList.size() -1)
            holder.setViewMore(context);
        else {
            holder.hideViewMore();
            DoctorObject model = arrayList.get(position);
            String doctorID = list[position];
            try {
                holder.doctorName("Dr. " + model.getName());
                holder.doctorDegree(model.getQualification());
                holder.doctorExperience(model.getExperience() + " experience");
            } catch(Exception e) {e.printStackTrace();}

            holder.setImage(model.getImage(), model.getSex());
            holder.openDoctorActivity(context, doctorID);
        }

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

        public void setViewMore(Context context) {
            itemView.findViewById(R.id.viewMore).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.info).setVisibility(View.GONE);
            itemView.findViewById(R.id.viewMore).setOnClickListener( v ->
                    context.startActivity(new Intent(context, DoctorsActivity.class)));
        }

        public void hideViewMore() {
            itemView.findViewById(R.id.viewMore).setVisibility(View.GONE);
            itemView.findViewById(R.id.info).setVisibility(View.VISIBLE);
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


        public void setImage(String image, String sex) {
            int drawable = R.drawable.vector_doctor_male;
            if(sex.equals("Female"))
                drawable = R.drawable.vector_doctor_female;
            int finalDrawable = drawable;

            Picasso.get().load(image)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(drawable)
                    .into(mView.findViewById(R.id.doctorImage),
                            new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(image).placeholder(finalDrawable)
                                            .into((ImageView) mView.findViewById(R.id.doctorImage));
                                }
                            });
        }

        public void openDoctorActivity(Context context, String doctorID) {
            mView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DoctorsDetailsActivity.class);
                intent.putExtra("doctorID", doctorID);
                context.startActivity(intent);
            });
        }
    }
}

