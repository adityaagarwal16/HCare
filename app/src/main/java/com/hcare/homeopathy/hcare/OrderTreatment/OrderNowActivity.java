package com.hcare.homeopathy.hcare.OrderTreatment;

import static com.hcare.homeopathy.hcare.OrderTreatment.AddressSharedPref.ADDRESS;
import static com.hcare.homeopathy.hcare.OrderTreatment.AddressSharedPref.CITY;
import static com.hcare.homeopathy.hcare.OrderTreatment.AddressSharedPref.PIN_CODE;
import static com.hcare.homeopathy.hcare.OrderTreatment.AddressSharedPref.STATE;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.FirebaseClasses.OrderObject;
import com.hcare.homeopathy.hcare.PaymentsReferrals.PaymentSuccessful;
import com.hcare.homeopathy.hcare.PaymentsReferrals.RazorPay;
import com.hcare.homeopathy.hcare.PaymentsReferrals.WalletStatic;
import com.hcare.homeopathy.hcare.R;
import com.razorpay.PaymentResultListener;

import java.text.MessageFormat;
import java.util.Objects;

public class OrderNowActivity extends BaseActivity implements PaymentResultListener {

    boolean paymentSuccessful = false;
    String userID, doctorID;
    OrderObject orderObject;
    String username, phoneNumber, email;
    int totalMoneyInWallet = 0;
    float subTotal, discount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_now);
        WalletStatic.walletMoneyUsed = 0;

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userID = Objects.requireNonNull(FirebaseAuth.getInstance()
                .getCurrentUser()).getUid();

        doctorID = getIntent().getStringExtra("user_id");
        discount = getIntent().getIntExtra("discount", 0);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        orderObject = new OrderObject();
        orderObject.setAmount(getIntent().getIntExtra("price", 0));

        subTotal = orderObject.getAmount();
        setFields();
        setAddress();
        setSpinner();
        setCrosses();

        reference.child("Users").child(userID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            username = (String) dataSnapshot.child("name").getValue();
                            ((EditText) findViewById(R.id.name)).setText(username);
                            phoneNumber = (String) dataSnapshot.child("phone number").getValue();
                            ((EditText) findViewById(R.id.phoneNumber)).setText(phoneNumber);
                            email = (String) dataSnapshot.child("email").getValue();
                        } catch (Exception ignored) { }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        setWallet();
        walletCheck();
    }

    void walletCheck() {
        CheckBox walletIsChecked = findViewById(R.id.walletCheckBox2);
        walletIsChecked.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                int max = (int) Math.ceil((double) (subTotal - discount) / 2);
                WalletStatic.walletMoneyUsed = Math.min(totalMoneyInWallet, max);
            }
            else
                WalletStatic.walletMoneyUsed = 0;

            setFields();
        });
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

    private void setWallet() {
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    totalMoneyInWallet = Objects.requireNonNull(snapshot
                            .child("Wallet").getValue(Integer.class));
                    ((TextView) findViewById(R.id.availableMoney))
                            .setText(MessageFormat.format("₹ {0}",  totalMoneyInWallet));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void setFields() {
        int total = (int) (subTotal - discount - WalletStatic.walletMoneyUsed);
        ((TextView) findViewById(R.id.total)).setText(String.valueOf(total));
        ((TextView) findViewById(R.id.subTotal)).setText(String.valueOf((int) subTotal));
        ((TextView) findViewById(R.id.deliveryCharge)).setText(String.valueOf(0));
        ((TextView) findViewById(R.id.HCareDiscount)).setText(String.valueOf((int) discount));
        ((TextView) findViewById(R.id.WalletDiscount)).setText(String.valueOf(WalletStatic.walletMoneyUsed));

        ((TextView) findViewById(R.id.savings))
                .setText(MessageFormat.format("{0} {1}",
                        "Your Savings : ₹", discount + WalletStatic.walletMoneyUsed));
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
        username = ((EditText) findViewById(R.id.name)).getText().toString();
        phoneNumber = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();
        try {
            orderObject.setPinCode(Integer.parseInt(((EditText) findViewById(R.id.pinCode))
                    .getText().toString()));
        } catch(Exception e) { orderObject.setPinCode(0);}
        orderObject.setAddress(((EditText) findViewById(R.id.address)).getText().toString());
        orderObject.setCity(((EditText) findViewById(R.id.city)).getText().toString());
        orderObject.setState(((TextView) findViewById(R.id.state)).getText().toString());

        try {
            if (!username.isEmpty()) {
                if (phoneNumber.length() == 10) {
                    if (orderObject.getPinCode() >= 100000 && orderObject.getPinCode() <= 999999) {
                        if (!orderObject.getAddress().isEmpty()) {
                            if (!orderObject.getCity().isEmpty()) {
                                if (!orderObject.getState().isEmpty())
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
        orderObject.setTime(System.currentTimeMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(paymentSuccessful)
            showOrderSuccessfulFragment();
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
        sharedPref.save(PIN_CODE, String.valueOf(orderObject.getPinCode()));
        sharedPref.save(ADDRESS, orderObject.getAddress());
        sharedPref.save(CITY, orderObject.getCity());
        sharedPref.save(STATE, orderObject.getState());
        int total = (int) (subTotal - discount - WalletStatic.walletMoneyUsed);
        new RazorPay(total, this);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            paymentSuccessful = true;
            new PaymentSuccessful(orderObject, userID, doctorID, WalletStatic.walletMoneyUsed);
            WalletStatic.walletMoneyUsed = 0;
        } catch (Exception e) {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO CHECK
    @Override
    public void onPaymentError(int code, String response) {
        try {
            WalletStatic.walletMoneyUsed = 0;
            Toast.makeText(OrderNowActivity.this,
                    "Payment failed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
