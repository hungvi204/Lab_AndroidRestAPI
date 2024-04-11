package com.example.myapplication.services;

import com.example.myapplication.model.District;
import com.example.myapplication.model.DistrictRequest;
import com.example.myapplication.model.Province;
import com.example.myapplication.model.ResponseGHN;
import com.example.myapplication.model.Ward;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GHNServices {
    public static String GHN_URL = "https://dev-online-gateway.ghn.vn/";

    //Get danh sách TP/Tỉnh
    @GET("shiip/public-api/master-data/province")
    Call<ResponseGHN<ArrayList<Province>>> getListProvince();
    //Get danh sách Quận/Huyện
    @POST("shiip/public-api/master-data/district")
    Call<ResponseGHN<ArrayList<District>>> getListDistrict(@Body DistrictRequest districtRequest);
    //Get danh sách Phường/ Xã
    @GET("shiip/public-api/master-data/ward")
    Call<ResponseGHN<ArrayList<Ward>>> getListWard(@Query("district_id") int district_id);
}
