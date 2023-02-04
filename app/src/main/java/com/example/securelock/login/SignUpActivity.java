package com.example.securelock.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.securelock.Constants;
import com.example.securelock.dao.LoginDAO;
import com.example.securelock.databinding.ActivitySignUpBinding;
import com.example.securelock.mailservice.EmailService;
import com.example.securelock.utilities.PreferneceManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class SignUpActivity extends AppCompatActivity {

    private static final String FILE_NAME = "signUp.txt";

    private ActivitySignUpBinding signUpBinding;
    private PreferneceManager preferneceManager;
    private LoginDAO loginDAO;
    private UserVO user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());
        preferneceManager = new PreferneceManager(getApplicationContext());
        user = new UserVO();
        setListeners();
    }

    private void setListeners() {
        signUpBinding.signIn.setOnClickListener(v -> onBackPressed());
        signUpBinding.signUpBtn.setOnClickListener(v -> {
            if (isValidInformation()){
                Log.d("SendMail", "before sign UP");
                user.setName(signUpBinding.inputName.getText().toString().trim());
                user.setEmailId(signUpBinding.inputEmailId.getText().toString().trim());
                if(signUp(user)){
//                    loginDAO.storeSignUpDetails(getApplicationContext(), user);
                    user.setPassword(signUpBinding.inputNewPassword.getText().toString().trim());
                    saveSignUp(user);
                }
                onBackPressed();
            }
        });
    }

    private void saveSignUp(UserVO user) {
        FileOutputStream fos = null;
        try {

            fos = openFileOutput(FILE_NAME, MODE_APPEND | MODE_PRIVATE);
            fos.write(user.toString().getBytes());

            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Boolean signUp(UserVO user) {
        EmailService mail = new EmailService(Constants.SENDER_EMAIL, Constants.SENDER_EMAIL_PASS,
                user.getEmailId(),
                "Registration Successful!",
                "Hi "+user.getName()+"\n\n" +
                        "Thanks for Registering into Secure Lock Application\n\n" +
                        "Regards,\nSecure Lock Application");
        mail.execute();
        showToast("Registration Successful!!");
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidInformation() {
        if (signUpBinding.inputName.getText().toString().trim().isEmpty()){
            showToast("Please enter name");
            return false;
        }
        if (signUpBinding.inputEmailId.getText().toString().trim().isEmpty()){
            showToast("Please enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(signUpBinding.inputEmailId.getText().toString()).matches()){
            showToast("Please enter valid email!");
            return false;
        }
        if (signUpBinding.inputNewPassword.getText().toString().trim().isEmpty()){
            showToast("Please enter password");
            return false;
        }else if (signUpBinding.inputConfirmPassword.getText().toString().trim().isEmpty()){
            showToast("Please enter confirm password");
            return false;
        } else if(!signUpBinding.inputNewPassword.getText().toString().equals(signUpBinding.inputConfirmPassword.getText().toString())){
            showToast("New Password & Confirm Password must be same");
            return false;
        }
        return true;
    }

}