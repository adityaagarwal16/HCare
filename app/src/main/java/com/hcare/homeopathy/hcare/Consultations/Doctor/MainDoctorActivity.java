package com.hcare.homeopathy.hcare.Consultations.Doctor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class MainDoctorActivity extends BaseActivity implements PaymentResultListener {

    private String doctorID, userID;
    int lastDay = 0;
    private DatabaseReference databaseRootReference, userRef,
            doctorReference, messagesReference, userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_doctor);

        doctorID = getIntent().getStringExtra("user_id");
        databaseRootReference = FirebaseDatabase.getInstance().getReference();

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();

        setToolbar();

        userRef = databaseRootReference.child("Users").child(userID);
        messagesReference = databaseRootReference.child("messages").child(userID).child(doctorID);

        userReference = databaseRootReference.child("Private_consult").child(doctorID).child(userID);
        doctorReference = databaseRootReference.child("Private_consult").child(userID).child(doctorID);

        loadMessages();
        setConsultAgainButton();

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
                    new TranslateAnimation(0, 0, fromDelta, toDelta);
            animate.setDuration(400);
            animate.setFillAfter(true);
            chatBar.startAnimation(animate);
            consultAgain.startAnimation(animate);

        } catch(Exception ignored) { }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportFragmentManager().findFragmentById(R.id.fragment) != null) {
            showOrHideFragment();
        }
    }

    private void setToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseRootReference.child("Doctors").child(doctorID).
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

                    int diff = 0;
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
                    if (consultdate.equals(getCalculatedDate("dd-MM-yyyy",0))
                            || diff > number)
                        consultAgain.setVisibility(View.VISIBLE);
                    else
                        consultAgain.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        consultAgain.setOnClickListener(v -> {
            ProgressDialog mProgressDialog =
                    new ProgressDialog(this);
            mProgressDialog.setTitle(" Payment Loading ");
            mProgressDialog.setMessage("Please wait ");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            startPayment();
        });
    }

    private void loadMessages() {
        RecyclerView mMessagesList = findViewById(R.id.messages_list);
        List<ChatObject> list = new ArrayList<>();
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);

        mLinearLayout.setStackFromEnd(true);
        mMessagesList.setLayoutManager(mLinearLayout);
        mMessagesList.setAdapter(new ChatAdapter(list, this));

        messagesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                ChatObject message = dataSnapshot.getValue(ChatObject.class);
                try {
                    if(lastDay == new GetTime(Objects.requireNonNull(message)
                            .getTime()).getDays())
                        message.setTime(0);
                    else
                        lastDay = new GetTime(message.getTime()).getDays();
                    /* Log.i("from", message.getFrom());
                    Log.i("message", message.getMessage());
                    Log.i("order", message.getOrdering());
                    Log.i("type", message.getType());
                    Log.i("seen", String.valueOf(message.getSeen()));
                    Log.i("image", message.getImage());
                    Log.i("medicine", message.getMedicineId());*/

                } catch (Exception ignored) { }

                list.add(message);
                mMessagesList.scrollToPosition(list.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void sendMessage(View view) {
        String message = ((EditText) findViewById(R.id.getUserMessage)).getText().toString();

        if (!message.isEmpty()) {
            String current_user_ref = "messages/" + userID +"/"+ doctorID;
            String chat_user_ref ="messages/" + doctorID +"/" + userID;

            DatabaseReference user_message_push = databaseRootReference
                    .child("messages")
                    .child(userID).child(doctorID)
                    .push();

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
        }
        loadMessages();

        userReference.child("time").setValue(ServerValue.TIMESTAMP);
        doctorReference.child("time").setValue(ServerValue.TIMESTAMP);
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
        doctorReference.child("list").setValue("online");
        userRef.child("status").setValue("online");

        databaseRootReference.child("notifications").child(userID).removeValue();
        databaseRootReference.child("Accepting")
                .child(userID).removeValue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        doctorReference.child("list").setValue("offline");
        userRef.child("status").setValue("offline");
    }

    public void notification() {
        HashMap<String,String> notificationData = new HashMap<>();
        notificationData.put("from", userID);
        notificationData.put("type","text");
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
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    phoneNumber =  (String) dataSnapshot.child("phone number").getValue();
                    eMail  = (String) dataSnapshot.child("email").getValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

            JSONObject options = new JSONObject();
            options.put("name", "Hcare");
            options.put("description", "discount applied");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.drawable.logo_green);
            options.put("currency", "INR");
            options.put("amount", "15000");

            JSONObject preFill = new JSONObject();
            preFill.put("email", eMail);
            preFill.put("contact", phoneNumber);

            options.put("prefill", preFill);

            co.open(activity, options);
        }
        catch (Exception e) {
            Toast.makeText(activity, "Error in payment", Toast.LENGTH_SHORT).show();
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
        String current_user_ref = "messages/" + userID +"/"+ doctorID;
        String chat_user_ref ="messages/" + doctorID +"/" + userID;

        DatabaseReference user_message_push =
                databaseRootReference.child("messages").child(userID).child(doctorID).push();

        String push_id = user_message_push.getKey();

        Map messageMap = new HashMap();
        messageMap.put("message","Follow up consultation");
        messageMap.put("seen", false);
        messageMap.put("type","text");
        messageMap.put("time",ServerValue.TIMESTAMP);
        messageMap.put("from", userID);

        Map messageUserMap = new HashMap();
        messageUserMap.put(current_user_ref +"/" + push_id,messageMap);
        messageUserMap.put(chat_user_ref +"/" + push_id,messageMap);
        databaseRootReference.child("Followup").child(doctorID)
                .child(userID).setValue(messageMap);
        doctorReference.child("nextConsultdate").setValue(getCalculatedDate("dd-MM-yyyy", 10));
        // mChatMessageView.setText("");
        databaseRootReference.updateChildren(messageUserMap,
                (databaseError, databaseReference) -> notification());
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
    }


}
