package com.example.securelock.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.securelock.Constants;
import com.example.securelock.R;
import com.example.securelock.databinding.ActivityForgetPasswordBinding;
import com.example.securelock.mailservice.EmailService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ForgetPasswordActivity extends AppCompatActivity {

    private static final String FILE_NAME = "signUp.txt";

    private ActivityForgetPasswordBinding forgetPasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgetPasswordBinding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(forgetPasswordBinding.getRoot());
        setListeners();
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

    private void handleResetPassword() {
        HashMap<String, String> signUpDetails = getSignUpDetails();
        if (forgetPasswordBinding.inputEmailId.getText().toString().trim().isEmpty()){
            showToast("Please enter registered email");
        } else if(!signUpDetails.containsKey(forgetPasswordBinding.inputEmailId.getText().toString().trim().toLowerCase())) {
            showToast("Please enter the registered email");
        } else {
            sendMailToUser(forgetPasswordBinding.inputEmailId.getText().toString().trim().toLowerCase(),signUpDetails.get(forgetPasswordBinding.inputEmailId.getText().toString().trim().toLowerCase()));
        }
    }

    private void sendMailToUser(String userEmail, String password) {
        EmailService mail = new EmailService(Constants.SENDER_EMAIL, Constants.SENDER_EMAIL_PASS,
                userEmail,
                "User Password",
                "Dear User \n \n" +
                        "Your Account Login Password is "+ password +"\n\n" +
                        "Please don't share this email to anyone, since this email contains sensitive information \n \n" +
                        "Regards,\n \nSecure Lock");
        mail.execute();
        showToast("Password Mail Sent Successfully!");
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setListeners() {
        forgetPasswordBinding.signIn.setOnClickListener(v ->
                onBackPressed());
        forgetPasswordBinding.resetPassBtn.setOnClickListener(v -> handleResetPassword());
    }

}