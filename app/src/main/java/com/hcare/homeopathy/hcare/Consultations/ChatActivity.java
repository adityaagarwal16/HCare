package com.hcare.homeopathy.hcare.Consultations;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.hcare.homeopathy.hcare.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

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

public class ChatActivity extends AppCompatActivity implements PaymentResultListener {

    private String userID;

    private TextView mTitleView;
    private DatabaseReference mRootref;
    private String mCurrentUserId;

    private ImageView mChatAddBtn,consultagain;

    final static int PICK_PDF_CODE = 2342;
    private RecyclerView mMessagesList;
    private final List<Messages> messageList= new ArrayList<>();
    private static final int GALLERY_PICK =1;

    private DatabaseReference userRef;
    private DatabaseReference a,h,z,followup ;
    private DatabaseReference NotificationData,notice;
    private StorageReference mimagestorage;

    private int diff;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userID = getIntent().getStringExtra("user_id");
        assert userID != null;
        Log.i("userID", userID);
        mRootref = FirebaseDatabase.getInstance().getReference();

        setToolbar();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mimagestorage = FirebaseStorage.getInstance().getReference();
        mChatAddBtn = findViewById(R.id.chat_add_btn);
        ImageView mChatSendBtn = findViewById(R.id.chat_send_btn);
        consultagain = findViewById(R.id.consultagainbtn);
        mTitleView = findViewById(R.id.consultagaintxt);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(" payment Loading ");
        mProgressDialog.setMessage("Please wait ");
        mProgressDialog.setCanceledOnTouchOutside(false);

        mMessagesList = findViewById(R.id.messages_list);
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);
        MessageAdapter mAdapter = new MessageAdapter(messageList);
        mMessagesList.setAdapter(mAdapter);

        userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mAuth.getCurrentUser().getUid());



        followup = FirebaseDatabase.getInstance().getReference().child("Followup");
        notice = FirebaseDatabase.getInstance().getReference().child("Accepting");
        a = mRootref.child("Private_consult").child(mCurrentUserId).child(userID);
        h = mRootref.child("messages").child(mCurrentUserId).child(userID);
        z = mRootref.child("Private_consult").child(userID).child(mCurrentUserId);
        NotificationData = FirebaseDatabase.getInstance().getReference().child("notifications");

        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("nextConsultdate")) {

                    String consultdate = Objects.requireNonNull(
                            dataSnapshot.child("nextConsultdate").getValue()).toString();

                    try {
                        @SuppressLint("SimpleDateFormat") DateFormat df =
                                new SimpleDateFormat("dd-MM-yyyy");
                        Date date1 = new java.util.Date();
                        Date date2 = df.parse(consultdate);
                        diff = (int) (Objects.requireNonNull(date2).getTime() - date1.getTime());
                    } catch (ParseException e) {
                        Log.e("TEST", "Exception", e);
                    }

                    int number = 864000000;
                    if (consultdate.equals(getCalculatedDate("dd-MM-yyyy",0))){
                       consultagain.setVisibility(View.VISIBLE);
                        mTitleView.setVisibility(View.VISIBLE);
                    } else if (diff > number){
                        Log.d("tes12345","smaller");
                        consultagain.setVisibility(View.VISIBLE);
                        mTitleView.setVisibility(View.VISIBLE);
                    }
                    else if (diff < number){
                        Log.d("tes12345","greater");
                      consultagain.setVisibility(View.GONE);
                        mTitleView.setVisibility(View.GONE);
                    } else {
                       consultagain.setVisibility(View.GONE);
                        mTitleView.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        consultagain.setOnClickListener(v -> {
            mProgressDialog.show();
            startPayment();
        });
        loadMessage();


        mChatSendBtn.setOnClickListener(v -> {
            sendMessage();
            z.child("time").setValue(ServerValue.TIMESTAMP);
            a.child("time").setValue(ServerValue.TIMESTAMP);

        });

        mChatAddBtn.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(ChatActivity.this,mChatAddBtn);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if(item.getItemId()==R.id.Gallery_btn){

                    Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);
                    z.child("time").setValue(ServerValue.TIMESTAMP);
                    a.child("time").setValue(ServerValue.TIMESTAMP);
                }
                if(item.getItemId()==R.id.File_btn) {
                    Intent intent = new Intent();
                    intent.setType("application/pdf");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
                    z.child("time").setValue(ServerValue.TIMESTAMP);
                    a.child("time").setValue(ServerValue.TIMESTAMP);
                }
                return false;
            });
            popupMenu.show();
        });


    }

    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mRootref.child("Doctors").child(userID).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ((TextView) findViewById(R.id.title))
                        .setText(MessageFormat.format("{0}  {1}",
                        "Dr.", Objects.requireNonNull(
                                dataSnapshot.child("name").getValue()).toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        findViewById(R.id.details).setOnClickListener(v -> {
            Intent profileIntent = new Intent(ChatActivity.this, DoctorDetailsActivity.class);
            profileIntent.putExtra("user_id", userID);
            startActivity(profileIntent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode ==GALLERY_PICK && resultCode ==RESULT_OK){
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

        if(requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                final StorageReference filepath = mimagestorage.child("messages")
                        .child(mCurrentUserId).child(random()+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(
                        task -> filepath.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Log.d(TAG, "onSuccess: uri= "+ uri.toString());

                            String download_Url = uri.toString();
                            String current_user_ref = "messages/" + mCurrentUserId +"/"+ userID;
                            String chat_user_ref ="messages/" + userID +"/" +mCurrentUserId;

                            DatabaseReference user_message_push = mRootref.child("messages")
                                    .child(mCurrentUserId).child(userID).push();

                            String push_id = user_message_push.getKey();

                            Map messageMap = new HashMap();
                            messageMap.put("message",download_Url);
                            messageMap.put("seen", false);
                            messageMap.put("type","image");
                            messageMap.put("time",ServerValue.TIMESTAMP);
                            messageMap.put("from",mCurrentUserId);


                            Map messageUserMap = new HashMap();
                            messageUserMap.put(current_user_ref +"/" + push_id,messageMap);
                            messageUserMap.put(chat_user_ref +"/" + push_id,messageMap);


                            mRootref.updateChildren(messageUserMap,
                                    (databaseError, databaseReference) -> {
                                notification();
                                h.orderByChild("seen").equalTo(false)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                    child.getRef().child("seen").setValue(true);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                                        });
                            });
                        }));
            }
        }
    }

    private void uploadFile(final Uri data) {

        final StorageReference sRef =mimagestorage.child("Files")
                .child(mCurrentUserId).child(random()+ ".pdf");
        sRef.putFile(data).addOnCompleteListener(task -> sRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    String download_Url = uri.toString();

                    String current_user_ref = "messages/" + mCurrentUserId +"/"+ userID;
                    String chat_user_ref ="messages/" + userID +"/" +mCurrentUserId;

                    DatabaseReference user_message_push = mRootref.child("messages")
                            .child(mCurrentUserId).child(userID).push();

                    String push_id = user_message_push.getKey();

                    Map messageMap = new HashMap();
                    messageMap.put("message",download_Url);
                    messageMap.put("seen", false);
                    messageMap.put("type","pdf");
                    messageMap.put("time",ServerValue.TIMESTAMP);
                    messageMap.put("from",mCurrentUserId);


                    Map messageUserMap = new HashMap();
                    messageUserMap.put(current_user_ref +"/" + push_id,messageMap);
                    messageUserMap.put(chat_user_ref +"/" + push_id,messageMap);


                    mRootref.updateChildren(messageUserMap, (databaseError, databaseReference) -> {
                        notification();
                        h.orderByChild("seen").equalTo(false)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    child.getRef().child("seen").setValue(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });
                    });
                }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        a.child("list").setValue("online");
        userRef.child("status").setValue("online");
        NotificationData.child(mCurrentUserId).removeValue();
        notice.child(mCurrentUserId).removeValue();
        h.orderByChild("seen").equalTo(false).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().child("seen").setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        a.child("list").setValue("offline");
        userRef.child("status").setValue("offline");

        h.orderByChild("seen").equalTo(false).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    child.getRef().child("seen").setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadMessage() {
        h.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Messages message = dataSnapshot.getValue(Messages.class);
                messageList.add(message);
                mMessagesList.scrollToPosition(messageList.size()-1);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {



            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {
        String message = ((EditText) findViewById(R.id.userMessage))
                .getText().toString();

        if (!TextUtils.isEmpty(message)) {
            String current_user_ref = "messages/" + mCurrentUserId +"/"+ userID;
            String chat_user_ref ="messages/" + userID +"/" +mCurrentUserId;

            DatabaseReference user_message_push = mRootref.child("messages")
                    .child(mCurrentUserId).child(userID).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen", false);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);
            messageMap.put("from",mCurrentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref +"/" + push_id,messageMap);
            messageUserMap.put(chat_user_ref +"/" + push_id,messageMap);

            ((EditText) findViewById(R.id.userMessage)).setText("");
            mRootref.updateChildren(messageUserMap, (databaseError, databaseReference) -> {
                notification();
                h.orderByChild("seen").equalTo(false).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            child.getRef().child("seen").setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

            });

        }
    }

    public void notification() {
        HashMap<String,String> notificationData = new HashMap<>();
        notificationData.put("from",mCurrentUserId);
        notificationData.put("type","text");
        NotificationData.child(userID).push().setValue(notificationData);
    }

    public static String random() {
        Random generator =  new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i<randomLength; i++){
            tempChar = (char)(generator.nextInt(96)+32);
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
        mProgressDialog.dismiss();
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final AppCompatActivity activity = this;

        final Checkout co = new Checkout();

        try {
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    phoneNumber =  (String) dataSnapshot.child("phone number").getValue();
                    eMail  = (String) dataSnapshot.child("email").getValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            JSONObject options = new JSONObject();
            options.put("name", "Hcare");
            options.put("description", "discount applied");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.drawable.hcarehori);
            options.put("currency", "INR");
            options.put("amount", "15000");

            JSONObject preFill = new JSONObject();
            preFill.put("email", eMail);
            preFill.put("contact", phoneNumber);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            sendRequest();
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendRequest() {
        String current_user_ref = "messages/" + mCurrentUserId +"/"+ userID;
        String chat_user_ref ="messages/" + userID +"/" +mCurrentUserId;

        DatabaseReference user_message_push =
                mRootref.child("messages").child(mCurrentUserId).child(userID).push();

        String push_id = user_message_push.getKey();

        Map messageMap = new HashMap();
        messageMap.put("message","Follow up consultation");
        messageMap.put("seen", false);
        messageMap.put("type","text");
        messageMap.put("time",ServerValue.TIMESTAMP);
        messageMap.put("from",mCurrentUserId);

        Map messageUserMap = new HashMap();
        messageUserMap.put(current_user_ref +"/" + push_id,messageMap);
        messageUserMap.put(chat_user_ref +"/" + push_id,messageMap);
        followup.child(userID).child(mCurrentUserId).setValue(messageMap);
        a.child("nextConsultdate").setValue(getCalculatedDate("dd-MM-yyyy", 10));
        // mChatMessageView.setText("");
        mRootref.updateChildren(messageUserMap, (databaseError, databaseReference) -> {
            notification();
            h.orderByChild("seen").equalTo(false).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        child.getRef().child("seen").setValue(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }

            });
        });
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
    }
}
