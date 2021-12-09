package com.example.bikaapi.module.login;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import com.example.bikaapi.BR;
import com.example.bikaapi.R;
import com.example.bikaapi.databinding.ActivityLoginBinding;
import com.gyf.immersionbar.ImmersionBar;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.base.BaseViewModel;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.LoginViewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        ImmersionBar.with(this).transparentBar().init();

        binding.ivCenterAnim.setBackgroundResource(R.drawable.bika_loading_big);
        AnimationDrawable animationDrawable = (AnimationDrawable) binding.ivCenterAnim.getBackground();
        animationDrawable.start();

    }
}