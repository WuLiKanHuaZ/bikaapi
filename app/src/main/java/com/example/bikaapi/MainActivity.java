package com.example.bikaapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.lfkdsk.bika.BikaApi;

import com.lfkdsk.bika.response.GeneralResponse;
import com.lfkdsk.bika.response.SignInResponse;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BikaApi.getInstance().initClient();

        BikaApi.getInstance().signInCall("1286041359@qq.com","ac7401399").enqueue(new Callback<GeneralResponse<SignInResponse>>() {
            @Override
            public void onResponse(Call<GeneralResponse<SignInResponse>> call, retrofit2.Response<GeneralResponse<SignInResponse>> response) {
                Log.d("登录",response.body().data.getToken());
            }

            @Override
            public void onFailure(Call<GeneralResponse<SignInResponse>> call, Throwable t) {

            }
        });
    }
}