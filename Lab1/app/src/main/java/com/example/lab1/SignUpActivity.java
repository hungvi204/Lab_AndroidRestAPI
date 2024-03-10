package com.example.lab1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText email, password;
    Button btnSignIn, btnSignUp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.edtUser);
        password = findViewById(R.id.edtPass);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
                    dangKyTaiKhoan(userEmail, userPassword);
                } else {
                    Toast.makeText(SignUpActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void dangKyTaiKhoan(String email, String password) {
        // Sử dụng FirebaseAuth để đăng ký tài khoản
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Lấy thông tin tài khoản mới đăng ký
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Hiển thị thông báo đăng ký thành công
                            Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                            // Quay trở lại màn hình đăng nhập
                            finish();
                        } else {
                            // Hiển thị thông báo đăng ký thất bại
                            Toast.makeText(SignUpActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();

                            // Ghi log lỗi
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}
