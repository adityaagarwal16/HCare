package com.hcare.homeopathy.hcare.PostConsultation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.Mainmenus.MainActivity;
import com.hcare.homeopathy.hcare.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddressActivity extends AppCompatActivity implements PaymentResultListener {

    private Toolbar mToolbar;


    private FirebaseUser mCurrentUser;
    private DatabaseReference meditDatabase,medicinedata,ordertDatabase,userRef,doctororder,ordercare;
    private EditText maName,maPhone_number,pincode,Flat,Towm,state;
    private Button mSavebtn;
    private ImageButton popback;
    private String mChatUser,current_uid ,bname,bphone_number,bpinCode,bFlat,bTown,bState,medicine_id,phoneno,mail,OrderId;
    private int price,netprice;
    private TextView total,net;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        setTitle("Order");

        OrderId ="Hcr"+getRandomNumberString();
        Log.d("test123456",""+ getRandomNumberString());
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        current_uid = mCurrentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mChatUser = getIntent().getStringExtra("user_id");
        medicine_id = getIntent().getStringExtra("medicine_id");

        price = getIntent().getIntExtra("price",00);
        meditDatabase = FirebaseDatabase.getInstance().getReference().child("Orders").child(current_uid);
        ordercare =FirebaseDatabase.getInstance().getReference().child("neworder");
        medicinedata = FirebaseDatabase.getInstance().getReference().child("Doctors").child(mChatUser);
        ordertDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(current_uid).child(mChatUser);
        doctororder = FirebaseDatabase.getInstance().getReference().child("messages").child(mChatUser).child(current_uid);
        maName = (EditText) findViewById(R.id.FullName);
        maPhone_number =(EditText) findViewById(R.id.PhoneNumber);
        pincode = (EditText) findViewById(R.id.PinCode);
        Flat =(EditText) findViewById(R.id.FlatAddress);
        Towm =(EditText) findViewById(R.id.City);
        state =(EditText) findViewById(R.id.state);
        mSavebtn =(Button)findViewById(R.id.PlaceOrderBtn);
        popback =(ImageButton)findViewById(R.id.popupback);
        total =(TextView)findViewById(R.id.totalcostview);
        net =(TextView)findViewById(R.id.maincostview);
        Log.d("test1234",""+ price);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                phoneno =  (String) dataSnapshot.child("phone number").getValue();
                mail  = (String) dataSnapshot.child("email").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        total.setText(""+price);
        net.setText(""+price);
        netprice = price*100;


        try {
            mSavebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bname = maName.getText().toString();
                    if (!TextUtils.isEmpty(bname)) {
                        bphone_number = maPhone_number.getText().toString();
                        if (!TextUtils.isEmpty(bphone_number)) {
                            bpinCode = pincode.getText().toString();
                            if (!TextUtils.isEmpty(bpinCode)) {
                                bFlat = Flat.getText().toString();
                                if (!TextUtils.isEmpty(bFlat)) {
                                        bTown = Towm.getText().toString();
                                        if (!TextUtils.isEmpty(bTown)) {
                                            bState = state.getText().toString();
                                            if (!TextUtils.isEmpty(bState)) {


                                               // payedorder();
                                                   startPayment();

                                                    Toast.makeText(AddressActivity.this, "pay now ", Toast.LENGTH_LONG).show();


                                            }else {
                                                Toast.makeText(AddressActivity.this, "please enter your State ", Toast.LENGTH_LONG).show();
                                            }
                                        }else {
                                            Toast.makeText(AddressActivity.this, "please enter your City ", Toast.LENGTH_LONG).show();
                                        }

                                }else {
                                    Toast.makeText(AddressActivity.this, "please enter your Flat No ", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(AddressActivity.this, "please enter your Pin Code ", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(AddressActivity.this, "please enter your Phone Number ", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(AddressActivity.this, "please enter your name ", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(AddressActivity.this, "Error occured , please try again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.child("status").setValue("online");
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.child("status").setValue("offline");
    }
    private PopupWindow pw;

    private void showPopup() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.addresspop,
                    (ViewGroup) findViewById(R.id.popup_1));
            pw = new PopupWindow(layout,  ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,true);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            Button Close = (Button) layout.findViewById(R.id.close_popup);
            Close.setOnClickListener(close);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private View.OnClickListener close = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
            Intent regIntent = new Intent(AddressActivity.this, MainActivity.class);
            regIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(regIntent);
            finish();

        }
    };

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final AppCompatActivity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Medicine");
            options.put("description", "40% discount applied");
            //You can omit the image option to fetch the image from dashboard
           // options.put("image", R.drawable.logo);
            options.put("currency", "INR");
            options.put("amount", netprice);

            JSONObject preFill = new JSONObject();
            preFill.put("email", mail);
            preFill.put("contact", phoneno);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            payedorder();
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Payment Successful Exeception: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Payment failed: execption " + response, Toast.LENGTH_SHORT).show();
        }
    }
    private void payedorder(){


        String time = DateFormat.getDateTimeInstance().format(new Date());



        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("FullName", bname);
        userMap.put("PhoneNumber", bphone_number);
        userMap.put("PinCode", bpinCode);
        userMap.put("Address", bFlat);
        userMap.put("PatientId", current_uid);
        userMap.put("City", bTown);
        userMap.put("State", bState);
        userMap.put("Doctor", mChatUser);
        userMap.put("emailId", mail);
        userMap.put("Amount", String.valueOf(price));
        userMap.put("OrderStatus", "Recieved");
        userMap.put("orderId", OrderId);
        userMap.put("Ordertime", time);
        ordercare.child(OrderId).setValue(userMap);
        meditDatabase.child(OrderId).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                medicinedata.child("count").push().setValue(current_uid);
               // userRef.child("consultCount").removeValue();
                ordertDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            child.getRef().child("ordering").setValue("ordered");
                            doctororder.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                                        child.getRef().child("ordering").setValue("ordered");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        showPopup();
       // Toast.makeText(AddressActivity.this, "Your order has been placed ", Toast.LENGTH_LONG).show();
    }
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

}
