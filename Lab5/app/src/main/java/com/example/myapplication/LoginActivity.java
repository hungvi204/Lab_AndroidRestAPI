package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.model.Response;
import com.example.myapplication.model.Users;
import com.example.myapplication.services.HttpRequest_User;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin, btnRegister;
    HttpRequest_User httpRequestUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        httpRequestUser = new HttpRequest_User();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Users users = new Users();
                String _username = edtUsername.getText().toString();
                String _password = edtPassword.getText().toString();
                users.setUsername(_username);
                users.setPassword(_password);
                httpRequestUser.callAPI().login(users).enqueue(responseUser);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    Callback<Response<Users>> responseUser = new Callback<Response<Users>>() {
        @Override
        public void onResponse(Call<Response<Users>> call, retrofit2.Response<Response<Users>> response) {
            if (response.isSuccessful()) {
                // check status code
                if (response.body().getStatus() == 200) {
                    Toast.makeText(LoginActivity.this, "Đăng Nhập thành công", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("INFO", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.putString("refreshToken", response.body().getRefreshToken());
                    editor.putString("id", response.body().getData().get_id());
                    editor.apply();


                    startActivity(new Intent(LoginActivity.this, FruitActivity.class));
                }
            }

        }

        @Override
        public void onFailure(Call<Response<Users>> call, Throwable t) {
            Log.d(">>> error", "onFailure" + t.getMessage());
        }
    };
}