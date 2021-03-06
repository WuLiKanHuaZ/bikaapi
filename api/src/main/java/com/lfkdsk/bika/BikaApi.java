package com.lfkdsk.bika;/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lfkdsk.bika.api.BaseRetrofitManager;
import com.lfkdsk.bika.api.BiKaApiService;
import com.lfkdsk.bika.api.RestWakaClient;
import com.lfkdsk.bika.request.SignInBody;
import com.lfkdsk.bika.response.*;
import com.lfkdsk.bika.utils.BikaJni;
import com.lfkdsk.bika.utils.HttpDns;

import me.goldze.mvvmhabit.http.interceptor.logging.Logger;
import me.goldze.mvvmhabit.http.interceptor.logging.LoggingInterceptor;
import me.goldze.mvvmhabit.utils.SPUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class BikaApi extends BaseRetrofitManager<BiKaApiService> {
    public static List<String> dns = Arrays.asList("104.20.180.50", "104.20.181.50");

    private String TAG = "BikaApi";
    //-------------------    bika   API  --------------------------;
    private String API_KEY = "C69BAF41DA5ABD1FFEDC6D2FEA56B";
    private String BASE_URL_PIKA = "https://picaapi.picacomic.com/";
    private String CERT_URL = "picaapi.picacomic.com";
    private String buildVersion = "45";
    private String uuid = "defaultUuid";
    private String version = "2.2.1.3.3.4";
    private int channel = 2;
    private String token = "";
    private String imageServer = "https://s3.picacomic.com/static/";
    private OkHttpClient client;

    public class BiKaIntercept implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            String uid = UUID.randomUUID().toString().replace("-", "");
            String url = original.url().toString().replace(BASE_URL_PIKA, "");
            String time = String.valueOf(System.currentTimeMillis() / 1000);
            Log.d("URl",url+"||"+original.url());
            String[] params = {BASE_URL_PIKA, url, time, uid, original.method(), API_KEY, version, buildVersion};
            String signature = BikaJni.INSTANCE.getStringCon(params);
            Response response = chain.proceed(
                    original.newBuilder()
                            .header("api-key", API_KEY)
                            .header("accept", "application/vnd.picacomic.com.v1+json")
                            .header("app-channel", "2")
                            .header("time", time)
                            .header("authorization", getToken())
                            .header("nonce", uid)
                            .header("signature", signature)
                            .header("app-version", version)
                            .header("app-uuid", uuid)
                            .header("image-quality", "original") // ?????????????????????????????????
                            .header("app-platform", "android")
                            .header("app-build-version", buildVersion)
                            .header("user-agent", "okhttp/3.8.1")
                            .method(original.method(), original.body())
                            .build()
            );

            String serverTime = response.headers().get("Server-Time");
            if (serverTime != null) {
//                    String diffTime = java.lang.Long.parseLong(serverTime) - System.currentTimeMillis() / 1000;
//                    PreferenceHelper.setTimeDifference(context, diffTime)
            }
            return response;
        }
    }

    public void initClient() {
        initClient(new ArrayList<>());
    }

    public void initClient(List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS);
        builder.dns(new HttpDns());
        builder.addInterceptor(new BiKaIntercept());

        LoggingInterceptor loggingInterceptor = new LoggingInterceptor.Builder()
                .logger(Logger.DEFAULT)
                .build();
        builder.addInterceptor(loggingInterceptor);
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }
        BiKaApiService api = new Retrofit.Builder()
                .baseUrl(BASE_URL_PIKA)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build()
                .create(BiKaApiService.class);
        this.setApi(api);
        this.client = builder.build();
    }

    public List<String> dns() throws IOException {
        retrofit2.Response<WakaInitResponse> res = new RestWakaClient().getApiService().getWakaInit().execute();
        if (res.body() != null) {
            return dns = res.body().addresses;
        }

        return Collections.emptyList();
    }

    public Call<GeneralResponse<CategoryResponse>> getCategories(){
        return this.getApi().getCategories(token);
    }

    public Call<GeneralResponse<SignInResponse>> signInCall(String name, String password){
        return this.getApi().signIn(new SignInBody(name, password));
    }

    public List<Category> categories() throws IOException {
        retrofit2.Response<GeneralResponse<CategoryResponse>> res = BikaApi.getInstance().getApi().getCategories(token).execute();
        if (res.body() == null) {
            return null;
        }

        GeneralResponse<CategoryResponse> body = res.body();
        CategoryResponse data = body.data;
        if (data == null || data.categories == null) {
            return null;
        }

        return data.categories.stream()
                .filter(category -> category.getActive() || category.getCategoryId() != null)
                .collect(Collectors.toList());
    }

    public ComicPage page(String category, int page) throws IOException {
        retrofit2.Response<GeneralResponse<ComicListResponse>> res =
                getInstance().getApi().getComicList(token, page, category, null, null, null, "ua", null, null).execute();
        if (res.body() == null) {
            return null;
        }

        GeneralResponse<ComicListResponse> body = res.body();
        ComicListResponse data = body.data;
        if (data == null || data.getComics() == null) {
            return null;
        }

        return data.getComics();
    }

    public String initImage(String token) throws IOException {
        retrofit2.Response<GeneralResponse<InitialResponse>> res = getInstance().getApi().getInit(token).execute();
        if (res.body() == null) {
            return null;
        }

        GeneralResponse<InitialResponse> body = res.body();
        InitialResponse data = body.data;
        if (data == null) {
            return null;
        }

        return imageServer = data.imageServer;
    }

    public ComicEpisodeData eps(String comicId, int page) throws IOException {
        retrofit2.Response<GeneralResponse<ComicEpisodeResponse>> res = getInstance().getApi().getComicEpisode(token, comicId, page).execute();
        if (res.body() == null) {
            return null;
        }

        GeneralResponse<ComicEpisodeResponse> body = res.body();
        ComicEpisodeResponse data = body.data;
        if (data == null) {
            return null;
        }

        return data.getEps();
    }

    public ComicPageData pages(String id, int order, int page) throws IOException {
        retrofit2.Response<GeneralResponse<ComicPagesResponse>> res = getInstance().getApi().getPagesWithOrder(token, id, order, page).execute();
        if (res.body() == null) {
            return null;
        }

        GeneralResponse<ComicPagesResponse> body = res.body();
        ComicPagesResponse data = body.data;
        if (data == null) {
            return null;
        }

        return data.getPages();
    }

    public Request pageRequest(String categoryName, int page) {
        return getInstance().getApi().getComicList(token, page, categoryName, null, null, null, "ua", null, null).request();
    }

    public Request chapterRequest(String comicId, int page) {
        return getApi().getComicEpisode(token, comicId, page).request();
    }

    public Request searchRequest(String query, int page) {
        return getApi().getComicListWithSearchKey(token, page, query).request();
    }

    public Request detailRequest(String comicId) {
        return getApi().getComicWithId(token, comicId).request();
    }

    public Request graphRequest(String id, int order) {
        return getApi().getPagesWithOrder(token, id, order, 1).request();
    }

    public static final class INSTANCE {
        static final BikaApi instance = new BikaApi();
    }

    private BikaApi() {
    }

    public static BikaApi getInstance() {
        return INSTANCE.instance;
    }

    public void setToken(String token) {
        this.token = token;
        SPUtils.getInstance().put("token",token);
    }

    public String getToken() {
        if (TextUtils.isEmpty(token)){
            token = SPUtils.getInstance().getString("token");
        }
        return token;
    }

    public String getImageServer() {
        return imageServer;
    }

    public void setImageServer(String imageServer) {
        this.imageServer = imageServer;
    }

    public OkHttpClient getClient() {
        return client;
    }
}
