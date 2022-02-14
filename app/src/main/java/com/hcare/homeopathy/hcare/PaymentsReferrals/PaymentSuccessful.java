package com.hcare.homeopathy.hcare.PaymentsReferrals;

import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.activeConsultations;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.consultations;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.customerOrders;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.followUp;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.newOrder;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.orders;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.recentConsultations;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.userConsultations;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.FirebaseClasses.ConsultationObject;
import com.hcare.homeopathy.hcare.FirebaseClasses.OrderObject;
import com.hcare.homeopathy.hcare.NewConsultation.DiseaseInfo;
import com.hcare.homeopathy.hcare.NewConsultation.Diseases;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PaymentSuccessful {

    DatabaseReference reference;
    static final int moneyToReferredByUsersWallet = 15;

    //Consultation
    public PaymentSuccessful(String userID, Diseases disease,
                             String patientIssue, int walletMoneyUsed) {

        reference = FirebaseDatabase.getInstance().getReference();
        String consultationID = new GenerateID().getID(Service.Consultation);

        ConsultationObject consultation = new ConsultationObject();
        try {
            consultation.setConsultationID(consultationID);
            consultation.setDisease(new DiseaseInfo(disease).getDiseaseName());
            consultation.setIssue(patientIssue);
            consultation.setTime(System.currentTimeMillis());
            consultation.setUserID(userID);
            consultation.setDoctorID("");
        } catch (Exception e) {e.printStackTrace();}

        //temporary store
        reference.child(activeConsultations)
                .child(userID).setValue(consultation);

        //permanent store - user
        reference.child(userConsultations)
                .child(userID)
                .child(consultationID)
                .setValue(consultation);

        //permanent store - recent
        reference.child(recentConsultations)
                .child(consultationID)
                .setValue(consultation);

        referralWalletOperations(userID, consultations, walletMoneyUsed);
    }

    //Renew Consultation
    public PaymentSuccessful(String userID, String doctorID) {
        reference = FirebaseDatabase.getInstance().getReference();
        String current_user_ref = "messages/" + userID +"/"+ doctorID;
        String chat_user_ref ="messages/" + doctorID +"/" + userID;

        DatabaseReference user_message_push =
                reference.child("messages").child(userID)
                        .child(doctorID).push();

        String push_id = user_message_push.getKey();

        Map messageMap = new HashMap();
        messageMap.put("message", "Follow up consultation");
        messageMap.put("seen", false);
        messageMap.put("type","text");
        messageMap.put("time", ServerValue.TIMESTAMP);
        messageMap.put("from", userID);

        Map messageUserMap = new HashMap();
        messageUserMap.put(current_user_ref +"/" + push_id,messageMap);
        messageUserMap.put(chat_user_ref +"/" + push_id,messageMap);


        ConsultationObject consultation = new ConsultationObject();
        consultation.setDoctorID(doctorID);
        consultation.setUserID(userID);
        consultation.setTime(System.currentTimeMillis());
        consultation.setIssue("Follow Up");
        consultation.setDisease("Follow Up");

        //Consultation table record
        reference.child(userConsultations)
                .child(userID).child(followUp)
                .child(doctorID).child(String.valueOf(consultation.getTime()))
                .setValue(consultation);

        reference.child(recentConsultations)
                .child(String.valueOf(consultation.getTime()))
                .setValue(consultation);

        //temporary store
        reference.child("Followup").child(doctorID)
                .child(userID).setValue(messageMap);

        reference.child("nextConsultdate")
                .setValue(getCalculatedDate("dd-MM-yyyy", 10));

        reference.updateChildren(messageUserMap,
                (databaseError, databaseReference) -> notification(userID, doctorID));
    }

    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }


    public void notification(String userID, String doctorID) {
        HashMap<String,String> notificationData = new HashMap<>();
        notificationData.put("from", userID);
        notificationData.put("type", "text");
        reference.child("notifications").child(doctorID)
                .push().setValue(notificationData);
    }


    //Order
    public PaymentSuccessful(OrderObject orderObject, String userID,
                             String doctorID, int walletMoneyUsed) {

        reference = FirebaseDatabase.getInstance().getReference();
        try {
            orderObject.setOrderID(new GenerateID().getID(Service.Order));
            orderObject.setUserID(userID);
            orderObject.setDoctorID(doctorID);

            reference.child(newOrder)
                    .child(orderObject.getOrderID())
                    .setValue(orderObject);

            reference.child(customerOrders).child(userID)
                    .child(orderObject.getOrderID())
                    .setValue(orderObject).addOnCompleteListener(task -> {

                reference.child("Doctors").child(doctorID)
                        .child("count").push().setValue(userID);

                reference.child("messages").child(userID).child(doctorID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    child.getRef().child("ordering").setValue("ordered");
                                    reference.child("messages").child(doctorID).child(userID)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                        child.getRef().child("ordering").setValue("ordered");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            });

            referralWalletOperations(userID, orders, walletMoneyUsed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void referralWalletOperations(String userID, String table, int moneyToRemove) {
        //remove money from wallet
        new WalletOperations().removeMoneyFromWallet(moneyToRemove);

        //Add to referredBy User's wallet and update Information
        reference.child("Users").child(userID).child("ReferredBy")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String referredByUserID = "";
                        try {
                            referredByUserID = snapshot.getValue(String.class);
                        } catch(Exception ignore) {
                            //not referred by anyone
                        }
                        if(!Objects.requireNonNull(referredByUserID).isEmpty()) {
                            //add 15 to the user's wallet
                            new WalletOperations()
                                    .addMoneyToWallet(referredByUserID, moneyToReferredByUsersWallet);

                            //update consultations
                            reference.child("Users")
                                    .child(referredByUserID).child("Referrals")
                                    .child(table)
                                    .child(String.valueOf(System.currentTimeMillis()))
                                    .setValue(userID);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

}
