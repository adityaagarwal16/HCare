package com.hcare.homeopathy.hcare.Mainmenus;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hcare.homeopathy.hcare.PreConsultation.DiseaseActivity;
import com.hcare.homeopathy.hcare.R;

import java.util.List;

class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.MyViewHolder> {

    private final List<DiseaseObject> list;
    Context context;

    public DiseaseAdapter(List<DiseaseObject> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.main_activity_disease_recycler, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DiseaseObject object = list.get(position);

        holder.diseaseName.setText(object.getDiseaseName());
        holder.diseaseImage.setImageResource(object.getDrawable());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DiseaseActivity.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(object);
                intent.putExtra("request_type1", myJson);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView diseaseName;
        ImageView diseaseImage;

        public MyViewHolder(View view) {
            super(view);
            card = view.findViewById(R.id.card);
            diseaseName = view.findViewById(R.id.diseaseName);
            diseaseImage = view.findViewById(R.id.diseaseImage);
        }
    }
}