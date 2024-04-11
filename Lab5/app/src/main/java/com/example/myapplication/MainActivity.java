package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.Recycle_Item_Distributors;
import com.example.myapplication.handel.Item_Distributor_Hander;
import com.example.myapplication.model.Distributor;
import com.example.myapplication.model.Response;
import com.example.myapplication.services.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity implements Item_Distributor_Hander {
    private HttpRequest httpRequest;
    private RecyclerView recycle_distributor;
    private Recycle_Item_Distributors adapter;


    EditText edtSearch;
    Button btnAdd;
    EditText edtNameDistributor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycle_distributor = findViewById(R.id.ryc_distributor);
        edtSearch = findViewById(R.id.edtSearch);
        btnAdd = findViewById(R.id.btnAdd);

        // Khởi tạo services request
        httpRequest = new HttpRequest();
        httpRequest.callAPI()
                .getListDistributor() // Phương thức api cần thực thi
                .enqueue(getDistributorAPI); // Xử lý bất đồng bộ

        btnAdd.setOnClickListener(v->{
            showDialog();
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    //Lấy từ khóa từ ô tìm kiếm
                    String key = edtSearch.getText().toString();

                    httpRequest.callAPI()
                            .searchDistributor(key)
                            .enqueue(getDistributorAPI);

                    return true;
                }
                return false;
            }
        });
        
    }

    private void getData(ArrayList<Distributor> ds) {
        adapter = new Recycle_Item_Distributors(this, ds, this);
        recycle_distributor.setLayoutManager(new LinearLayoutManager(this));
        recycle_distributor.setAdapter(adapter);
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item_distributor, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

        edtNameDistributor = view.findViewById(R.id.edtName);
        Button btnAdd = view.findViewById(R.id.dialogAdd);
        Button btnBack = view.findViewById(R.id.dialogBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                alertDialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(v->{
            if(!edtNameDistributor.getText().toString().isEmpty()){
                Distributor distributor = new Distributor();
                distributor.setName(edtNameDistributor.getText().toString().trim());
                httpRequest.callAPI()
                        .addDistributor(distributor)
                        .enqueue(responseDistributorAPI);
                alertDialog.dismiss();
            }else {
                Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
            }
        });
    }

    Callback<Response<ArrayList<Distributor>>> getDistributorAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    ArrayList<Distributor> ds = response.body().getData();
                    getData(ds);
//                    Toast.makeText(MainActivity.this, response.body().getMessenge(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {
//            Log.d("GetLisDistributor", "onFaile", + t.getMessage());
        }
    };

    Callback<Response<Distributor>> responseDistributorAPI = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    httpRequest.callAPI()
                            .getListDistributor()
                            .enqueue(getDistributorAPI);
                    Toast.makeText(MainActivity.this, ""+response.body().getMessenge(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable t) {
//            Log.d("GetLisDistributor", "onFaile", + t.getMessage());
        }
    };

    @Override
    public void Delete(String id) {
        httpRequest.callAPI()
                .deleteDistributorById(id)
                .enqueue(responseDistributorAPI);
    }

    @Override
    public void Update(String id, Distributor distributor) {
        httpRequest.callAPI()
                .updateDistributorById(id, distributor)
                .enqueue(responseDistributorAPI);
    }
}
