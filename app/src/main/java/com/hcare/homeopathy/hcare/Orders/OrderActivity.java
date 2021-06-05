package com.hcare.homeopathy.hcare.Orders;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcare.homeopathy.hcare.BaseActivity;
import com.hcare.homeopathy.hcare.R;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends BaseActivity {

    String userID;
    AllOrdersObject order = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            order = (AllOrdersObject) getIntent().getSerializableExtra("order");
            getSupportActionBar().setTitle(order.getOrderId());
            getDetails();
        } catch(Exception e) {
            e.printStackTrace();
            getSupportActionBar().setTitle("Order Not Found");
        }
    }

    void getDetails() {
        RetrofitInterface retrofitInterface = RetrofitClient.getInstance()
                .create(RetrofitInterface.class);
        Call<JSONObject> call = retrofitInterface.getDetails(113236372);

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse (@NonNull Call<JSONObject> call,
                                    @NonNull Response<JSONObject> response) {
                try {
                    //assert response.body() != null;
                    Log.i("call", String.valueOf(call.request()));
                    Log.i("status", response.toString());
                    Log.i("message", String.valueOf(response.body()));
                /*    Log.i("status",String.valueOf(response.body().getTrack_status()));
                    Log.i("order",String.valueOf(response.body().getCurrent_status()));
                    Log.i("order",String.valueOf(response.body().getActivity()));
                    Log.i("order",String.valueOf(response.body().getDate()));
                    Log.i("order",String.valueOf(response.body().getDelivered_to()));
                    Log.i("order",String.valueOf(response.body().getTrack_url()));*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JSONObject> call,@NonNull  Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
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

}
