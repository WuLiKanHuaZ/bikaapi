package com.example.bikaapi.http;

public class HttpData<T> {

    /** 返回码 */
    private int code;
    /** 提示语 */
    private String message;
    /** 数据 */
    private T data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    /**
     * 是否请求成功
     */
    public boolean isRequestSucceed() {
        return code == 200;
    }

    /**
     * 是否 Token 失效
     */
    public boolean isTokenFailure() {
        return code == 1001;
    }

}
