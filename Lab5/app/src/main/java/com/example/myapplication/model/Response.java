package com.example.myapplication.model;

public class Response<T> {
    private int status;
    private String messenge;
    //T là kiểu Generic
    private T data;

    public Response() {
    }

    public Response(int status, String messenge, T data) {
        this.status = status;
        this.messenge = messenge;
        this.data = data;
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
}
