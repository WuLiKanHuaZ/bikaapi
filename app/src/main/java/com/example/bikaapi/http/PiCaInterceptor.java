package com.example.bikaapi.http;

import static com.lfkdsk.bika.BikaConfig.BASE_URL_PIKA;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bikaapi.BuildConfig;
import com.example.bikaapi.SPkey;
import com.hjq.http.config.IRequestInterceptor;
import com.hjq.http.model.HttpHeaders;
import com.hjq.http.model.HttpParams;
import com.hjq.http.request.HttpRequest;
import com.lfkdsk.bika.BikaConfig;
import com.lfkdsk.bika.utils.BikaJni;

import java.io.IOException;
import java.util.UUID;

import me.goldze.mvvmhabit.utils.SPUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class PiCaInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        String uid = UUID.randomUUID().toString().replace("-", "");
        String url = original.url().toString().replace(BASE_URL_PIKA, "");
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        Log.d("URl",url+"||"+original.url());
        String[] params = {BASE_URL_PIKA, url, time, uid, original.method(), BikaConfig.API_KEY, BikaConfig.version, BikaConfig.buildVersion};
        String signature = BikaJni.INSTANCE.getStringCon(params);
        String token = SPUtils.getInstance().getString(SPkey.TOKEN);
        Response response = chain.proceed(
                original.newBuilder()
                        .header("api-key", BikaConfig.API_KEY)
                        .header("accept", "application/vnd.picacomic.com.v1+json")
                        .header("app-channel", "2")
                        .header("time", time)
                        .header("authorization", token)
                        .header("nonce", uid)
                        .header("signature", signature)
                        .header("app-version", BikaConfig.version)
                        .header("app-uuid", BikaConfig.uuid)
                        .header("image-quality", "original") // 哔咔服务器加载图片质量
                        .header("app-platform", "android")
                        .header("app-build-version", BikaConfig.buildVersion)
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
