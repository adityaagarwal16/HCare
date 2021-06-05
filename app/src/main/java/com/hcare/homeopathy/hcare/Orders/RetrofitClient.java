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
    private static final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjk2NzQ3MSwiaXNzIjoiaHR0cHM6Ly9hcGl2Mi5zaGlwcm9ja2V0LmluL3YxL2V4dGVybmFsL2F1dGgvbG9naW4iLCJpYXQiOjE2MjI3MTY3NzMsImV4cCI6MTYyMzU4MDc3MywibmJmIjoxNjIyNzE2NzczLCJqdGkiOiI1TGhiZ0l2SjNuOXZNU3FLIn0.7zTQAI_JijbINg1DZJmLeaC2R-RdvirhgxJtezxHmBw";

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
                    .header("Authorization","Bearer "+ token)
                    .build();

            return chain.proceed(newRequest);
        }
    }


}
