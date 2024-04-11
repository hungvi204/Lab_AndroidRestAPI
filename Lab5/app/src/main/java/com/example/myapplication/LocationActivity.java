package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.adapter.Adapter_Item_District_Seclect_GHN;
import com.example.myapplication.adapter.Adapter_Item_Province_Seclect_GHN;
import com.example.myapplication.adapter.Adapter_Item_Ward_Seclect_GHN;
import com.example.myapplication.model.District;
import com.example.myapplication.model.DistrictRequest;
import com.example.myapplication.model.Province;
import com.example.myapplication.model.ResponseGHN;
import com.example.myapplication.model.Ward;
import com.example.myapplication.services.GHNRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity {
    GHNRequest ghnRequest;
    Spinner sp_province, sp_district, sp_ward;
    EditText edt_location;
    Button btn_next;
    //lưu dữ liệu chọn
    private String WardCode;
    private int DistrictID;
    private int ProvinceID;
    private Adapter_Item_Ward_Seclect_GHN adapter_item_ward_seclect_ghn;
    private Adapter_Item_Province_Seclect_GHN adapter_item_province_seclect_ghn;
    private Adapter_Item_District_Seclect_GHN adapter_item_district_seclect_ghn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ghnRequest = new GHNRequest();
        sp_province = findViewById(R.id.sp_province);
        sp_district = findViewById(R.id.sp_district);
        sp_ward = findViewById(R.id.sp_ward);
        edt_location = findViewById(R.id.edtLocation);
        btn_next = findViewById(R.id.btnNext);
        //gọi api lấy danh sách TP/Tỉnh đầu tiên
        ghnRequest.callAPI().getListProvince().enqueue(responseProvince);
        //lắng nghe sự kiện chọn
        sp_province.setOnItemSelectedListener(onItemSelectedListener);
        sp_district.setOnItemSelectedListener(onItemSelectedListener);
        sp_ward.setOnItemSelectedListener(onItemSelectedListener);
        sp_province.setSelection(0);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LocationActivity.this, "Gửi thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getId() == R.id.sp_province) {
                ProvinceID = ((Province) adapterView.getAdapter().getItem(i)).getProvinceID();
                DistrictRequest districtRequest = new DistrictRequest(ProvinceID);
                ghnRequest.callAPI().getListDistrict(districtRequest).enqueue(responseDistrict);
            } else if (adapterView.getId() == R.id.sp_district) {
                DistrictID = ((District) adapterView.getAdapter().getItem(i)).getDistrictID();
                ghnRequest.callAPI().getListWard(DistrictID).enqueue(responseWard);
            } else if (adapterView.getId() == R.id.sp_ward) {
                WardCode = ((Ward) adapterView.getAdapter().getItem(i)).getWardCode();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    private void SetDataSpinProvince(ArrayList<Province> ds){
        adapter_item_province_seclect_ghn = new Adapter_Item_Province_Seclect_GHN(this, ds);
        sp_province.setAdapter(adapter_item_province_seclect_ghn);
        adapter_item_province_seclect_ghn.notifyDataSetChanged();
    }
    private void SetDataSpinDistrict(ArrayList<District> ds){
        adapter_item_district_seclect_ghn = new Adapter_Item_District_Seclect_GHN(this, ds);
        sp_district.setAdapter(adapter_item_district_seclect_ghn);
    }
    private void SetDataSpinWard(ArrayList<Ward> ds){
        adapter_item_ward_seclect_ghn = new Adapter_Item_Ward_Seclect_GHN(this, ds);
        sp_ward.setAdapter(adapter_item_ward_seclect_ghn);
    }
    Callback<ResponseGHN<ArrayList<Province>>> responseProvince = new Callback<ResponseGHN<ArrayList<Province>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Province>>> call, Response<ResponseGHN<ArrayList<Province>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    ArrayList<Province> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinProvince(ds);
                    // Hiển thị dữ liệu trên Logcat
                    for (Province province : ds) {
                        Log.d("ProvinceInfo", "Province Name: " + province.getProvinceName());
                        Log.d("ProvinceInfo", "Province ID: " + province.getProvinceID());
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Province>>> call, Throwable t) {

        }
    };
    Callback<ResponseGHN<ArrayList<District>>> responseDistrict = new Callback<ResponseGHN<ArrayList<District>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<District>>> call, Response<ResponseGHN<ArrayList<District>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    ArrayList<District> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinDistrict(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<District>>> call, Throwable t) {

        }
    };
    Callback<ResponseGHN<ArrayList<Ward>>> responseWard = new Callback<ResponseGHN<ArrayList<Ward>>>() {
        @Override
        public void onResponse(Call<ResponseGHN<ArrayList<Ward>>> call, Response<ResponseGHN<ArrayList<Ward>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getCode() == 200) {
                    ArrayList<Ward> ds = new ArrayList<>(response.body().getData());
                    SetDataSpinWard(ds);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseGHN<ArrayList<Ward>>> call, Throwable t) {

        }
    };
}