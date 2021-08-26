package com.hcare.homeopathy.hcare.Main.Doctors;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.hcare.homeopathy.hcare.FirebaseClasses.DoctorObject;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.Objects;

import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.coronaVirus;
import static com.hcare.homeopathy.hcare.Main.Doctors.DoctorsActivity.SCREEN_WIDTH;

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
        model.setDoctorID(Objects.requireNonNull(getRef(position).getKey()));
        holder.setCardRadius();
        try {
            holder.doctorName("Dr. " + model.getName());
            holder.doctorDegree(model.getQualification());
            holder.doctorExperience(model.getExperience());
        } catch(Exception e) {e.printStackTrace();}

        try {
            if(model.getCount() == null && model.getAcceptCount() == null)
                holder.doctorConsultations(0);
            else {
                if(model.getCount() == null)
                    holder.doctorConsultations(model.getAcceptCount().size());
                else if(model.getAcceptCount() == null)
                    holder.doctorConsultations(model.getCount().size());
                else
                    holder.doctorConsultations(model.getCount().size() + model.getAcceptCount().size());
            }

        } catch(Exception ignored) {}

        try {
            if(model.getSex() == null)
                model.setSex("Male");
        } catch(Exception e) {model.setSex("Male");}

        try {
            holder.setImage(model.getImage(), model.getSex());
            holder.openDoctorActivity(context, model);
        } catch(Exception ignored) {}

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

        public void setCardRadius() {
            CardView card = mView.findViewById(R.id.doctorCard);
            card.setRadius(SCREEN_WIDTH/4.6f);
        }

        public void doctorDegree(String doctorName) {
            ((TextView) mView.findViewById(R.id.doctorDegree))
                    .setText(doctorName);
        }

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
    }
}
