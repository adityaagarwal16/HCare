package com.hcare.homeopathy.hcare.Orders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.joestelmach.natty.Parser;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.hcare.homeopathy.hcare.FirebaseConstants.coronaVirus;

public class AllOrdersAdapter extends FirebaseRecyclerAdapter<
        AllOrdersObject, AllOrdersAdapter.DoctorsViewHolder> {

    private final Context context;

    public AllOrdersAdapter(@NonNull FirebaseRecyclerOptions<AllOrdersObject> options,
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
                                    int position, @NonNull AllOrdersObject model) {
        try { viewHolder.orderID(model.getOrderId()); } catch(Exception ignored) {}

        try {
            if(model.getTime() == null)
                model.setTime(model.getOrdertime());
            Parser parser = new Parser();
            Date date = parser.parse(model.getOrdertime()).get(0).getDates().get(0);
            viewHolder.date(new SimpleDateFormat("MMM dd, yyyy\n hh:mm a",
                    Locale.ENGLISH).format(date));
        } catch(Exception e) { e.printStackTrace(); }

        if(model.getAmount() != null)
            viewHolder.totalAmount(MessageFormat.format(
                    "{0} {1}",
                    "₹", model.getAmount()));

        viewHolder.openOrder(context, model);

        try {
            if(model.getDoctor() != null)
                FirebaseDatabase.getInstance().getReference()
                        .child("Doctors").child(model.getDoctor())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    String name;
                                    if(model.getDoctor().equals(coronaVirus))
                                        name = Objects.requireNonNull(
                                                dataSnapshot.child("name")
                                                        .getValue()).toString();
                                    else
                                        name = MessageFormat.format("{0} {1}",
                                                "Dr.",
                                                Objects.requireNonNull(
                                                        dataSnapshot.child("name")
                                                                .getValue()).toString());
                                    viewHolder.doctorName(name);
                                } catch (Exception ignored) { }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
        } catch (Exception e) {e.printStackTrace();}
    }

    public static class DoctorsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public DoctorsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void openOrder(Context context, AllOrdersObject object) {
            mView.setOnClickListener(v -> {
                Intent intent = new Intent(context, OrderActivity.class);
                intent.putExtra("order", object);
                intent.putExtra("doctorName",
                        ((TextView) mView.findViewById(R.id.doctorName)).getText().toString());
                context.startActivity(intent);
            });
        }

        public void totalAmount(String name) {
            ((TextView) mView.findViewById(R.id.totalAmount)).setText(name);
        }

        public void date(String name) {
            ((TextView) mView.findViewById(R.id.date)).setText(name);
        }

        public void orderID(String name) {
            ((TextView) mView.findViewById(R.id.orderID)).setText(name);
        }

        public void doctorName(String doctorName) {
            ((TextView) mView.findViewById(R.id.doctorName))
                    .setText(doctorName);
        }
    }


}
