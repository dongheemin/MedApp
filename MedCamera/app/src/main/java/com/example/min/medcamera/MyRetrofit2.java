package com.example.min.medcamera;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofit2 {
    public static final String URL = "http://220.149.189.234:8080/ImageSaver/";

    static Retrofit mRetrofit;

    public static Retrofit getRetrofit2(){

        if(mRetrofit == null){

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return mRetrofit;
    }
}
