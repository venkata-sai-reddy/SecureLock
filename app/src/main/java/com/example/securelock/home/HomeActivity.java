package com.example.securelock.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.securelock.R;
import com.example.securelock.databinding.ActivityHomeBinding;
import com.example.securelock.login.ForgetPasswordActivity;
import com.example.securelock.login.SignIn;
import com.example.securelock.login.SignUpActivity;
import com.example.securelock.utilities.PreferneceManager;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private PreferneceManager preferneceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferneceManager = new PreferneceManager(getApplicationContext());
        setListeners();
        getPasswords();
    }


    private void getPasswords(){
        loading(true);
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setListeners() {
        binding.logOut.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignIn.class)));
    }
}