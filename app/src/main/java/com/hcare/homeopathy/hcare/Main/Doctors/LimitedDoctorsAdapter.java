package com.hcare.homeopathy.hcare.Main.Doctors;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
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

    public LimitedDoctorsAdapter(ArrayList<DoctorObject> arrayList, Context context, String[] list) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public LimitedDoctorsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
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
            try {
                holder.doctorName("Dr. " + model.getName());
                holder.doctorDegree(model.getQualification());
                holder.doctorExperience(model.getExperience());

                if(model.getCount() == null && model.getAcceptCount() == null)
                    holder.doctorConsultations(0);
                else {
                    if(model.getCount() == null)
                        holder.doctorConsultations(model.getAcceptCount().size());
                    else if(model.getAcceptCount() == null)
                        holder.doctorConsultations(model.getCount().size());
                    else {
                        holder.doctorConsultations(model.getCount().size() + model.getAcceptCount().size());
                    }
                }

            } catch(Exception e) {e.printStackTrace();}

            if(model.getSex() == null)
                model.setSex("Male");
            holder.setImage(model.getImage(), model.getSex());
            holder.openDoctorActivity(context, model);

        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public void doctorExperience(int years) {
            String text = String.valueOf(years);
            if(years == 1)
                text += " year";
            else
                text += " years";
            String sourceString = "<b> <font color='black'>" + text + "</b> " + " experience";
            ((TextView) mView.findViewById(R.id.doctorExperience))
                    .setText(Html.fromHtml(sourceString), TextView.BufferType.SPANNABLE);
        }

        public void doctorConsultations(int consultations) {
            TextView text = mView.findViewById(R.id.consultations);
            if(consultations == 0)
                text.setVisibility(View.GONE);
            else {
                String consultText = "Consultations";
                if(consultations == 1)
                    consultText = "Consultation";

                text.setVisibility(View.VISIBLE);
                String sourceString = "<b> <font color='#82e4ad'>" +
                        consultations + "</b> " + " " +consultText;
                text.setText(Html.fromHtml(sourceString), TextView.BufferType.SPANNABLE);
            }

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
                                public void onSuccess() { }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(image).placeholder(finalDrawable)
                                            .into((ImageView) mView.findViewById(R.id.doctorImage));
                                }
                            });
        }

        public void openDoctorActivity(Context context, DoctorObject doctor) {
            mView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DoctorsDetailsActivity.class);
                intent.putExtra("doctor", doctor);
                context.startActivity(intent);
            });
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setViewMore(Context context) {
            ViewGroup.LayoutParams layoutParams = itemView.findViewById(R.id.viewMore)
                    .getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            itemView.findViewById(R.id.viewMore).setLayoutParams(layoutParams);

            ViewGroup.LayoutParams layoutParams1 = itemView.findViewById(R.id.info)
                    .getLayoutParams();
            layoutParams1.width = 0;
            itemView.findViewById(R.id.info).setLayoutParams(layoutParams1);

            itemView.findViewById(R.id.viewMore).setOnClickListener( v ->
                    context.startActivity(new Intent(context, DoctorsActivity.class)));
        }

        public void hideViewMore() {
            ViewGroup.LayoutParams layoutParams = itemView.findViewById(R.id.viewMore).getLayoutParams();
            layoutParams.width = 0;
            itemView.findViewById(R.id.viewMore).setLayoutParams(layoutParams);

            ViewGroup.LayoutParams layoutParams1 = itemView.findViewById(R.id.info).getLayoutParams();
            layoutParams1.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            itemView.findViewById(R.id.info).setLayoutParams(layoutParams1);
        }

        public void doctorName(String doctorName) {
            ((TextView) mView.findViewById(R.id.doctorName))
                    .setText(doctorName);
        }

        public void doctorDegree(String doctorName) {
            ((TextView) mView.findViewById(R.id.doctorDegree))
                    .setText(doctorName);
        }

    }


}

