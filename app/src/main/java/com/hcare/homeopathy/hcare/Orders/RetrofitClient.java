package com.hcare.homeopathy.hcare.Orders;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    //FIX token issue
    private static final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjE1ODUzMjcsImlzcyI6Imh0dHBzOi8vYXBpdjIuc2hpcHJvY2tldC5pbi92MS9leHRlcm5hbC9hdXRoL2xvZ2luIiwiaWF0IjoxNjI0NzI5MDg1LCJleHAiOjE2MjU1OTMwODUsIm5iZiI6MTYyNDcyOTA4NSwianRpIjoiYk9YZmo2aFIwblRVb1pXUyJ9.gLXXL8N28biiNn8tP7TN7_i-T2lJi9VwymI8UJo6_AE";

    private static final String Url = "https://apiv2.shiprocket.in/v1/external/";

    public static Retrofit getInstance() {
        if(retrofit == null) {
            TokenInterceptor interceptor=new TokenInterceptor();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static class TokenInterceptor implements Interceptor{

        @NonNull
        @Override
        public Response intercept(Chain chain) throws IOException {

            //rewrite the request to add bearer token
            Request newRequest=chain.request().newBuilder()
                    .header("Authorization",
                            "Bearer "+ token)
                    .build();

            return chain.proceed(newRequest);
        }
    }


}
