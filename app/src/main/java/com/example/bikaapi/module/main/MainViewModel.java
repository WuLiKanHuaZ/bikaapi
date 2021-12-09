package com.example.bikaapi.module.main;

import android.app.Application;

import androidx.annotation.NonNull;

import com.hjq.gson.factory.GsonFactory;
import com.lfkdsk.bika.BikaApi;
import com.lfkdsk.bika.response.CategoryResponse;
import com.lfkdsk.bika.response.GeneralResponse;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.utils.KLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends BaseViewModel {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        text();
    }

    private void text(){
        BikaApi.getInstance().getCategories().enqueue(new Callback<GeneralResponse<CategoryResponse>>() {
            @Override
            public void onResponse(Call<GeneralResponse<CategoryResponse>> call, Response<GeneralResponse<CategoryResponse>> response) {

            }

            @Override
            public void onFailure(Call<GeneralResponse<CategoryResponse>> call, Throwable t) {

            }
        });
    }

}
