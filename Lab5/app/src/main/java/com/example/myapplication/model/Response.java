package com.example.myapplication.model;

public class Response<T> {
    private int status;
    private String messenge;
    //T là kiểu Generic
    private T data;
    private String token;
    private String refreshToken;

    public Response() {
    }

    public Response(int status, String messenge, T data, String token, String refreshToken) {
        this.status = status;
        this.messenge = messenge;
        this.data = data;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessenge() {
        return messenge;
    }

    public void setMessenge(String messenge) {
        this.messenge = messenge;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
