package com.hcare.homeopathy.hcare.Mainmenus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hcare.homeopathy.hcare.R;
import com.hcare.homeopathy.hcare.Start.ProfileSettingActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    private DatabaseReference mUserrDatabase;
    private FirebaseUser mCurrentUser;

    //android layout
    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mPhone_number;
    private TextView mAge;
    private TextView mSex;
    private TextView mEmail;
    private Button mEditbtn;
    private ImageButton mimagebtn;

    private static final int GALLERY_PICK = 1;
    ProgressBar mProgressbar;

    //storage image
    private StorageReference mimagestorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mToolbar = (Toolbar) findViewById(R.id.profilesetting_appBar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDisplayImage = (CircleImageView) findViewById(R.id.settings_image);
        mName = (TextView) findViewById(R.id.name);
        mPhone_number =(TextView) findViewById(R.id.phone_number);
        mAge =(TextView) findViewById(R.id.age);
        mSex =(TextView) findViewById(R.id.sex);
        mEmail =(TextView) findViewById(R.id.emailview);
        mEditbtn =(Button) findViewById(R.id.edit_btn);
        mimagebtn =(ImageButton) findViewById(R.id.imageButton);

        mimagestorage = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        mUserrDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);





        mUserrDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String hname = dataSnapshot.child("name").getValue().toString();
                String hphone_number = dataSnapshot.child("phone number").getValue().toString();
                String hage = dataSnapshot.child("age").getValue().toString();
                String hsex = dataSnapshot.child("sex").getValue().toString();
                String hthumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                String hemail = dataSnapshot.child("email").getValue().toString();
                try {
                    mName.setText(hname);
                    mPhone_number.setText(hphone_number);
                    mAge.setText(hage);
                    mSex.setText(hsex);
                    mEmail.setText(hemail);
                }catch (Exception a){
                    Toast.makeText(ProfileActivity.this, "Your order has been placed ", Toast.LENGTH_LONG).show();
                }
                final String himage = dataSnapshot.child("image").getValue().toString();
                if (!himage.equals("default")) {
                    Picasso.get().load(himage).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_profile).into(mDisplayImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(himage).placeholder(R.drawable.default_profile).into(mDisplayImage);
                        }
                    });

                }else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mEditbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name_value = mName.getText().toString();
                String phone_value = mPhone_number.getText().toString();
                String age_value = mAge.getText().toString();
                String email_value = mEmail.getText().toString();

                Intent regIntent = new Intent(ProfileActivity.this, ProfileSettingActivity.class);
                regIntent.putExtra("name_value" , name_value);
                regIntent.putExtra("phone_value" , phone_value);
                regIntent.putExtra("age_value" , age_value);
                regIntent.putExtra("email_value" , email_value);
                startActivity(regIntent);
                finish();

            }
        });

        mimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mUserrDatabase.child("status").setValue("online");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent startIntent = new Intent(  ProfileActivity.this, MainActivity.class);
        startIntent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startIntent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUserrDatabase.child("status").setValue("offline");
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode ==GALLERY_PICK && resultCode ==RESULT_OK){

            Uri imageUri =data.getData();

            CropImage.activity(imageUri).setAspectRatio(1,1).start(this);
        }
        if(requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){



                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                String current_user_id = mCurrentUser.getUid();

                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxHeight(200)
                        .setMaxWidth(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                final byte[] thumb_byte = baos.toByteArray();

                final StorageReference filepath = mimagestorage.child("profile_images").child(current_user_id+".jpg");
                final StorageReference thumb_filepath = mimagestorage.child("profile_images").child("thumbs").child(current_user_id+"jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Log.d(TAG, "onSuccess: uri= "+ uri.toString());

                                final String download_Url = uri.toString();

                                UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                        thumb_filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                // Log.d(TAG, "onSuccess: uri= "+ uri.toString());

                                                final String thumb_dowloadUrl = uri.toString();


                                                Map update_haspMap = new HashMap<String, String>();
                                                update_haspMap.put("image", download_Url);
                                                update_haspMap.put("thumb_image", thumb_dowloadUrl);

                                                mUserrDatabase.updateChildren(update_haspMap);

                                            }
                                        });
                                    }
                                });

                            }
                        });
                    }
                });

            }else if (resultCode ==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
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

