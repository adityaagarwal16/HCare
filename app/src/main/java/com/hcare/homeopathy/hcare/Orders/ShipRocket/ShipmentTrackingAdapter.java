package com.hcare.homeopathy.hcare.Orders.ShipRocket;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcare.homeopathy.hcare.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShipmentTrackingAdapter extends RecyclerView.Adapter<ShipmentTrackingAdapter.MyViewHolder> {

    private final List<ShipmentTrackActivity> list;

    public ShipmentTrackingAdapter(List<ShipmentTrackActivity> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_shipment_tracking, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        ShipmentTrackActivity order  = list.get(position);

        try {
            Log.i("date", order.date);
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).
                    parse(order.date);
            assert date != null;
            holder.date.setText(new SimpleDateFormat("dd MMM\nhh:mm a",
                    Locale.ENGLISH).format(date));
        } catch(Exception e) { e.printStackTrace(); }

        holder.location.setText(order.location);

        if(order.activity.contains("Manifested - "))
            order. activity = order.activity.replace("Manifested - ", "");
        else if(order.activity.contains("Pending - "))
            order. activity = order.activity.replace("Pending - ", "");
        else if(order.activity.contains("Dispatched  - "))
            order. activity = order.activity.replace("Dispatched - ", "");

        holder.activity.setText(order.activity);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, location, activity;

        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.date);
            location = view.findViewById(R.id.location);
            activity = view.findViewById(R.id.activity);
        }
    }
}