package com.hcare.homeopathy.hcare.Consultations.Doctor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hcare.homeopathy.hcare.PostConsultation.OrderActivity;
import com.hcare.homeopathy.hcare.PostConsultation.PrescriptionActivity;
import com.hcare.homeopathy.hcare.R;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private final List<ChatObject> mMessageList;
    private final Context context;

    public ChatAdapter(List<ChatObject> mMessageList, Context context){
        this.mMessageList =mMessageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder viewHolder, int i) {
        String userID = Objects.requireNonNull(
                FirebaseAuth.getInstance()).getUid();

        final ChatObject c = mMessageList.get(i);
        final String time = new GetTime(c.getTime()).getTimeAgo();

        if(!time.isEmpty()) {
            viewHolder.messageTime.setVisibility(View.VISIBLE);
            viewHolder.messageTime.setText(time);
        } else
            viewHolder.messageTime.setVisibility(View.GONE);


        if(c.getFrom().equals(userID)) {
            viewHolder.doctorMessage.setVisibility(View.GONE);
            viewHolder.userMessage.setVisibility(View.VISIBLE);
        }else{
            viewHolder.userMessage.setVisibility(View.GONE);
            viewHolder.doctorMessage.setVisibility(View.VISIBLE);
        }

        switch (c.getType()) {

            case "text":
                viewHolder.userMessage.setText(c.getMessage());
                viewHolder.doctorMessage.setText(c.getMessage());
                viewHolder.treatmentCard.setVisibility(View.GONE);
                break;

            case "image":
                viewHolder.userMessage.setVisibility(View.GONE);
                viewHolder.doctorMessage.setVisibility(View.GONE);
                viewHolder.treatmentCard.setVisibility(View.GONE);
                break;

            case "pdf":
                viewHolder.treatment.setText(R.string.pdf_file);
                viewHolder.treatmentCard.setVisibility(View.VISIBLE);

                viewHolder.treatmentCard.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(c.getMessage()), "application/pdf");
                    context.startActivity(intent);
                });

                viewHolder.userMessage.setVisibility(View.GONE);
                viewHolder.doctorMessage.setVisibility(View.GONE);
                break;

            case "call back":
                viewHolder.doctorMessage.setVisibility(View.VISIBLE);
                viewHolder.doctorMessage
                        .setText("The doctor will get in touch with you as soon as possible.");
                viewHolder.userMessage.setVisibility(View.GONE);
                viewHolder.treatmentCard.setVisibility(View.GONE);
                break;

            default:
                viewHolder.treatmentCard.setVisibility(View.VISIBLE);
                viewHolder.userMessage.setVisibility(View.GONE);
                viewHolder.doctorMessage.setVisibility(View.VISIBLE);
                viewHolder.doctorMessage
                        .setText("Here's your treatment, please press the following button to get it.");

                if (c.getOrdering().equals("ordered")) {
                    viewHolder.treatment.setText("Ordered");
                }

                viewHolder.treatmentCard.setOnClickListener(v -> {
                    if (c.getOrdering().equals("ordered")) {
                        context.startActivity(new Intent(v.getContext(), OrderActivity.class));
                    } else {
                        Intent intent = new Intent(context, PrescriptionActivity.class);
                        intent.putExtra("user_id", c.getFrom());
                        intent.putExtra("medicine_id", c.getMedicineId());
                        context.startActivity(intent);
                    }
                });
                break;
        }
    }


    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView userMessage , doctorMessage, messageTime;
        public TextView treatment;
        public CardView treatmentCard;


        public MessageViewHolder(View view){
            super(view);
            userMessage = view.findViewById(R.id.userMessage);
            doctorMessage =view.findViewById(R.id.doctorMessage);
            treatment = view.findViewById(R.id.treatment);
            messageTime = view.findViewById(R.id.messageTime);
            treatmentCard = view.findViewById(R.id.treatmentCard);
        }

    }



}
