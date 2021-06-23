package com.hcare.homeopathy.hcare.Orders;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitInterface {

    @GET("courier/track/shipment/{shipment_id}")
    Call<ShipRocketData> getDetails(@Path("shipment_id") long shipment_id
    );
}
