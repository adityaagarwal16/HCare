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
    private static final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjU1OTQ0NiwiaXNzIjoiaHR0cHM6Ly9hcGl2Mi5zaGlwcm9ja2V0LmluL3YxL2V4dGVybmFsL2F1dGgvbG9naW4iLCJpYXQiOjE2MjQ1NTA0NTMsImV4cCI6MTYyNTQxNDQ1MywibmJmIjoxNjI0NTUwNDUzLCJqdGkiOiJmU0g0VW5XaWpSc09tTkZwIn0.x7W92tD_9twmPlFzvm9SSpgruDRVCj0r652NWZtJ7r8";

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
