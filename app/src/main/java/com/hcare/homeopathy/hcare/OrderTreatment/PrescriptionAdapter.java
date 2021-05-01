package com.hcare.homeopathy.hcare.OrderTreatment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.hcare.homeopathy.hcare.R;

class PrescriptionAdapter extends FirebaseRecyclerAdapter<
        PrescriptionObject, PrescriptionAdapter.DoctorsViewHolder>  {

    public PrescriptionAdapter(@NonNull FirebaseRecyclerOptions<PrescriptionObject> options) {
        super(options);
    }

    @NonNull
    @Override
    public DoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoctorsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_prescription, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull DoctorsViewHolder holder, int position,
                                    @NonNull PrescriptionObject model) {
        holder.sets(model.getMedicine_name());
        holder.setB(model.getMedicine_days());
        holder.setC(model.getMedicine_time());
        holder.setTime(model.getMedicine_Instruction());
        holder.setSos(model.getInstructions());
    }

    public static class DoctorsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public DoctorsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void sets(String medicine_A) {
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.medA);
            doctorsNameView.setText(medicine_A);
            ((TextView) mView.findViewById(R.id.line)).setText(medicine_A);
        }
        public void setB(String medicine_B) {
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.medB);
            doctorsNameView.setText(medicine_B);
        }
        public void setC(String medicine_C) {
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.medC);
            doctorsNameView.setText(medicine_C);
        }

        public void setSos(String sos) {
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.medE);
            if(sos.isEmpty())
                doctorsNameView.setVisibility(View.GONE);
            else
                doctorsNameView.setText(sos);
        }
        public void setTime(String time) {
            TextView doctorsNameView = (TextView) mView.findViewById(R.id.medD);
            doctorsNameView.setText(time);
        }

    }
}