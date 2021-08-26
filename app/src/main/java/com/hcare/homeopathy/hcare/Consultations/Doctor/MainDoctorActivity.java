package com.hcare.homeopathy.hcare.Consultations.Doctor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.FirebaseClasses.ChatObject;
import com.hcare.homeopathy.hcare.FirebaseClasses.ConsultationObject;
import com.hcare.homeopathy.hcare.Main.Doctors.DoctorDetailsFragment;
import com.hcare.homeopathy.hcare.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.theartofdev.edmodo.cropper.CropImage;
import org.json.JSONObject;
import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.hcare.homeopathy.hcare.Consultations.Doctor.Constants.GALLERY_PICK;
import static com.hcare.homeopathy.hcare.Consultations.Doctor.Constants.PICK_PDF_CODE;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.followUp;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.recentConsultations;
import static com.hcare.homeopathy.hcare.FirebaseClasses.FirebaseConstants.userConsultations;

public class MainDoctorActivity extends BaseActivity implements PaymentResultListener {

    private String doctorID, userID;
    int lastDay = 0;
    private DatabaseReference databaseRootReference,
            doctorReference, messagesReference, userReference;
    ProgressDialog mProgressDialog;
    List<ChatObject> list;
    boolean paymentSuccessful = false;
    String referredByUserID;
    int moneyInWallet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_doctor);

        doctorID = getIntent().getStringExtra("user_id");

        databaseRootReference = FirebaseDatabase.getInstance().getReference();

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();

        messagesReference = databaseRootReference
                .child("messages").child(userID).child(doctorID);

        userReference = databaseRootReference.child("Private_consult")
                .child(doctorID).child(userID);
        doctorReference = databaseRootReference.child("Private_consult")
                .child(userID).child(doctorID);

        setToolbar();
        loadMessages();
    }

    void setLastSeen() {
        try {
            messagesReference.orderByKey().limitToLast(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if(snapshot.exists()) {
                                    String key = "";
                                    for (DataSnapshot supportItem : snapshot.getChildren()) {
                                        key = supportItem.getKey();
                                    }
                                    assert key != null;
                                    ChatObject message = snapshot.child(key)
                                            .getValue(ChatObject.class);
                                    if (!Objects.requireNonNull(message).getFrom().equals(userID))
                                        messagesReference.child(key).child("seen").setValue(true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        } catch (Exception ignored) {}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment) != null)
            showOrHideFragment();
        else
            super.onBackPressed();
    }

    private void showOrHideFragment() {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                .beginTransaction();

        View chatBar = findViewById(R.id.chatBar);
        View consultAgain = findViewById(R.id.consultAgain);
        int fromDelta, toDelta;

        try {
            if (getSupportFragmentManager().findFragmentById(R.id.fragment) != null) {
                transaction.remove(Objects.requireNonNull
                        (getSupportFragmentManager()
                                .findFragmentById(R.id.fragment))).commit();

                fromDelta = chatBar.getHeight();
                toDelta = 0;

                findViewById(R.id.getUserMessage).setFocusableInTouchMode(true);
                findViewById(R.id.getUserMessage).setFocusable(true);

            } else {
                DoctorDetailsFragment fragment = new DoctorDetailsFragment();
                Bundle args = new Bundle();
                args.putString("user_id", doctorID);
                fragment.setArguments(args);

                fragment.setEnterTransition(new Fade().setDuration(300));
                fragment.setExitTransition(new Fade().setDuration(300));

                transaction.replace(R.id.fragment, fragment)
                        .commit();

                toDelta = chatBar.getHeight();
                fromDelta = 0;

                findViewById(R.id.getUserMessage).setFocusable(false);
            }

            TranslateAnimation animate =
                    new TranslateAnimation(0, 0,
                            fromDelta * 1.5f, toDelta * 1.5f);
            animate.setDuration(400);
            animate.setFillAfter(true);
            chatBar.startAnimation(animate);
            consultAgain.startAnimation(animate);

        } catch(Exception ignored) { }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(paymentSuccessful)
            checkoutSuccessfulFragment();
    }

    private void checkoutSuccessfulFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        View consultAgain = findViewById(R.id.consultAgain);
        View chatBar = findViewById(R.id.chatBar);
        TranslateAnimation animate =
                new TranslateAnimation(0, 0,
                        0,
                        300);
        animate.setDuration(0);
        animate.setFillAfter(true);
        consultAgain.startAnimation(animate);
        chatBar.startAnimation(animate);
        transaction
                .replace(R.id.fragment, new CheckoutSuccessfulFragment())
                .commit();

        //Adding money to the wallet of referredBy user
        FirebaseDatabase.getInstance().getReference().child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                referredByUserID = snapshot.child("ReferredBy").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        FirebaseDatabase.getInstance().getReference().child("Users").child(referredByUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                moneyInWallet = Integer.parseInt(snapshot.child("Wallet").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        moneyInWallet += 15;
                        FirebaseDatabase.getInstance()
                                .getReference().child("Users").child(referredByUserID).child("Wallet").setValue(moneyInWallet);
                    }
                },
                5000
        );
    }

    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseRootReference.child("Doctors").child(doctorID).
                addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            ((TextView) findViewById(R.id.title))
                                    .setText(MessageFormat.format("{0}  {1}",
                                            "Dr.", Objects.requireNonNull(
                                                    dataSnapshot.child("name").getValue()).toString()));

                            setConsultAgainButton();
                            setLastSeen();

                        } catch (Exception e) {
                            ((TextView) findViewById(R.id.title)).setText("Doctor not Found");
                            findViewById(R.id.consultAgain).setVisibility(View.GONE);
                            findViewById(R.id.chatBar).setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

        findViewById(R.id.details).setOnClickListener(v -> showOrHideFragment());
    }

    private void setConsultAgainButton() {
        CardView consultAgain = findViewById(R.id.consultAgain);
        doctorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("nextConsultdate")) {
                    String consultdate = Objects.requireNonNull(
                            dataSnapshot.child("nextConsultdate").getValue()).toString();

                    long diff = 0;
                    try {
                        @SuppressLint("SimpleDateFormat") DateFormat df =
                                new SimpleDateFormat("dd-MM-yyyy");
                        Date date1 = new Date();
                        Date date2 = df.parse(consultdate);
                        diff = Objects.requireNonNull(date2).getTime() - date1.getTime();
                        Log.i("diff", String.valueOf(diff));
                    } catch (ParseException e) {
                        Log.e("TEST", "Exception", e);
                    }

                    //consult date already set 10 days after
                    if (diff<0) {
                        databaseRootReference.child("Users").child(userID)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        phoneNumber =  (String)
                                                dataSnapshot.child("phone number").getValue();
                                        eMail  = (String) dataSnapshot.child("email").getValue();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                });
                        consultAgain.setVisibility(View.VISIBLE);
                    }
                    else
                    consultAgain.setVisibility(View.GONE);
                } else {
                    doctorReference.child("nextConsultdate")
                            .setValue(getCalculatedDate("dd-MM-yyyy", 10));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        consultAgain.setOnClickListener(v -> {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Payment Loading");
            mProgressDialog.setMessage("Please wait");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            startPayment();
        });
    }

    private void loadMessages() {
        RecyclerView mMessagesList = findViewById(R.id.messages_list);
        list = new ArrayList<>();
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);

        mLinearLayout.setStackFromEnd(true);
        mMessagesList.setLayoutManager(mLinearLayout);
        ChatAdapter adapter = new ChatAdapter(list, this);
        mMessagesList.setAdapter(adapter);

        DatabaseReference chat = databaseRootReference
                .child("messages").child(userID).child(doctorID);
        chat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                try {
                    ChatObject message = dataSnapshot.getValue(ChatObject.class);
                    assert message != null;
                    message.setMessageID(dataSnapshot.getKey());

                    if (lastDay == new GetTime(Objects.requireNonNull(message)
                            .getTime()).getDays())
                        message.setTime(0);
                    else
                        lastDay = new GetTime(message.getTime()).getDays();

                    list.add(message);
                    adapter.notifyDataSetChanged();
                } catch(Exception ignored){ }
                mMessagesList.scrollToPosition(list.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                try {
                    ChatObject message = dataSnapshot.getValue(ChatObject.class);
                    assert message != null;
                    message.setMessageID(dataSnapshot.getKey());

                    Log.i("message", message.toString());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        list.removeIf(e -> e.getMessageID().equals(message.getMessageID()));
                    }
                    adapter.notifyDataSetChanged();
                } catch(Exception ignored){ }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        chat.keepSynced(true);
    }

    public void sendMessage(View view) {
        String message = ((EditText) findViewById(R.id.getUserMessage)).getText().toString();
        if (!message.isEmpty()) {
            String current_user_ref = "messages/" + userID +"/"+ doctorID;
            String chat_user_ref ="messages/" + doctorID +"/" + userID;

            DatabaseReference user_message_push = messagesReference.push();
            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen", false);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);
            messageMap.put("from", userID);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref +"/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref +"/" + push_id, messageMap);

            ((EditText) findViewById(R.id.getUserMessage)).setText("");
            databaseRootReference.updateChildren(messageUserMap,
                    (databaseError, databaseReference) -> notification());
            loadMessages();

            userReference.child("time").setValue(ServerValue.TIMESTAMP);
            doctorReference.child("time").setValue(ServerValue.TIMESTAMP);
        }
    }

    public void attachFile(View view) {
        PopupMenu popupMenu = new PopupMenu(MainDoctorActivity.this, findViewById(R.id.attachFile));
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getItemId()==R.id.Gallery_btn){

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"), GALLERY_PICK);
                userReference.child("time").setValue(ServerValue.TIMESTAMP);
                doctorReference.child("time").setValue(ServerValue.TIMESTAMP);
            }

            if(item.getItemId()==R.id.File_btn) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
                userReference.child("time").setValue(ServerValue.TIMESTAMP);
                doctorReference.child("time").setValue(ServerValue.TIMESTAMP);
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode ==GALLERY_PICK && resultCode ==RESULT_OK) {
            Uri imageUri =data.getData();
            CropImage.activity(imageUri).start(this);
        }

        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }
            else
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                final StorageReference filepath =  FirebaseStorage.getInstance()
                        .getReference().child("messages")
                        .child(userID).child(random()+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(
                        task -> filepath.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Log.d(TAG, "onSuccess: uri= "+ uri.toString());

                            String download_Url = uri.toString();
                            String current_user_ref = "messages/" + userID +"/"+ doctorID;
                            String chat_user_ref ="messages/" + doctorID +"/" + userID;

                            DatabaseReference user_message_push = databaseRootReference.child("messages")
                                    .child(userID).child(doctorID).push();

                            String push_id = user_message_push.getKey();

                            Map messageMap = new HashMap();
                            messageMap.put("message",download_Url);
                            messageMap.put("seen", false);
                            messageMap.put("type","image");
                            messageMap.put("time",ServerValue.TIMESTAMP);
                            messageMap.put("from", userID);


                            Map messageUserMap = new HashMap();
                            messageUserMap.put(current_user_ref +"/" + push_id,messageMap);
                            messageUserMap.put(chat_user_ref +"/" + push_id,messageMap);


                            databaseRootReference.updateChildren(messageUserMap,
                                    (databaseError, databaseReference) -> notification());
                        }));
            }
        }
    }

    private void uploadFile(final Uri data) {
        String displayName = null;
        if ((String.valueOf(data)).startsWith("content://")) {
            try (Cursor cursor = getContentResolver()
                    .query(data, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        } else
            displayName = new File(String.valueOf(data)).getName();

        final StorageReference sRef =  FirebaseStorage.getInstance()
                .getReference().child("Files")
                .child(userID).child(displayName);

        sRef.putFile(data).addOnCompleteListener(task -> sRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    String download_Url = uri.toString();

                    String current_user_ref = "messages/" + userID +"/"+ doctorID;
                    String chat_user_ref ="messages/" + doctorID +"/" + userID;

                    DatabaseReference user_message_push = databaseRootReference.child("messages")
                            .child(userID).child(doctorID).push();

                    String push_id = user_message_push.getKey();

                    Map messageMap = new HashMap();
                    messageMap.put("message",download_Url);
                    messageMap.put("seen", false);
                    messageMap.put("type","pdf");
                    messageMap.put("time",ServerValue.TIMESTAMP);
                    messageMap.put("from", userID);


                    Map messageUserMap = new HashMap();
                    messageUserMap.put(current_user_ref +"/" + push_id,messageMap);
                    messageUserMap.put(chat_user_ref +"/" + push_id,messageMap);


                    databaseRootReference.updateChildren(messageUserMap,
                            (databaseError, databaseReference) -> notification());
                }));
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseRootReference.child("notifications").child(userID).removeValue();
        databaseRootReference.child("Accepting")
                .child(userID).removeValue();
    }

    public void notification() {
        HashMap<String,String> notificationData = new HashMap<>();
        notificationData.put("from", userID);
        notificationData.put("type", "text");
        databaseRootReference.child("notifications").child(doctorID)
                .push().setValue(notificationData);
    }

    public String random() {
        Random generator =  new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i< randomLength; i++) {
            tempChar = (char) (generator.nextInt(96)+32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    String phoneNumber, eMail;
    public void startPayment() {
        final AppCompatActivity activity = this;
        final Checkout co = new Checkout();
        try {

            JSONObject options = new JSONObject();
            options.put("name", "HCare");
            options.put("description", "discount applied");
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            options.put("amount", "14900");

            JSONObject preFill = new JSONObject();
            preFill.put("email", eMail);
            preFill.put("contact", phoneNumber);

            options.put("prefill", preFill);
            co.setImage(R.drawable.logo_green);
            co.open(activity, options);
        }
        catch (Exception e) {
            Toast.makeText(activity, "Error in payment", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendRequest() {
        String current_user_ref = "messages/" + userID +"/"+ doctorID;
        String chat_user_ref ="messages/" + doctorID +"/" + userID;

        DatabaseReference user_message_push =
                databaseRootReference.child("messages").child(userID)
                        .child(doctorID).push();

        String push_id = user_message_push.getKey();

        Map messageMap = new HashMap();
        messageMap.put("message", "Follow up consultation");
        messageMap.put("seen", false);
        messageMap.put("type","text");
        messageMap.put("time",ServerValue.TIMESTAMP);
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
        databaseRootReference.child(userConsultations)
                .child(userID).child(followUp)
                .child(doctorID).child(String.valueOf(consultation.getTime()))
                .setValue(consultation);

        databaseRootReference.child(recentConsultations)
                .child(String.valueOf(consultation.getTime()))
                .setValue(consultation);

        //temporary store
        databaseRootReference.child("Followup").child(doctorID)
                .child(userID).setValue(messageMap);

        doctorReference.child("nextConsultdate")
                .setValue(getCalculatedDate("dd-MM-yyyy", 10));

        databaseRootReference.updateChildren(messageUserMap,
                (databaseError, databaseReference) -> notification());
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            mProgressDialog.dismiss();

            paymentSuccessful = true;
            sendRequest();
        } catch (Exception e) {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO
    @Override
    public void onPaymentError(int code, String response) {
        try {
            mProgressDialog.dismiss();

            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
