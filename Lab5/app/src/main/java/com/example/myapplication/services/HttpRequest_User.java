package com.example.myapplication.services;

import static com.example.myapplication.services.ApiServices.BASE_URL;
import static com.example.myapplication.services.ApiServices.BASE_URL_USER;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest_User {
    //Biến interface ApiServices
    private ApiServices requestInterface;

    public HttpRequest_User(){
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL_USER)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiServices.class);
    }

    public ApiServices callAPI(){
        //Get Retrofit
        return requestInterface;
    }
}
