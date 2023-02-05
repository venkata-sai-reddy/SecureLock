package com.example.securelock.home;

public class PasswordVO {

   private String emailId;
   private String password;
   private String name;
   private String expireDate;
   private String status;

   public PasswordVO(String emailId, String name, String password, String expireDate, String status) {
      this.password = password;
      this.emailId = emailId;
      this.name = name;
      this.expireDate = expireDate;
      this.status = status;
   }
   public PasswordVO() {
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getExpireDate() {
      return expireDate;
   }

   public void setExpireDate(String expireDate) {
      this.expireDate = expireDate;
   }

   public String getEmailId() {
      return emailId;
   }

   public void setEmailId(String emailId) {
      this.emailId = emailId;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   @Override
   public String toString() {
      return "Id=" + emailId + ':' +
              "name=" + name + ':' +
              "password=" + password + ':' +
              "expireDate=" + expireDate+ ':' +
              "status=" + status+ "\n" ;
   }
}
