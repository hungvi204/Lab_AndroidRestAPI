package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.adapter.Recycle_Item_Fruits;
import com.example.myapplication.model.Fruit;
import com.example.myapplication.model.Page;
import com.example.myapplication.model.Response;
import com.example.myapplication.services.HttpRequest_Fruit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class FruitActivity extends AppCompatActivity implements Serializable {
    private HttpRequest_Fruit httpRequestFruit;
    private RecyclerView recyclerView;
    private Recycle_Item_Fruits adapter;
    private SharedPreferences sharedPreferences;
    private String token;
    private ProgressBar loadmore;
    private ArrayList<Fruit> ds = new ArrayList<>();
    private int page = 1;
    private int totalPage = 0;
    private NestedScrollView nestedScrollView;
    private Spinner spinner;
    private String sort;
    EditText edtSeach, edtSeachPrice;
    Button btnLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);

        recyclerView = findViewById(R.id.ryc_fruit);
        loadmore = findViewById(R.id.loadmore);
        nestedScrollView = findViewById(R.id.nestScrollView);
        spinner = findViewById(R.id.spinner);
        edtSeach = findViewById(R.id.edtSearch);
        edtSeachPrice = findViewById(R.id.edtSearchPrice);
        btnLoc = findViewById(R.id.btnLoc);
        sharedPreferences = getSharedPreferences("INFO", MODE_PRIVATE);
        //Lấy token từ sharedPreferences
        token = sharedPreferences.getString("token", "");
        httpRequestFruit = new HttpRequest_Fruit(token);
        onResume();
//        httpRequestFruit.callAPI().getListFruit().enqueue(getListFruitRespone1);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (totalPage == page) return;
                    if (loadmore.getVisibility() == View.GONE) {
                        loadmore.setVisibility(View.VISIBLE);
                        page ++;//tăng page
                        //Call API
                        FilterFruit();
                    }
                }
            }
        });

        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //đưa page về 1
                page = 1;
                //Nếu lọc sẽ clean list
                ds.clear();
                adapter.notifyDataSetChanged();
                FilterFruit();
            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_price, android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence value = (CharSequence) adapterView.getAdapter().getItem(i);
                if (value.toString().equals("Ascending")) {
                    //biến sort toàn cục , tăng dần
                    sort = "1";
                }else if (value.toString().equals("Decrease")){
                    //Giảm dần
                    sort = "-1";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setSelection(1);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //filter mặc định
        Map<String, String> map = getMapFilter(page, "", "0", "-1");
        httpRequestFruit.callAPI().getPageFruit(map).enqueue(getListFruitRespone);
    }

    Callback<Response<Page<ArrayList<Fruit>>>> getListFruitRespone = new Callback<Response<Page<ArrayList<Fruit>>>>() {
        @Override
        public void onResponse(Call<Response<Page<ArrayList<Fruit>>>> call, retrofit2.Response<Response<Page<ArrayList<Fruit>>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    //set totalPage
                    totalPage = response.body().getData().getTotalPage();
                    //Lay data
                    ArrayList<Fruit> _ds = response.body().getData().getData();
                    getData(_ds);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Page<ArrayList<Fruit>>>> call, Throwable t) {
            Log.e("FruitActivity", "Error fetching fruit data: " + t.getMessage());
            Toast.makeText(FruitActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
        }
    };

//    Callback<Response<ArrayList<Fruit>>> getListFruitRespone1 = new Callback<Response<ArrayList<Fruit>>>() {
//        @Override
//        public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
//            if (response.isSuccessful()) {
//                if (response.body().getStatus() == 200) {
//                    //Lay data
//                    ArrayList<Fruit> _ds = response.body().getData();
//                    getData(_ds);
//                }
//            }
//        }
//
//        @Override
//        public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {
//
//        }
//    };

    private void getData(ArrayList<Fruit> _ds){
        //Kiểm tra nếu process load more chạy thì chỉ cần add thêm fruit vào list
        if (loadmore.getVisibility() == View.VISIBLE) {
            //Do chạy ở local nên tốc độ mạng tốt nên sẽ thêm đoạn code delay
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyItemInserted(ds.size() -1);
                    loadmore.setVisibility(View.GONE);
                    ds.addAll(_ds);
                    //thông báo adapter dữ liệu thay đổi
                    adapter.notifyDataSetChanged();
                }
            }, 4000);
            return;
        }
        ds.addAll(_ds);
        adapter = new Recycle_Item_Fruits(this, ds);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        // Thiết lập khoảng cách giữa các item trong RecyclerView
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing); // Định nghĩa khoảng cách trong file dimens.xml
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    //Hàm call api theo filter
    private void FilterFruit(){
        String _name = edtSeach.getText().toString().equals("") ? "" : edtSeach.getText().toString();
        String _price = edtSeachPrice.getText().toString().equals("") ? "0" : edtSeachPrice.getText().toString();
        String _sort = (sort != null && sort.equals("")) ? "-1" : sort;

        Map<String, String> map = getMapFilter(page, _name, _price, _sort   );
        httpRequestFruit.callAPI().getPageFruit(map).enqueue(getListFruitRespone);
    }

    //hàm setup MapQuery
    private Map<String, String> getMapFilter(int _page, String _name, String _price, String _sort){
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(_page));
        map.put("name", _name);
        map.put("price", _price);
        map.put("sort", _sort);
        return map;
    }
}