package com.example.securelock.dao;

import android.content.Context;
import android.util.Log;

import com.example.securelock.login.UserVO;

public class LoginDAO {

   public void storeSignUpDetails(Context applicationContext, UserVO user) {
      Log.d("Stg", "path : "+ applicationContext.getFilesDir().getAbsolutePath());
   }
}
