package com.hcare.homeopathy.hcare.Main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hcare.homeopathy.hcare.NewConsultation.DiseaseActivity;
import com.hcare.homeopathy.hcare.NewConsultation.DiseaseInfo;
import com.hcare.homeopathy.hcare.NewConsultation.Diseases;
import com.hcare.homeopathy.hcare.R;

import java.util.ArrayList;

class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private final ArrayList<Diseases> arrayList;
    Context context;

    public SearchAdapter(ArrayList<Diseases> list, Context context) {
        this.arrayList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_search, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Diseases disease = arrayList.get(position);
        if(disease == Diseases.others)
            holder.disease.setText("Couldn't find the disease you're looking for, tap here.");
        else
            holder.disease.setText(new DiseaseInfo(disease).getDiseaseName());

        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(context, DiseaseActivity.class);
            intent.putExtra("request_type1", disease);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView disease;
        RelativeLayout layout;

        public MyViewHolder(View view) {
            super(view);
            disease = view.findViewById(R.id.disease);
            layout = view.findViewById(R.id.layout);
        }
    }
}