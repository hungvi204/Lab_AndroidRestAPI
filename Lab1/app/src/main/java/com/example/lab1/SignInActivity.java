package com.example.lab1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText email, password;
    Button btnSignIn, btnSignUp;
    TextView tvForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.edtUser);
        password = findViewById(R.id.edtPass);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvForgotPass = findViewById(R.id.tvForgotPass);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
                    dangNhapTaiKhoan(userEmail, userPassword);
                } else {
                    Toast.makeText(SignInActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    public void dangNhapTaiKhoan(String email, String password) {
        // Sử dụng FirebaseAuth để đăng nhập
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Lấy thông tin tài khoản mới đăng nhập
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this, LogoutActivity.class));
                        } else {
                            // Hiển thị thông báo đăng nhập thất bại
                            Toast.makeText(SignInActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();

                            // Ghi log lỗi
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}
