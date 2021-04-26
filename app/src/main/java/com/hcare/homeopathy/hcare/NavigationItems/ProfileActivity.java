package com.hcare.homeopathy.hcare.NavigationItems;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.hcare.homeopathy.hcare.MainActivity;
import com.hcare.homeopathy.hcare.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    Spinner spinner;

    //android layout
    private static final int GALLERY_PICK = 1;

    //storage image
    private StorageReference imageStorage;
    private DatabaseReference loggedin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Profile");
        spinner = findViewById(R.id.genderSpinner);

        imageStorage = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = Objects.requireNonNull(mCurrentUser).getUid();

        loggedin = FirebaseDatabase.getInstance().getReference().child("loggedin").child(current_uid);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    setFields(dataSnapshot);
                } catch(Exception ignored) { }

                final String image = Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString();

                if (!image.equals("default")) {

                    Picasso.get()
                            .load(image)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.vector_person)
                            .into(findViewById(R.id.profilePicture), new Callback() {

                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(image)
                                            .placeholder(R.drawable.vector_person)
                                            .into((ImageView) findViewById(R.id.profilePicture));
                                }
                            });

                    Picasso.get()
                            .load(image)
                            .transform(new BlurTransformation(getApplicationContext(), 25, 1))
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(findViewById(R.id.background), new Callback() {

                                @Override
                                public void onSuccess() {
                                    findViewById(R.id.tint).setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(image)
                                            .transform(new BlurTransformation(
                                                    getApplicationContext(), 25, 1))
                                            .into((ImageView) findViewById(R.id.background));
                                    findViewById(R.id.tint).setVisibility(View.VISIBLE);
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        findViewById(R.id.imageButton).setOnClickListener(v -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(
                    Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);
        });

        setCrosses();
    }

    void setFields(DataSnapshot dataSnapshot) {
        try{
            ((EditText) findViewById(R.id.patientName))
                    .setText(Objects.requireNonNull(
                            dataSnapshot.child("name").getValue()).toString());
            ((EditText) findViewById(R.id.email))
                    .setText(Objects.requireNonNull(
                            dataSnapshot.child("email").getValue()).toString());
            ((EditText) findViewById(R.id.age))
                    .setText(Objects.requireNonNull(
                            dataSnapshot.child("age").getValue()).toString());
            ((EditText) findViewById(R.id.contact))
                    .setText(Objects.requireNonNull(
                            dataSnapshot.child("phone number").getValue()).toString());
            spinner.setSelection(getSpinnerNum(
                    Objects.requireNonNull(dataSnapshot.child("sex")
                            .getValue()).toString())
            );
        } catch (Exception ignored) { }
    }


    void setCrosses() {
        crossListeners(R.id.patientNameCross, R.id.patientName);
        crossListeners(R.id.emailCross, R.id.email);
        crossListeners(R.id.ageCross, R.id.age);
        crossListeners(R.id.contactCross, R.id.contact);
    }

    void crossListeners(int crossID, int textID) {
        findViewById(crossID).setOnClickListener(v -> {
            EditText text =  findViewById(textID);
            final InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            text.requestFocus();
            text.setText("");
            manager.showSoftInput(text, 1);
        });
    }

    int getSpinnerNum(String gender) {
        if(gender.equals("Male"))
            return 0;
        else
            return 1;
    }

    String getSpinnerString() {
        if(spinner.getSelectedItemPosition() == 0)
            return "Male";
        else
            return "Female";
    }

    public void saveButton (View view) {
        String patientName = ((EditText) findViewById(R.id.patientName)).getText().toString();
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String age = ((EditText) findViewById(R.id.age)).getText().toString();
        String gender = getSpinnerString();
        String contactNumber = ((EditText) findViewById(R.id.contact)).getText().toString();

        if (!patientName.isEmpty()) {
            if (!email.isEmpty()) {
                if (!age.isEmpty()) {
                    if (!contactNumber.isEmpty()) {

                        mUserDatabase.child("name").setValue(patientName);
                        mUserDatabase.child("phone number").setValue(contactNumber);
                        mUserDatabase.child("age").setValue(age);
                        mUserDatabase.child("email").setValue(email);
                        mUserDatabase.child("sex").setValue(gender);

                        HashMap<String, String> notifyData = new HashMap<>();
                        notifyData.put("phone_number", contactNumber);
                        notifyData.put("email", email);
                        notifyData.put("name", patientName);
                        notifyData.put("age", age);
                        notifyData.put("sex", gender);
                        loggedin.setValue(notifyData);

                        Intent regIntent = new Intent(this, MainActivity.class);
                        regIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(regIntent);
                        finish();

                    } else
                        Toast.makeText(this, "Please enter your Contact number",
                                Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(this, "Please enter your age",
                            Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, "Please enter your Email",
                        Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, "Please enter your name",
                    Toast.LENGTH_LONG).show();
    }


        @Override
        protected void onStart() {
            super.onStart();
            mUserDatabase.child("status").setValue("online");
        }

        @Override
    protected void onStop() {
        super.onStop();
        mUserDatabase.child("status").setValue("offline");
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            Uri imageUri =data.getData();
            CropImage.activity(imageUri).setAspectRatio(1,1).start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Loading image\nPlease wait ..", Toast.LENGTH_SHORT).show();
                Uri resultUri = result.getUri();
                final File thumb_filePath = new File(Objects.requireNonNull(resultUri.getPath()));

                String current_user_id = mCurrentUser.getUid();

                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxHeight(200)
                        .setMaxWidth(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                final byte[] thumb_byte = baos.toByteArray();

                final StorageReference filepath =
                        imageStorage.child("profile_images").child(current_user_id+".jpg");
                final StorageReference thumb_filepath =
                        imageStorage.child("profile_images").child("thumbs").child(current_user_id+"jpg");

                filepath.putFile(resultUri).addOnCompleteListener(
                        task -> filepath.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Log.d(TAG, "onSuccess: uri= "+ uri.toString());

                    final String download_Url = uri.toString();

                    UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                    uploadTask.addOnCompleteListener(thumb_task -> thumb_filepath.getDownloadUrl()
                            .addOnSuccessListener(uri1 -> {

                                Map update_haspMap = new HashMap<String, String>();
                                update_haspMap.put("image", download_Url);
                                update_haspMap.put("thumb_image", uri1.toString());

                                mUserDatabase.updateChildren(update_haspMap);

                            }));

                }));

            }
        }

    }

}
