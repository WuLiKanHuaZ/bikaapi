package com.example.bikaapi.module.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.text.TextUtils;

import com.example.bikaapi.BR;
import com.example.bikaapi.R;
import com.example.bikaapi.SPkey;
import com.example.bikaapi.databinding.ActivityMainBinding;
import com.example.bikaapi.module.category.FragmentCategory;
import com.example.bikaapi.module.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.utils.SPUtils;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.MainViewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        if (TextUtils.isEmpty(SPUtils.getInstance().getString(SPkey.TOKEN))){
            startActivity(LoginActivity.class);
            finish();
            return;
        }

        initPager();
    }

    private void initPager(){
        String[] titles = new String[]{"分类","游戏"};
        binding.topTab.addTab(binding.topTab.newTab().setText("分类"));
        binding.topTab.addTab(binding.topTab.newTab().setText("游戏"));

        fragments.add(new FragmentCategory());
        fragments.add(new FragmentCategory());
        binding.topTab.setupWithViewPager(binding.viewPager);
        binding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });

    }

}