package com.hcare.homeopathy.hcare.Consultations.Doctor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.hcare.homeopathy.hcare.NavigationItems.Orders.OrdersActivity;
import com.hcare.homeopathy.hcare.OrderTreatment.CartActivity;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

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
                .inflate(R.layout.adapter_chat, parent,false));
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

        switch (c.getType()) {

            case "text":
                viewHolder.treatmentCard.setVisibility(View.GONE);
                viewHolder.imageCard.setVisibility(View.GONE);
                if(c.getFrom().equals(userID)) {
                    viewHolder.doctorMessage.setVisibility(View.GONE);
                    viewHolder.userMessage.setText(c.getMessage());
                    viewHolder.userMessage.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.userMessage.setVisibility(View.GONE);
                    viewHolder.doctorMessage.setText(c.getMessage());
                    viewHolder.doctorMessage.setVisibility(View.VISIBLE);
                }
                break;

            case "image":
                viewHolder.userMessage.setVisibility(View.GONE);
                viewHolder.doctorMessage.setVisibility(View.GONE);
                viewHolder.treatmentCard.setVisibility(View.GONE);

                Picasso.get().load(c.getMessage())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(viewHolder.chatImage, new Callback() {
                            @Override
                            public void onSuccess() { }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(c.getMessage())
                                        .into(viewHolder.chatImage);
                            }
                        });


                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)
                        viewHolder.imageCard.getLayoutParams();
                if(c.getFrom().equals(userID)) {
                    lp.removeRule(RelativeLayout.ALIGN_PARENT_START);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_END);
                } else {
                    lp.removeRule(RelativeLayout.ALIGN_PARENT_END);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_START);

                }
                viewHolder.imageCard.setLayoutParams(lp);
                viewHolder.imageCard.setVisibility(View.VISIBLE);

                viewHolder.imageCard.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(c.getMessage()), "image/*");
                    context.startActivity(intent);
                });

                break;

            case "pdf":
                viewHolder.header.setText(R.string.pdf_file);
                RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams)
                        viewHolder.treatmentCard.getLayoutParams();
                if(c.getFrom().equals(userID)) {
                    lp1.removeRule(RelativeLayout.ALIGN_PARENT_START);
                    lp1.addRule(RelativeLayout.ALIGN_PARENT_END);

                } else {
                    lp1.removeRule(RelativeLayout.ALIGN_PARENT_END);
                    lp1.addRule(RelativeLayout.ALIGN_PARENT_START);
                }
                viewHolder.treatmentCard.setLayoutParams(lp1);
                viewHolder.treatmentCard.setVisibility(View.VISIBLE);

                viewHolder.treatmentCard.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(c.getMessage()), "application/pdf");
                    context.startActivity(intent);
                });

                viewHolder.userMessage.setVisibility(View.GONE);
                viewHolder.doctorMessage.setVisibility(View.GONE);
                viewHolder.imageCard.setVisibility(View.GONE);
                break;

            case "call back":
                viewHolder.doctorMessage.setVisibility(View.VISIBLE);
                viewHolder.doctorMessage
                        .setText("The doctor will get in touch with you as soon as possible.");
                viewHolder.userMessage.setVisibility(View.GONE);
                viewHolder.imageCard.setVisibility(View.GONE);
                viewHolder.treatmentCard.setVisibility(View.GONE);
                break;

            default:
                viewHolder.treatmentCard.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams lp3 = (RelativeLayout.LayoutParams)
                        viewHolder.treatmentCard.getLayoutParams();
                lp3.removeRule(RelativeLayout.ALIGN_PARENT_END);
                lp3.addRule(RelativeLayout.ALIGN_PARENT_START);
                viewHolder.treatmentCard.setLayoutParams(lp3);

                viewHolder.userMessage.setVisibility(View.GONE);
                viewHolder.imageCard.setVisibility(View.GONE);
                viewHolder.doctorMessage.setVisibility(View.VISIBLE);
                viewHolder.doctorMessage
                        .setText("Here's your header, please press the following button to get it.");

                if (c.getOrdering().equals("ordered")) {
                    viewHolder.header.setText("Ordered");
                }

                viewHolder.treatmentCard.setOnClickListener(v -> {
                    if (c.getOrdering().equals("ordered")) {
                        context.startActivity(new Intent(v.getContext(), OrdersActivity.class));
                    } else {
                        Intent intent = new Intent(context, CartActivity.class);
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

        public TextView userMessage , doctorMessage, messageTime, header;
        public CardView treatmentCard, imageCard;
        public ImageView chatImage;

        public MessageViewHolder(View view){
            super(view);
            userMessage = view.findViewById(R.id.userMessage);
            doctorMessage =view.findViewById(R.id.doctorMessage);
            header = view.findViewById(R.id.header);
            messageTime = view.findViewById(R.id.messageTime);
            treatmentCard = view.findViewById(R.id.treatmentCard);
            imageCard = view.findViewById(R.id.imageCard);
            chatImage = view.findViewById(R.id.chatImage);
        }

    }



}
