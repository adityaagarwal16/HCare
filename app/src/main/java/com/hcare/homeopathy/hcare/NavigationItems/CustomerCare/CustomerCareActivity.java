package com.hcare.homeopathy.hcare.NavigationItems.CustomerCare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.Consultations.Doctor.ChatAdapter;
import com.hcare.homeopathy.hcare.Consultations.Doctor.ChatObject;
import com.hcare.homeopathy.hcare.Consultations.Doctor.GetTime;
import com.hcare.homeopathy.hcare.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class CustomerCareActivity extends BaseActivity {

    private DatabaseReference rootReference;
    private String mCurrentUserId;

    int lastDay = 0;

    final static int PICK_PDF_CODE = 2342;
    private RecyclerView mMessagesList;
    private final List<ChatObject> messageList= new ArrayList<>();
    private ChatAdapter mAdapter;
    private static final int GALLERY_PICK =1;
    private DatabaseReference h;

    private StorageReference imageStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_care);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rootReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        imageStorage = FirebaseStorage.getInstance().getReference();

      //  mCallBtn =(ImageButton) findViewById(R.id.CallBtn);


        mAdapter = new ChatAdapter(messageList, this);

        mMessagesList = findViewById(R.id.messages_list);
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);

        mLinearLayout.setStackFromEnd(true);
        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);
        mMessagesList.setAdapter(mAdapter);

        h = rootReference.child("Customercare").child(mCurrentUserId);
        loadMessage();
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

    public void attachFile(View view) {
        PopupMenu popupMenu = new PopupMenu(CustomerCareActivity.this,
                findViewById(R.id.attachFile));
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getItemId()==R.id.Gallery_btn){
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);

            }
            if(item.getItemId()==R.id.File_btn) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser
                        (intent, "Select Picture"), PICK_PDF_CODE);
            }
            return false;
        });
        popupMenu.show();
    }

    public void sendMessage(View view) {
        String message = ((EditText) findViewById(R.id.getUserMessage)).getText().toString();

        if (!TextUtils.isEmpty(message)){
            String current_user_ref = "Customercare/" + mCurrentUserId ;


            DatabaseReference user_message_push =
                    rootReference.child("Customercare").child(mCurrentUserId).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen", false);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);
            messageMap.put("from",mCurrentUserId);


            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref +"/" + push_id,messageMap);

            ((EditText) findViewById(R.id.getUserMessage)).setText("");
            rootReference.updateChildren(messageUserMap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode ==GALLERY_PICK && resultCode ==RESULT_OK){

            Uri imageUri =data.getData();

            CropImage.activity(imageUri).start(this);
        }
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                StorageReference filepath = imageStorage.child("Customercare")
                        .child(mCurrentUserId).child(random()+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {
                        Uri downloadUri = Objects.requireNonNull(task.getResult()).getUploadSessionUri();
                        String download_Url = Objects.requireNonNull(downloadUri).toString();
                        // mUserrDatabase.child("image").setValue(download_Url);

                        String current_user_ref = "Customercare/" + mCurrentUserId;

                        DatabaseReference user_message_push = rootReference.child("Customercare").child(mCurrentUserId).push();

                        String push_id = user_message_push.getKey();

                        Map messageMap = new HashMap();
                        messageMap.put("message", download_Url);
                        messageMap.put("seen", false);
                        messageMap.put("type", "image");
                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from", mCurrentUserId);


                        Map messageUserMap = new HashMap();
                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);


                        rootReference.updateChildren(messageUserMap);

                    }
                });


            }
        }

    }

    private void uploadFile(final Uri data) {

        StorageReference sRef = imageStorage.child("Files").child(mCurrentUserId).child(random()+ ".pdf");
        sRef.putFile(data).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Uri downloadUri = Objects.requireNonNull(task.getResult()).getUploadSessionUri();
                String download_Url = Objects.requireNonNull(downloadUri).toString();
                // mUserrDatabase.child("image").setValue(download_Url);

                String current_user_ref = "Customercare/" + mCurrentUserId ;

                DatabaseReference user_message_push = rootReference.child("Customercare").child(mCurrentUserId).push();

                String push_id = user_message_push.getKey();

                Map messageMap = new HashMap();
                messageMap.put("message",download_Url);
                messageMap.put("seen", false);
                messageMap.put("type","pdf");
                messageMap.put("time",ServerValue.TIMESTAMP);
                messageMap.put("from",mCurrentUserId);


                Map messageUserMap = new HashMap();
                messageUserMap.put(current_user_ref +"/" + push_id,messageMap);

                rootReference.updateChildren(messageUserMap);
            }
        });

    }

    private void loadMessage() {
        h.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                try {
                    ChatObject message = dataSnapshot.getValue(ChatObject.class);
                    Log.i("message", s);

                    if (lastDay == new GetTime(Objects.requireNonNull(message)
                            .getTime()).getDays())
                        message.setTime(0);
                    else
                        lastDay = new GetTime(message.getTime()).getDays();
                    messageList.add(message);
                    mAdapter.notifyDataSetChanged();
                } catch(Exception ignored){ }

                mMessagesList.scrollToPosition(messageList.size()-1);
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

    public static String random(){
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

}
