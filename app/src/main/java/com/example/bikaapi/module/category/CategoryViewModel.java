package com.example.bikaapi.module.category;

import android.app.Application;

import androidx.annotation.NonNull;

import com.lfkdsk.bika.BikaApi;
import com.lfkdsk.bika.response.Category;
import com.lfkdsk.bika.response.CategoryResponse;
import com.lfkdsk.bika.response.GeneralResponse;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends BaseViewModel {

    public SingleLiveEvent<List<Category>> category = new SingleLiveEvent<>();

    public SingleLiveEvent<List<Category>> getCategory() {
        return category;
    }

    public CategoryViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        categoryRequest();
    }

    public void categoryRequest(){
        BikaApi.getInstance().getCategories().enqueue(new Callback<GeneralResponse<CategoryResponse>>() {
            @Override
            public void onResponse(Call<GeneralResponse<CategoryResponse>> call, Response<GeneralResponse<CategoryResponse>> response) {
                assert response.body() != null;
                assert response.body().data != null;
                category.setValue(response.body().data.getCategories());
            }

            @Override
            public void onFailure(Call<GeneralResponse<CategoryResponse>> call, Throwable t) {

            }
        });
    }

}
