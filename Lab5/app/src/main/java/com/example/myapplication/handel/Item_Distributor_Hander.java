package com.example.myapplication.handel;

import com.example.myapplication.model.Distributor;

public interface Item_Distributor_Hander { //Tạo xử lý khi click vào item trong adapter
    public void Delete(String id);
    public void Update(String id, Distributor distributor);
}
