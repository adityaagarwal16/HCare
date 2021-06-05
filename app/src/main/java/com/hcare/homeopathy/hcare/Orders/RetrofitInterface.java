package com.hcare.homeopathy.hcare.Orders;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitInterface {

    @GET("courier/track/shipment/{shipment_id}")
    Call<JSONObject> getDetails(@Path("shipment_id") long shipment_id
    );
}
