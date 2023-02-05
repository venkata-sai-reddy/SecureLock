package com.example.securelock.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.securelock.Constants;
import com.example.securelock.R;
import com.example.securelock.databinding.ActivityHomeBinding;
import com.example.securelock.login.SignIn;
import com.example.securelock.mailservice.EmailService;
import com.example.securelock.utilities.PreferneceManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity implements PasswordsViwerInterface {

    private static final String FILE_NAME = "passwords.txt";

    private ActivityHomeBinding binding;
    private PreferneceManager preferneceManager;
    private String loggedInUser;
    RecyclerView recyclerView;
    private List<PasswordVO> passwordsOfUser;
    RecycleViewAdapterHome adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferneceManager = new PreferneceManager(getApplicationContext());
        loggedInUser = getIntent().getStringExtra("id");
        setListeners();
        recyclerView = findViewById(R.id.passwordsRecyclerView);
        getPasswords();
    }


    private void getPasswords(){
        loading(true);
        passwordsOfUser = getPasswordDetails();
        if(passwordsOfUser.isEmpty()){
            enableNoData(true);
        } else {
            enableNoData(false);
            loading(false);
            adapter = new RecycleViewAdapterHome(this, this, passwordsOfUser);
            recyclerView.setAdapter(adapter);
        }
    }

    private List<PasswordVO> getPasswordDetails() {
        FileInputStream fis = null;
        List<PasswordVO> passwords= new ArrayList<>();
        HashMap<String, PasswordVO> passwordsMap = new HashMap<>();
        Log.d("fpass","Starting Fetching Passwords");

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {

                String[] arr = text.toString().split(":");
                if (loggedInUser.equalsIgnoreCase(arr[0].split("=")[1])) {
                    PasswordVO pswd = new PasswordVO(arr[0].split("=")[1], arr[1].split("=")[1], arr[2].split("=")[1], arr[3].split("=")[1], arr[4].split("=")[1]);
                    PasswordVO pswdExist = passwordsMap.getOrDefault(pswd.getName(), pswd);
                    if(LocalDate.parse(pswd.getExpireDate()).compareTo(LocalDate.parse(pswdExist.getExpireDate())) >= 0) {
                        passwordsMap.put(pswd.getName(), pswd);
                    } else {
                        passwordsMap.put(pswd.getName(), pswdExist);
                    }
                    Log.d("fpass", pswd.getName());
                }
                sb.append(text).append("\n");
            }
            if(!passwordsMap.isEmpty()){
                for (String name : passwordsMap.keySet()) {
                    if(!passwordsMap.get(name).getStatus().equalsIgnoreCase("D")) {
                        passwords.add(passwordsMap.get(name));
                    }
                }
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
        return passwords;
    }

    private void getSystemGeneratedPassword(PasswordVO passwordVO) {
        passwordVO.setEmailId(loggedInUser);
        passwordVO.setStatus("I");
        passwordVO.setExpireDate(getExpiryDate());
        passwordVO.setPassword(getGeneratedPass());
    }

    private String getExpiryDate() {
        return LocalDate.now().plusMonths(1).toString();
    }

    private String getGeneratedPass() {
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        String alphaCap = alpha.toUpperCase();
        String numeric = "1234567890";
        String special = "!@#$%^&*+-";
        String allCombo = alpha+alphaCap+numeric+special;
        Random rand = new Random();
        StringBuilder password = new StringBuilder();
        password.append(alpha.charAt(rand.nextInt(alpha.length())));
        password.append(alphaCap.charAt(rand.nextInt(alphaCap.length())));
        password.append(numeric.charAt(rand.nextInt(numeric.length())));
        password.append(special.charAt(rand.nextInt(special.length())));
        for(int i=0; i<4; i++){
            password.append(allCombo.charAt(rand.nextInt(allCombo.length())));
        }
        List<String> chars = Arrays.asList(password.toString().split(""));
        Collections.shuffle(chars);
        String finalPass = "";
        for (String letter : chars) {
            finalPass += letter;
        }
        return finalPass;
    }

    private void saveNewPasswords(PasswordVO passwordVO) {
        getSystemGeneratedPassword(passwordVO);
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_APPEND | MODE_PRIVATE);
            fos.write(passwordVO.toString().getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            String message = "Dear User \n \n"+
                    passwordVO.getName()+" Account Password has been set to "+ passwordVO.getPassword()+ " successfully \n \n"+
                    "Please don't share this email to anyone, since email contains sensitive information \n \n" +
                    "Regards,\nSecure Lock";
            sendEmail(passwordVO.getName()+" Password Saved Successfully", message);
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deletePasswordInStore(PasswordVO passwordVO) {
        passwordVO.setStatus("D");
        passwordVO.setExpireDate(getExpiryDate());
        FileOutputStream fos = null;
        try {

            fos = openFileOutput(FILE_NAME, MODE_APPEND | MODE_PRIVATE);
            fos.write(passwordVO.toString().getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            String message = "Dear User \n \n"+
                    passwordVO.getName()+" Account Password has been deleted successfully \n \n"+
                    "Regards,\nSecure Lock";
            sendEmail(passwordVO.getName()+" Account Password Deleted Successfully", message);
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void newPasswordIntitated(Boolean isInitiated) {
        if (isInitiated) {
            binding.passwordsView.setVisibility(View.GONE);
            binding.newPassword.setVisibility(View.GONE);
            binding.newPassForm.setVisibility(View.VISIBLE);
            binding.titleHome.setVisibility(View.GONE);
        } else {
            getPasswords();
            binding.passwordsView.setVisibility(View.VISIBLE);
            binding.newPassword.setVisibility(View.VISIBLE);
            binding.titleHome.setVisibility(View.VISIBLE);
            binding.newPassForm.setVisibility(View.GONE);
        }

    }

    private void enableNoData(Boolean enable) {
        if(enable){
            binding.passwordsRecyclerView.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            binding.passwordNoData.setVisibility(View.VISIBLE);
        } else {
            binding.passwordNoData.setVisibility(View.GONE);
        }
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.passwordsRecyclerView.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.passwordsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setListeners() {
        binding.logOut.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignIn.class)));
        binding.newPassword.setOnClickListener(v -> newPasswordIntitated(true));
        binding.newPassCreate.setOnClickListener(v -> {
            PasswordVO password = new PasswordVO();
            password.setName(binding.passName.getText().toString().trim());
            saveNewPasswords(password);
            newPasswordIntitated(false);
        });
        binding.newPassCancel.setOnClickListener(v -> newPasswordIntitated(false));
    }


    @Override
    public void deletePassword(int position) {
        if(passwordsOfUser.size() > position) {
            deletePasswordInStore(passwordsOfUser.get(position));
            passwordsOfUser.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    @Override
    public void restPassword(int position) {
        if(passwordsOfUser.size() > position) {
            passwordsOfUser.get(position).setPassword(getGeneratedPass());
            passwordsOfUser.get(position).setExpireDate(getExpiryDate());
            saveNewPasswords(passwordsOfUser.get(position));
            adapter.notifyItemChanged(position, passwordsOfUser);
        }
    }

    private void sendEmail(String subject, String message){
        EmailService mail = new EmailService(Constants.SENDER_EMAIL, Constants.SENDER_EMAIL_PASS,
                loggedInUser,
                subject,
                message);
        mail.execute();
    }
}