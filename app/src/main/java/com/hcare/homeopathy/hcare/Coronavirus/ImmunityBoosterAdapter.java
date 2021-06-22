package com.hcare.homeopathy.hcare.Coronavirus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hcare.homeopathy.hcare.R;

import java.util.ArrayList;

class ImmunityBoosterAdapter extends RecyclerView.Adapter<ImmunityBoosterAdapter.MyViewHolder> {
    
    private final ArrayList<Boosters> arrayList;
    private final Boosters[] list;
    Context context;

    public ImmunityBoosterAdapter(Boosters[] list, Context context) {
        this.list = list;
        this.arrayList = null;
        this.context = context;
    }

    public ImmunityBoosterAdapter(ArrayList<Boosters> list, Context context) {
        this.arrayList = list;
        this.list = null;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_immunity_booster, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        BoosterInfo object = null;
        Boosters booster = null;
        if(arrayList!= null) {
            booster = arrayList.get(position);
            object = new BoosterInfo(booster);
        }else if(list != null) {
            booster = list[position];
            object = new BoosterInfo(booster);
        }

        assert object != null;
        holder.boosterName.setText(object.getBoosterName());

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
        TextView boosterName;

        public MyViewHolder(View view) {
            super(view);
            card = view.findViewById(R.id.card);
            boosterName = view.findViewById(R.id.boosterName);
        }
    }



}
