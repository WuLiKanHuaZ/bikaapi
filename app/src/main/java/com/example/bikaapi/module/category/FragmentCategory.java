package com.example.bikaapi.module.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.bikaapi.BR;
import com.example.bikaapi.R;
import com.example.bikaapi.databinding.FragmentCategoryBinding;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.base.BaseViewModel;

public class FragmentCategory extends BaseFragment<FragmentCategoryBinding, CategoryViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_category;
    }

    @Override
    public int initVariableId() {
        return BR.CategoryViewModel;
    }
}
