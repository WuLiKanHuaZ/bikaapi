package com.example.bikaapi;

import android.app.Application;

import com.example.bikaapi.http.PiCaInterceptor;
import com.example.bikaapi.http.PiCaServer;
import com.example.bikaapi.http.RequestHandler;
import com.hjq.http.EasyConfig;
import com.lfkdsk.bika.BikaApi;

import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.utils.KLog;
import okhttp3.OkHttpClient;


public class PiCaApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BaseApplication.setApplication(this);
        KLog.init(true);

        BikaApi.getInstance().initClient();
    }



}
