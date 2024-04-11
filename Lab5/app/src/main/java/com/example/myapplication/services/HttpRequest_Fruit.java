package com.example.myapplication.services;

import static com.example.myapplication.services.ApiServices.BASE_URL_FRUIT;
import static com.example.myapplication.services.ApiServices.BASE_URL_USER;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest_Fruit {
    private ApiServices requestInterface;

    // Constructor mặc định không có tham số
    public HttpRequest_Fruit() {
        // Khởi tạo đối tượng requestInterface
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL_FRUIT)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiServices.class);
    }

//     Constructor có tham số
    public HttpRequest_Fruit(String token) {
        // Tạo OkHttpClient với interceptor để thêm token vào header
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+ token).build();
                return chain.proceed(request);
            }
        });

        // Khởi tạo đối tượng requestInterface với OkHttpClient đã được cấu hình
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL_FRUIT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build().create(ApiServices.class);
    }

    // Phương thức gọiAPI để trả về đối tượng requestInterface
    public ApiServices callAPI(){
        return requestInterface;
    }
}
