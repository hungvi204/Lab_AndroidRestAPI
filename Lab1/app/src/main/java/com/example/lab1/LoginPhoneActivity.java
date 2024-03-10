package com.example.lab1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginPhoneActivity extends AppCompatActivity {
    Button btnGetOTP, btnLogin;
    EditText edtOTP, edtPhone;
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);

        btnLogin = findViewById(R.id.btnLogin);
        btnGetOTP = findViewById(R.id.btnGetOTP);
        edtOTP = findViewById(R.id.edtOTP);
        edtPhone = findViewById(R.id.edtPhone);
        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                String code = credential.getSmsCode();
                if (code != null) {
                    edtOTP.setText(code);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // Xử lý khi xác minh thất bại
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
            }
        };

        btnGetOTP.setOnClickListener(v -> {
            String phoneNumber = edtPhone.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                getOTP(phoneNumber);
            } else {
                Toast.makeText(LoginPhoneActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogin.setOnClickListener(v -> {
            String code = edtOTP.getText().toString().trim();
            if (!code.isEmpty()) {
                verifyOTP(code);
            } else {
                Toast.makeText(LoginPhoneActivity.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getOTP(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void verifyOTP(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginPhoneActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(LoginPhoneActivity.this, LogoutActivity.class));
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginPhoneActivity.this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
