package com.example.securelock.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.securelock.R;
import com.example.securelock.databinding.ActivityForgetPasswordBinding;

public class ForgetPasswordActivity extends AppCompatActivity {

    private ActivityForgetPasswordBinding forgetPasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgetPasswordBinding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(forgetPasswordBinding.getRoot());
        setListeners();
    }

    private void setListeners() {
        forgetPasswordBinding.signIn.setOnClickListener(v ->
                onBackPressed());
    }
}