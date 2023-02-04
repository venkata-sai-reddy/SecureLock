package com.example.securelock.login;

public class UserVO {

    private String name;
    private String emailId;
    private String password;

    UserVO() {
    }

    UserVO(String name, String emailId){
        this.name = name;
        this.emailId = emailId;
    }

    UserVO(String name, String emailId, String password){
        this.name = name;
        this.emailId = emailId;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "name=" + name + ":" +
                ", emailId=" + emailId + ":"+
                ", password=" + password+ "\n" ;
    }
}
