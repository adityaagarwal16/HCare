package com.hcare.homeopathy.hcare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hcare.homeopathy.hcare.PreConsultation.DiseaseActivity;

import java.util.ArrayList;

class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.MyViewHolder> {

    private final Diseases[] list;
    private final ArrayList<Diseases> arrayList;
    boolean isArrayList = false;
    Context context;

    public DiseaseAdapter(Diseases[] list, Context context) {
        this.list = list;
        this.arrayList = null;
        this.context = context;
    }
    public DiseaseAdapter(ArrayList<Diseases> list, Context context) {
        isArrayList = true;
        this.arrayList = list;
        this.list = null;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        DiseaseInfo object;
        if(isArrayList)
            object = new DiseaseInfo(arrayList.get(position));
        else
            object = new DiseaseInfo(list[position]);

        holder.diseaseName.setText(object.getDiseaseName());
        holder.diseaseImage.setImageResource(object.getDrawable());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DiseaseActivity.class);
                intent.putExtra("request_type1", list[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(isArrayList)
            return arrayList.size();
        else
            return list.length;
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