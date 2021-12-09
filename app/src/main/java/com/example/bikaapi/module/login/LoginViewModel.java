package com.example.bikaapi.module.login;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.example.bikaapi.module.main.MainActivity;
import com.lfkdsk.bika.BikaApi;
import com.lfkdsk.bika.response.GeneralResponse;
import com.lfkdsk.bika.response.SignInResponse;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginViewModel extends BaseViewModel {

    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public BindingCommand bindingLogin = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(email.get())){
                ToastUtils.showShort("账号不能为空");
                return;
            }
            if (TextUtils.isEmpty(password.get())){
                ToastUtils.showShort("密码不能为空");
                return;
            }
            login();
        }
    });

    private void login(){
        BikaApi.getInstance().signInCall(email.get(),password.get()).enqueue(new Callback<GeneralResponse<SignInResponse>>() {
            @Override
            public void onResponse(Call<GeneralResponse<SignInResponse>> call, retrofit2.Response<GeneralResponse<SignInResponse>> response) {
                BikaApi.getInstance().setToken(response.body().data.getToken());
                startActivity(MainActivity.class);
                finish();
            }

            @Override
            public void onFailure(Call<GeneralResponse<SignInResponse>> call, Throwable t) {

            }
        });
    }

}
