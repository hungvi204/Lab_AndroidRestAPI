package com.example.myapplication.model;

public class DistrictRequest {//model Request lấy danh sách Quận Huyện
    private int province_id;

    public DistrictRequest(int province_id) {
        this.province_id = province_id;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }
}
