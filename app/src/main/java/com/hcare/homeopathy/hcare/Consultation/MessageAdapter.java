package com.hcare.homeopathy.hcare.Consultation;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hcare.homeopathy.hcare.JvFiles.GetTimeAgo;
import com.hcare.homeopathy.hcare.JvFiles.ImageActivity;
import com.hcare.homeopathy.hcare.PostConsultation.OrderActivity;
import com.hcare.homeopathy.hcare.PostConsultation.PrescriptionActivity;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Vinith pc on 9/10/2017.
 */

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<Messages> mMessageList;

    public  MessageAdapter(List<Messages> mMessageList){
        this.mMessageList =mMessageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout,parent,false);
        return new MessageViewHolder(v);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText,messageText1,mPdf,mcallback,infotext,time;
        public ImageView messageView,messageView1;
        public Button treatmentBtn;


        public MessageViewHolder(View view){
            super(view);
            messageText = view.findViewById(R.id.message_text_layout);
            messageText1 =view.findViewById(R.id.message_text_layout1);
            messageView =view.findViewById(R.id.textimage);
            messageView1 =view.findViewById(R.id.textimage1);
            mPdf = view.findViewById(R.id.pdfview);
            mcallback = view.findViewById(R.id.callreqView);
            infotext = view.findViewById(R.id.treattext);
            treatmentBtn = view.findViewById(R.id.treatment);
            time = view.findViewById(R.id.timeview);
         //    myAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.shake);
           // treatmentBtn.setAnimation(myAnim);

        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String current_user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        final Messages c = mMessageList.get(i);

        final String medicine_id = c.getMedicineId();
        final String from_user = c.getFrom();
        String message_type = c.getType();
        final String order_type = c.getOrdering();
        long time =c.getTime();

        GetTimeAgo getTimeAgo = new GetTimeAgo();

        long lastTime = Long.parseLong(String.valueOf(time));

        String lastSeenTime = getTimeAgo.getTimeAgo(lastTime,getTimeAgo);


        viewHolder.time.setText(lastSeenTime);

        if(from_user.equals(current_user_id)){
            viewHolder.messageText1.setVisibility(View.INVISIBLE);
            viewHolder.messageText.setVisibility(View.VISIBLE);
        }else{
            viewHolder.messageText.setVisibility(View.INVISIBLE);
            viewHolder.messageText1.setVisibility(View.VISIBLE);
        }
        switch (message_type) {
            case "text":
                viewHolder.messageText.setText(c.getMessage());
                viewHolder.messageText1.setText(c.getMessage());
                viewHolder.messageView.setVisibility(View.INVISIBLE);
                viewHolder.messageView1.setVisibility(View.INVISIBLE);
                viewHolder.mPdf.setVisibility(View.INVISIBLE);
                viewHolder.mcallback.setVisibility(View.INVISIBLE);
                viewHolder.treatmentBtn.setVisibility(View.INVISIBLE);
                viewHolder.infotext.setVisibility(View.INVISIBLE);
                break;
            case "image":
                viewHolder.messageText.setVisibility(View.INVISIBLE);
                viewHolder.messageText1.setVisibility(View.INVISIBLE);
                viewHolder.mPdf.setVisibility(View.INVISIBLE);
                viewHolder.mcallback.setVisibility(View.INVISIBLE);
                viewHolder.treatmentBtn.setVisibility(View.INVISIBLE);
                viewHolder.infotext.setVisibility(View.INVISIBLE);
                if (from_user.equals(current_user_id)) {
                    viewHolder.messageView1.setVisibility(View.VISIBLE);
                    viewHolder.messageView.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.messageView.setVisibility(View.VISIBLE);
                    viewHolder.messageView1.setVisibility(View.INVISIBLE);
                }

                Picasso.get().load(c.getMessage())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(viewHolder.messageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(c.getMessage()).into(viewHolder.messageView);

                    }
                });
                Picasso.get().load(c.getMessage())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(viewHolder.messageView1, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(c.getMessage()).into(viewHolder.messageView1);
                    }
                });

                break;
            case "pdf":
                viewHolder.mPdf.setVisibility(View.VISIBLE);
                viewHolder.messageView.setVisibility(View.INVISIBLE);
                viewHolder.messageView1.setVisibility(View.INVISIBLE);
                viewHolder.messageText.setVisibility(View.INVISIBLE);
                viewHolder.messageText1.setVisibility(View.INVISIBLE);
                viewHolder.mcallback.setVisibility(View.INVISIBLE);
                viewHolder.treatmentBtn.setVisibility(View.INVISIBLE);
                viewHolder.infotext.setVisibility(View.INVISIBLE);
                break;
            case "call back":
                viewHolder.mcallback.setVisibility(View.VISIBLE);
                viewHolder.messageView.setVisibility(View.INVISIBLE);
                viewHolder.messageView1.setVisibility(View.INVISIBLE);
                viewHolder.messageText.setVisibility(View.INVISIBLE);
                viewHolder.messageText1.setVisibility(View.INVISIBLE);
                viewHolder.treatmentBtn.setVisibility(View.INVISIBLE);
                viewHolder.infotext.setVisibility(View.INVISIBLE);
                break;
            default:
                viewHolder.infotext.setVisibility(View.VISIBLE);
                viewHolder.treatmentBtn.setVisibility(View.VISIBLE);
                viewHolder.mcallback.setVisibility(View.INVISIBLE);
                viewHolder.messageView.setVisibility(View.INVISIBLE);
                viewHolder.messageView1.setVisibility(View.INVISIBLE);
                viewHolder.messageText.setVisibility(View.INVISIBLE);
                viewHolder.messageText1.setVisibility(View.INVISIBLE);
                if (order_type.equals("ordered")) {
                    viewHolder.treatmentBtn.setText("Ordered");
                }
                //viewHolder.treatmentBtn.setAnimation(myAnim);

                break;
        }
        viewHolder.messageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(v.getContext(), ImageActivity.class);
                fullScreenIntent.setData(Uri.parse(c.getMessage()));
                v.getContext().startActivity(fullScreenIntent);
            }
        });
        viewHolder.messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(v.getContext(),ImageActivity.class);
                fullScreenIntent.setData(Uri.parse(c.getMessage()));
                v.getContext().startActivity(fullScreenIntent);
            }
        });

        viewHolder.treatmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order_type.equals("order")) {
                    Intent fullScreenIntent = new Intent(v.getContext(), PrescriptionActivity.class);
                    fullScreenIntent.putExtra("user_id", from_user);
                    fullScreenIntent.putExtra("medicine_id",medicine_id);
                    v.getContext().startActivity(fullScreenIntent);
                }
                else if (order_type.equals("ordered")){
                    Intent fullScreenIntent = new Intent(v.getContext(), OrderActivity.class);
                    v.getContext().startActivity(fullScreenIntent);
                }else {
                    Intent fullScreenIntent = new Intent(v.getContext(), PrescriptionActivity.class);
                    fullScreenIntent.putExtra("user_id", from_user);
                    fullScreenIntent.putExtra("medicine_id",medicine_id);
                    v.getContext().startActivity(fullScreenIntent);
                }
            }
        });
        viewHolder.mPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(c.getMessage()), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                v.getContext().startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
