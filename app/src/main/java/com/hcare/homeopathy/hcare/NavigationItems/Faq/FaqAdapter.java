package com.hcare.homeopathy.hcare.NavigationItems.Faq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcare.homeopathy.hcare.R;

import java.util.Objects;

class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.MyViewHolder> {

    private final FaqObject[] list;
    Context context;

    public FaqAdapter(FaqObject[] list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_faq, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        FaqObject faqObject  = list[position];
        holder.heading.setText(Objects.requireNonNull(faqObject).getHeading());
        holder.subHeading.setText(faqObject.getSubHeading());
        holder.line.setText(faqObject.getHeading());

        holder.layout.setOnClickListener(v -> {
            if(holder.subHeading.getVisibility() == View.VISIBLE)
                holder.subHeading.setVisibility(View.GONE);
            else
                holder.subHeading.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView heading, subHeading, line;
        RelativeLayout layout;

        public MyViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.layout);
            line = view.findViewById(R.id.line);
            heading = view.findViewById(R.id.heading);
            subHeading = view.findViewById(R.id.subHeading);
        }
    }
}