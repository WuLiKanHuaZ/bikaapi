package com.example.bikaapi.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

public class LoginApi implements IRequestApi, IRequestType {
    @Override
    public String getApi() {
        return "auth/sign-in";
    }

    @Override
    public BodyType getBodyType() {
        return BodyType.JSON;
    }

    private String email;
    private String password;

    public LoginApi(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
