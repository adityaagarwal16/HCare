package com.hcare.homeopathy.hcare.OrderTreatment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import static com.hcare.homeopathy.hcare.OrderTreatment.AddressSharedPref.ADDRESS;
import static com.hcare.homeopathy.hcare.OrderTreatment.AddressSharedPref.CITY;
import static com.hcare.homeopathy.hcare.OrderTreatment.AddressSharedPref.PIN_CODE;
import static com.hcare.homeopathy.hcare.OrderTreatment.AddressSharedPref.STATE;

public class OrderNowActivity extends BaseActivity implements PaymentResultListener {

    private DatabaseReference reference, userRef;
    private String doctorID, userID;

    private String name, email = "example", phoneNumber = "91", pinCode, address, city, state;
    int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_now);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();

        userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userID);

        doctorID = getIntent().getStringExtra("user_id");
        reference = FirebaseDatabase.getInstance().getReference();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    email = (String) dataSnapshot.child("email").getValue();
                    phoneNumber = (String) dataSnapshot.child("phone number").getValue();
                } catch (Exception ignored) { }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setFields();
        setAddress();
        setSpinner();
        setCrosses();
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

    private void setFields() {
        totalPrice = getIntent().getIntExtra("price", 0);

        ((TextView) findViewById(R.id.total)).setText(String.valueOf(totalPrice));
        ((TextView) findViewById(R.id.subTotal)).setText(String.valueOf(totalPrice));
        ((TextView) findViewById(R.id.deliveryCharge)).setText(String.valueOf(0));

        ((TextView) findViewById(R.id.savings))
                .setText(MessageFormat.format("{0} {1}",
                        "Your Savings : ₹",
                        getIntent().getIntExtra("discount", 0))
                );

        reference.child("Users").child(userID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            name = Objects.requireNonNull(
                                    dataSnapshot.child("name").getValue()).toString();
                            ((EditText) findViewById(R.id.name))
                                    .setText(name);

                            phoneNumber = Objects.requireNonNull(
                                    dataSnapshot.child("phone number").getValue()).toString();
                            ((EditText) findViewById(R.id.phoneNumber))
                                    .setText(phoneNumber);

                        } catch (Exception ignored) { }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void setAddress() {
        AddressSharedPref sharedPref = new AddressSharedPref(this);

        ((EditText) findViewById(R.id.pinCode)).setText(sharedPref.get(PIN_CODE));
        ((EditText) findViewById(R.id.address)).setText(sharedPref.get(ADDRESS));
        ((EditText) findViewById(R.id.city)).setText(sharedPref.get(CITY));
        ((TextView) findViewById(R.id.state)).setText(sharedPref.get(STATE));
    }

    void setSpinner() {
        String[] states = {
                "Andaman and Nicobar Islands", "Andhra Pradesh",
                "Arunachal Pradesh", "Assam", "Bihar",
                "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli",
                "Daman and Diu", "Delhi", "Goa", "Gujarat",
                "Haryana", "Himachal Pradesh", "Jammu and Kashmir",
                "Jharkhand", "Karnataka", "Kerala", "Ladakh",
                "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur",
                "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry",
                "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
                "Uttar Pradesh", "Uttarakhand", "West Bengal"
        };

        findViewById(R.id.selectState).setOnClickListener(v -> {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Select State");

            b.setItems(states, (dialog, which) -> {
                dialog.dismiss();
                ((TextView) findViewById(R.id.state)).setText(states[which]);
            });

            AlertDialog alertDialog = b.create();
            alertDialog.show();
            DisplayMetrics metrics = getResources().getDisplayMetrics();

            Objects.requireNonNull(alertDialog.getWindow()).setLayout(
                    (int) (metrics.widthPixels * 0.9),
                    (int) (metrics.heightPixels * 0.6)
            );
        });
    }

    void setCrosses() {
        crossListeners(R.id.name, R.id.nameCross);
        crossListeners(R.id.phoneNumber, R.id.phoneNumberCross);
        crossListeners(R.id.pinCode, R.id.pinCodeCross);
        crossListeners(R.id.address, R.id.addressCross);
        crossListeners(R.id.city, R.id.cityCross);
    }

    void crossListeners(int textID, int crossID) {
        findViewById(crossID).setOnClickListener(v -> {
            EditText text =  findViewById(textID);
            final InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            text.requestFocus();
            text.setText("");
            manager.showSoftInput(text, 1);
        });
    }

    public void placeOrder(View view) {
        name = ((EditText) findViewById(R.id.name)).getText().toString();
        phoneNumber = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();
        pinCode = ((EditText) findViewById(R.id.pinCode)).getText().toString();
        address = ((EditText) findViewById(R.id.address)).getText().toString();
        city = ((EditText) findViewById(R.id.city)).getText().toString();
        state = ((TextView) findViewById(R.id.state)).getText().toString();

        try {
            if (!name.isEmpty()) {
                if (phoneNumber.length() == 10) {
                    if (pinCode.length() == 6) {
                        if (!address.isEmpty()) {
                            if (!city.isEmpty()) {
                                if (!state.isEmpty())

                                    startPayment();

                                else
                                    Toast.makeText(this, "Please enter your State",
                                            Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(this, "Please enter your City",
                                        Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(this, "please enter your Address",
                                    Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(this, "Please enter 6 digit Pin Code",
                                Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(this, "Please enter valid Phone Number",
                            Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(this, "Please enter your name",
                        Toast.LENGTH_LONG).show();

        } catch (Exception e){
            Toast.makeText(this, "Error, please try again", Toast.LENGTH_LONG).show();
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

    private void showOrderSuccessfulFragment() {
        findViewById(R.id.placeOrder).setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new OrderSuccessfulFragment())
                .commit();
    }

    public void startPayment() {
        AddressSharedPref sharedPref = new AddressSharedPref(this);
        sharedPref.save(PIN_CODE, pinCode);
        sharedPref.save(ADDRESS, address);
        sharedPref.save(CITY, city);
        sharedPref.save(STATE, state);

        try {
            final AppCompatActivity activity = this;
            final Checkout co = new Checkout();
            JSONObject options = new JSONObject();
            options.put("name", "Medicine");
            options.put("description", "40% discount applied");

            //You can omit the image option to fetch the image from dashboard
            // options.put("image", R.drawable.logo);
            int RAZORPAY_MULTIPLIER = 100;
            options.put("currency", "INR");
            options.put("amount", totalPrice * RAZORPAY_MULTIPLIER);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phoneNumber);

            options.put("prefill", preFill);

            co.setImage(R.drawable.logo_green);
            co.open(activity, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            orderSuccessful();
        } catch (Exception e) {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO CHECK
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(OrderNowActivity.this,
                    "Payment failed", Toast.LENGTH_SHORT).show();
        } catch (Exception ignored) { }
    }

    private void orderSuccessful() {
        String OrderId ="Hcr" + getRandomNumberString();
        String time = DateFormat.getDateTimeInstance().format(new Date());

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("FullName", name);
        userMap.put("PhoneNumber", phoneNumber);
        userMap.put("PinCode", pinCode);
        userMap.put("Address", address);
        userMap.put("PatientId", userID);
        userMap.put("City", city);
        userMap.put("State", state);
        userMap.put("Doctor", doctorID);
        userMap.put("emailId", email);
        userMap.put("Amount", String.valueOf(totalPrice));
        userMap.put("OrderStatus", "Recieved");
        userMap.put("orderId", OrderId);
        userMap.put("Ordertime", time);

        reference.child("neworder").child(OrderId).setValue(userMap);
        reference.child("Orders").child(userID)
                .child(OrderId).setValue(userMap).addOnCompleteListener(task -> {
            reference.child("Doctors").child(doctorID)
                    .child("count").push().setValue(userID);

           // userRef.child("consultCount").removeValue();
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

        showOrderSuccessfulFragment();
    }

    @SuppressLint("DefaultLocale")
    public String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }


}
