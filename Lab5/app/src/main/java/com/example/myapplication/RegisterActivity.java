package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.model.Response;
import com.example.myapplication.model.Users;
import com.example.myapplication.services.HttpRequest;
import com.example.myapplication.services.HttpRequest_User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword, edtEmail, edtName;
    Button btnRegister,btnLogin;
    ImageView avatar;

    File file;
    private HttpRequest_User httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        avatar = findViewById(R.id.imgAvatar);
        httpRequest = new HttpRequest_User();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "click", Toast.LENGTH_SHORT).show();
                // sử dụng RequestBody
                RequestBody _username = RequestBody.create(MediaType.parse("multipart/form-data"), edtUsername.getText().toString().trim());
                RequestBody _password = RequestBody.create(MediaType.parse("multipart/form-data"), edtPassword.getText().toString().trim());
                RequestBody _email = RequestBody.create(MediaType.parse("multipart/form-data"), edtEmail.getText().toString().trim());
                RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"), edtName.getText().toString().trim());
                MultipartBody.Part multipartBody;
                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                    // avatar là cùng tên với key trong multipart
                } else {
                    multipartBody = null;
                }
                httpRequest.callAPI().register(_username, _password, _email, _name, multipartBody).enqueue(responseUser);


            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }
    // ham chon anh
    private void chooseImage() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getImage.launch(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    //Activity result sau khi lấy hình
    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // there are no request codes
                Intent data = result.getData();
                Uri imagePath = data.getData();
                // file avatar bien global
                file = createFileFromUri(imagePath, "avatar");
                // glide de load hinh
                Glide.with(RegisterActivity.this)
                        .load(file) // load file hinh
                        .thumbnail(Glide.with(RegisterActivity.this).load(R.drawable.loading))
                        .centerCrop() // center cắt ảnh
                        .circleCrop() // bo tròn ảnh
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // clear cache
                        .skipMemoryCache(true)
                        .into(avatar);
            }
        }
    });

    // hàm tạo file hình từ Uri
    private File createFileFromUri(Uri path, String name) {
        File _file = new File(RegisterActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = RegisterActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    Callback<Response<Users>> responseUser =new Callback<Response<Users>>() {
        @Override
        public void onResponse(Call<Response<Users>> call, retrofit2.Response<Response<Users>> response) {
            if (response.isSuccessful()){
                // check status code
                if (response.body().getStatus() == 200){
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Users>> call, Throwable t) {
            Log.d(">>> error", "onFailure" + t.getMessage());
        }
    };
}