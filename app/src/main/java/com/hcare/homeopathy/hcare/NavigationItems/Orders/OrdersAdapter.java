package com.hcare.homeopathy.hcare.NavigationItems.Orders;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.Objects;

public class OrdersAdapter extends FirebaseRecyclerAdapter<
        OrdersObject, OrdersAdapter.DoctorsViewHolder> {

    private final Context context;

    public OrdersAdapter(@NonNull FirebaseRecyclerOptions<OrdersObject> options,
                         Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_orders, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull DoctorsViewHolder viewHolder,
                                    int position, @NonNull OrdersObject model) {
        viewHolder.orderID(model.getOrderId());
        viewHolder.orderStatus(model.getOrderStatus());
        viewHolder.date(model.getOrdertime().substring(0, 11));
        viewHolder.totalAmount(MessageFormat.format(
                "{0} {1}",
                "Total Amount : ₹", model.getAmount()));

        viewHolder.openPrescription(context, model.getDoctor());
        FirebaseDatabase.getInstance().getReference()
                .child("Doctors").child(model.getDoctor())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String name = MessageFormat.format("{0} {1}",
                            "Dr.",
                            Objects.requireNonNull(
                                    dataSnapshot.child("name")
                                            .getValue()).toString());
                    viewHolder.doctorName(name);
                }
                catch (Exception ignored) { }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public static class DoctorsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public DoctorsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void openPrescription(Context context, String doctorID) {
            mView.findViewById(R.id.viewPrescription)
                    .setOnClickListener(v -> {
                          Intent intent = new Intent(context, PrescriptionActivity.class);
                          intent.putExtra("user_id", doctorID);
                          context.startActivity(intent);
                    });
        }

        public void totalAmount(String name) {
            ((TextView) mView.findViewById(R.id.totalAmount)).setText(name);
        }

        public void orderStatus(String name) {
            ((TextView) mView.findViewById(R.id.orderStatus)).setText(name);
        }

        public void date(String name) {
            ((TextView) mView.findViewById(R.id.date)).setText(name);
        }

        public void orderID(String name) {
            ((TextView) mView.findViewById(R.id.orderID)).setText(
                    MessageFormat.format(
                            "{0} {1}",
                            "Order ID :", name)
            );
        }

        public void doctorName(String doctorName) {
            ((TextView) mView.findViewById(R.id.doctorName))
                    .setText(doctorName);
        }
    }


}
