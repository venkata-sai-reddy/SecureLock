package com.example.securelock.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.securelock.databinding.ActivitySignInBinding;
import com.example.securelock.home.HomeActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    private static final String FILE_NAME = "signUp.txt";

    private ActivitySignInBinding signInBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(signInBinding.getRoot());
        setListeners();
        signIn();
    }

    private void signIn() {
        signInBinding.signInBtn.setOnClickListener(v -> {

            Log.d("loginInfo","on click");
            if(isValidSignIn()){
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("id", signInBinding.inputEmailId.getText().toString().trim());
                startActivity(intent);
            } else {
                showToast("Please check SignIn information");
            }
        });
    }

    private boolean isValidSignIn() {
        if(!isValidInformation()){
            return false;
        }

        HashMap<String, String> loginMap = getSignUpDetails();
        if(loginMap.containsKey(signInBinding.inputEmailId.getText().toString().trim())){
            return loginMap.get(signInBinding.inputEmailId.getText().toString().trim()).equals(signInBinding.inputPassword.getText().toString());
        } else {
            showToast("Not Registered, please register before login!!!");
        }
        return false;
    }

    private HashMap<String, String> getSignUpDetails() {
        FileInputStream fis = null;
        HashMap<String, String> signUpMap = new HashMap<>();
        Log.d("loginInfo","Starting");

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {

                String[] arr = text.toString().split(":");
                signUpMap.put(arr[1].split("=")[1], arr[2].split("=")[1]);
                sb.append(text).append("\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return signUpMap;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidInformation() {
        if (signInBinding.inputEmailId.getText().toString().trim().isEmpty()){
            showToast("Please enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(signInBinding.inputEmailId.getText().toString()).matches()){
            showToast("Please enter valid email!");
            return false;
        }
        if (signInBinding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Please enter password");
            return false;
        }
        return true;
    }

    private void setListeners() {
        signInBinding.signUp.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        signInBinding.forgotPassword.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class)));
    }
}