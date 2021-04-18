package com.hcare.homeopathy.hcare.Mainmenus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hcare.homeopathy.hcare.Consultation.MessageAdapter;
import com.hcare.homeopathy.hcare.Consultation.Messages;
import com.hcare.homeopathy.hcare.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomercareActivity extends AppCompatActivity {

    private Toolbar mChatToolbar;

    private TextView mTitleView;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootref;
    private String mCurrentUserId,phone_number;

    private ImageButton mChatAddBtn;
    private ImageButton mChatSendBtn;
    private EditText mChatMessageView;
    private ImageButton mPrescribeBtn;
    private ImageButton mCallBtn;
    final static int PICK_PDF_CODE = 2342;
    private RecyclerView mMessagesList;
    private final List<Messages> messageList= new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;
    private static final int GALLERY_PICK =1;
    private DatabaseReference userRef;
    private DatabaseReference h;

    private StorageReference mimagestorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customercare);

        setTitle("Customer Service");

        mRootref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mimagestorage = FirebaseStorage.getInstance().getReference();
        mChatAddBtn =(ImageButton) findViewById(R.id.chat_add_btn);
        mChatSendBtn =(ImageButton) findViewById(R.id.chat_send_btn);
        mChatMessageView =(EditText) findViewById(R.id.chat_message_view);

      //  mCallBtn =(ImageButton) findViewById(R.id.CallBtn);


        mAdapter =new MessageAdapter(messageList);

        mMessagesList =(RecyclerView) findViewById(R.id.messages_list);
        mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);
        mMessagesList.setAdapter(mAdapter);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());




        h = mRootref.child("Customercare").child(mCurrentUserId);



        loadMessage();





        mChatSendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendMessage();

            }
        });
        mChatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(CustomercareActivity.this,mChatAddBtn);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
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
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);

                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


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
                StorageReference filepath = mimagestorage.child("Customercare").child(mCurrentUserId).child(random()+".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){
                            Uri downloadUri = task.getResult().getUploadSessionUri();
                            String download_Url = downloadUri.toString();
                            // mUserrDatabase.child("image").setValue(download_Url);

                            String current_user_ref = "Customercare/" + mCurrentUserId ;

                            DatabaseReference user_message_push = mRootref.child("Customercare").child(mCurrentUserId).push();

                            String push_id = user_message_push.getKey();

                            Map messageMap = new HashMap();
                            messageMap.put("message",download_Url);
                            messageMap.put("seen", false);
                            messageMap.put("type","image");
                            messageMap.put("time", ServerValue.TIMESTAMP);
                            messageMap.put("from",mCurrentUserId);


                            Map messageUserMap = new HashMap();
                            messageUserMap.put(current_user_ref +"/" + push_id,messageMap);


                            mRootref.updateChildren(messageUserMap);



                        }else {

                        }
                    }
                });


            }else if (resultCode ==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

    }
    private void uploadFile(final Uri data) {

        StorageReference sRef =mimagestorage.child("Files").child(mCurrentUserId).child(random()+ ".pdf");
        sRef.putFile(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult().getUploadSessionUri();
                    String download_Url = downloadUri.toString();
                    // mUserrDatabase.child("image").setValue(download_Url);

                    String current_user_ref = "Customercare/" + mCurrentUserId ;

                    DatabaseReference user_message_push = mRootref.child("Customercare").child(mCurrentUserId).push();

                    String push_id = user_message_push.getKey();

                    Map messageMap = new HashMap();
                    messageMap.put("message",download_Url);
                    messageMap.put("seen", false);
                    messageMap.put("type","pdf");
                    messageMap.put("time",ServerValue.TIMESTAMP);
                    messageMap.put("from",mCurrentUserId);


                    Map messageUserMap = new HashMap();
                    messageUserMap.put(current_user_ref +"/" + push_id,messageMap);



                    mRootref.updateChildren(messageUserMap);



                }else {

                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();


        userRef.child("status").setValue("online");


    }

    @Override
    protected void onStop() {
        super.onStop();

        userRef.child("status").setValue("online");

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent startIntent = new Intent(  CustomercareActivity.this, MainActivity.class);
        startIntent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startIntent);
        finish();
    }

    private void loadMessage() {

        h.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message = dataSnapshot.getValue(Messages.class);
                messageList.add(message);
                mAdapter.notifyDataSetChanged();
                mMessagesList.scrollToPosition(messageList.size()-1);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {



            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void sendMessage() {
        String message = mChatMessageView.getText().toString();

        if (!TextUtils.isEmpty(message)){
            String current_user_ref = "Customercare/" + mCurrentUserId ;


            DatabaseReference user_message_push = mRootref.child("Customercare").child(mCurrentUserId).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen", false);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);
            messageMap.put("from",mCurrentUserId);




            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref +"/" + push_id,messageMap);



            mChatMessageView.setText("");
            mRootref.updateChildren(messageUserMap);

        }
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
