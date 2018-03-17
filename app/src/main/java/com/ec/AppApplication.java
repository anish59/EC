package com.ec;

import android.app.Application;

import com.ec.helper.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anish on 29-08-2017.
 */

public class AppApplication extends Application {
    private static Retrofit retrofit;
    private static AppApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initRetroFit();
    }

    private void initRetroFit() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}