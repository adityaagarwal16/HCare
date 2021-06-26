package com.hcare.homeopathy.hcare.Orders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.R;
import com.joestelmach.natty.Parser;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends BaseActivity {

    String userID, doctorName;
    AllOrdersObject order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        userID = Objects.requireNonNull(FirebaseAuth
                .getInstance().getCurrentUser()).getUid();

        try {
            order = (AllOrdersObject) getIntent().getSerializableExtra("order");
            doctorName = getIntent().getStringExtra("doctorName");
            setDeliveryDetails();
        } catch (Exception e) {e.printStackTrace();}

        getTrackingDetails();
    }

    void setDeliveryDetails() {
        ((TextView) findViewById(R.id.orderID))
                .setText(MessageFormat.format("Order ID: {0}", order.getOrderId()));

        try {
            Parser parser = new Parser();
            Date date = parser.parse(order.getOrdertime()).get(0).getDates().get(0);
            ((TextView) findViewById(R.id.date)).setText
                    (new SimpleDateFormat("MMM dd, yyyy - hh:mm a",
                            Locale.ENGLISH).format(date));
        } catch(Exception e) { e.printStackTrace(); }

        if(order.getAmount() == null)
            order.setAmount("0");
        ((TextView) findViewById(R.id.totalAmount))
                .setText(MessageFormat.format("₹ {0}", order.getAmount()));

        //delivery
        if(!order.getFullName().isEmpty())
            ((TextView) findViewById(R.id.customerName)).setText(order.getFullName());

        setPhoneEmail(order.getPhoneNumber(), order.getEmailId());

        if(order.getAddress().equals(""))
            order.setAddress("Address Unavailable");
        ((TextView) findViewById(R.id.address)).setText(order.getAddress());

        if(order.getState().equals(""))
            order.setAddress("State not found");
        ((TextView) findViewById(R.id.state)).setText(order.getState());

        if(order.getPinCode().length() != 6)
            order.setAddress("Pin code Unavailable");
        ((TextView) findViewById(R.id.pinCode)).setText(order.getPinCode());

        ((TextView) findViewById(R.id.doctorName)).setText(doctorName);
        findViewById(R.id.doctor).setOnClickListener(v -> {
            Intent intent = new Intent(this, PrescriptionActivity.class);
            intent.putExtra("doctorID", order.getDoctor());
            startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    void setPhoneEmail(String phone, String email) {
        TextView phoneEmail = findViewById(R.id.phoneEmail);
        if(phone!=null  && phone.length() == 10 && email!=null && email.contains("@"))
           phoneEmail.setText(MessageFormat.format("{0} || {1}",
                            order.getPhoneNumber(), order.getEmailId()));
        else if(email!= null && email.contains("@"))
            phoneEmail.setText(order.getEmailId());
        else if(phone!=null && phone.length() == 10)
            phoneEmail.setText(order.getPhoneNumber());
        else
            phoneEmail.setText("Contact Unavailable");
    }

    void showTrackDetails() {
      findViewById(R.id.trackingUnavailable).setVisibility(View.GONE);
      findViewById(R.id.loadingTracking).setVisibility(View.GONE);
    }

    void trackingUnavailable() {
        findViewById(R.id.trackingUnavailable).setVisibility(View.VISIBLE);
        findViewById(R.id.loadingTracking).setVisibility(View.GONE);
    }

    void getTrackingDetails() {
        if(order.getShipmentID() != 0) {
            RetrofitInterface retrofitInterface = RetrofitClient.getInstance()
                    .create(RetrofitInterface.class);
            Call<ShipRocketData> call = retrofitInterface.getDetails(order.getShipmentID());
            call.enqueue(new Callback<ShipRocketData>() {
                @Override
                public void onResponse(@NonNull Call<ShipRocketData> call,
                                       @NonNull Response<ShipRocketData> response) {
                    try {
                        if (response.body() != null) {
                            TrackingData object = response.body().getTracking_data();
                            Log.i("track", object.toString());
                            if(object.getTrack_status() != 0) {
                                try {
                                    setTrackingRecycler(object.getShipment_track_activities());
                                } catch (Exception ignored) { }
                                try {
                                    ShipmentTrack shipmentTrack = object.getShipment_track().get(0);
                                    setTrackDetails(shipmentTrack.current_status, shipmentTrack.awb_code);
                                } catch (Exception ignored) { }
                                try {
                                    findViewById(R.id.trackLinkButton).setOnClickListener(v -> {
                                        if (object.getTrack_url() != null) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse(object.getTrack_url()));
                                            startActivity(browserIntent);
                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Tracking unavailable", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } catch (Exception ignored) { }
                                showTrackDetails();
                            } else
                                trackingUnavailable();
                        } else {
                            trackingUnavailable();
                            Log.d("TAG", "onResponseCode: " + response.code());
                            Log.d("TAG", "onResponseErrorBody: " + response.errorBody());
                        }
                    } catch (Exception e) {
                        trackingUnavailable();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ShipRocketData> call, @NonNull Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else trackingUnavailable();
    }

    private void setTrackingRecycler(List<ShipmentTrackActivity> list) {
        if(!list.isEmpty()) {
            RecyclerView shipmentTrackRecycler = findViewById(R.id.shipmentTrackRecycler);
            shipmentTrackRecycler.setLayoutManager(new LinearLayoutManager(this));
            shipmentTrackRecycler.setAdapter(new ShipmentTrackingAdapter(list));
        }
    }

    private void setTrackDetails(String status, String awb) {
        ((TextView) findViewById(R.id.orderStatus)).setText(status);
        ((TextView) findViewById(R.id.awbCode)).setText(MessageFormat.
                format("AWB Code : {0}", awb));
    }

    public void Back(View view) {
        onBackPressed();
    }
}
