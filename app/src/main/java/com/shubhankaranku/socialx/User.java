package com.shubhankaranku.socialx;

public class User {
    public String uid;
    public String email;
    public String user_name;
    public String number;
    public String password;
    public User(){

    }

    public User(String user_id, String name, String email, String number, String password) {
        this.uid = user_id;
        this.email = email;
        this.user_name=name;
        this.number=number;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
