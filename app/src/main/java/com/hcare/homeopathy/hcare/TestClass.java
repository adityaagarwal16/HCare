package com.hcare.homeopathy.hcare;

import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TestClass {

    DatabaseReference databaseRootReference;
    String userID, doctorID;

    public TestClass() {
        databaseRootReference = FirebaseDatabase.getInstance().getReference();
        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();
        doctorID = "AQtq6nwXN6cjsvm0GqDdB49rH8u2";
    }

    public void doctorMessage() {

            String current_user_ref = "messages/" + userID +"/"+ doctorID;
            String chat_user_ref ="messages/" + doctorID +"/" + userID;

            DatabaseReference user_message_push = databaseRootReference
                    .child("messages")
                    .child(userID).child(doctorID)
                    .push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message","Hello");
            messageMap.put("seen", false);
            messageMap.put("type","text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", doctorID);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref +"/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref +"/" + push_id, messageMap);


            databaseRootReference.updateChildren(messageUserMap,
                    (databaseError, databaseReference) -> notification());

    }

    public void notification() {
        HashMap<String,String> notificationData = new HashMap<>();
        notificationData.put("from", userID);
        notificationData.put("type", "text");
        databaseRootReference.child("notifications").child(doctorID)
                .push().setValue(notificationData);
    }

}
