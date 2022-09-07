package com.spinnycatalogue.network;

import static com.spinnycatalogue.utils.Constants.BASE_URL_FUNCTIONS;
import static com.spinnycatalogue.utils.Constants.BASE_URL_FUNCTIONS;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static Retrofit retrofit;
    private static APIInterface apiRequests;

    // Singleton Instance of APIRequests
    public static APIInterface getInstance() {

        if (apiRequests == null) {


            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


            final OkHttpClient client = new OkHttpClient.Builder()
                     .addNetworkInterceptor(interceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_FUNCTIONS)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            apiRequests = retrofit.create(APIInterface.class);

            return apiRequests;

        } else {
            return apiRequests;
        }
    }
}