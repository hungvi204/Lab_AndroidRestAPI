package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;
public class Distributor {
    //Có thể dùng Annotations của gson để đổi tên cho các trường nhận vào
    //Ví dụ trường _id nhận từ api, thay vì đặt tên trường trong object là _id
    //Có thể đặt là id và thêm vào Annotations @SerializedName("_id")
    @SerializedName("_id")
    private String id;
    private String name, createAt, updateAt;

    public Distributor() {

    }

    public Distributor(String id, String name, String createAt, String updateAt) {
        this.id = id;
        this.name = name;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
