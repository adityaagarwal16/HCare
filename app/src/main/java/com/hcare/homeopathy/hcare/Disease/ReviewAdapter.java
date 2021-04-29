package com.hcare.homeopathy.hcare.Disease;

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

import com.hcare.homeopathy.hcare.DiseaseInfo;
import com.hcare.homeopathy.hcare.Diseases;
import com.hcare.homeopathy.hcare.R;

import java.util.ArrayList;

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private final Diseases[] list;
    private final ArrayList<Diseases> arrayList;
    Context context;

    public ReviewAdapter(Diseases[] list, Context context) {
        this.list = list;
        this.arrayList = null;
        this.context = context;
    }

    public ReviewAdapter(ArrayList<Diseases> list, Context context) {
        this.arrayList = list;
        this.list = null;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_reviews, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        DiseaseInfo object = null;
        Diseases disease = null;
        if(arrayList!= null) {
            disease = arrayList.get(position);
            object = new DiseaseInfo(disease);
        } else if(list != null) {
            disease = list[position];
            object = new DiseaseInfo(disease);
        }

        assert object != null;
        holder.diseaseName.setText(object.getDiseaseName());
        holder.diseaseImage.setImageResource(object.getDrawable());

        Diseases finalDisease = disease;
        holder.card.setOnClickListener(v -> {
            Intent intent = new Intent(context, DiseaseActivity.class);
            intent.putExtra("request_type1", finalDisease);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(arrayList!= null)
            return arrayList.size();
        else {
            assert list != null;
            return list.length;
        }
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