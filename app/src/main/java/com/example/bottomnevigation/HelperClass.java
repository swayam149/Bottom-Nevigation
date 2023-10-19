package com.example.bottomnevigation;

public class HelperClass {
    String name,email_id,mobile_no,username,password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HelperClass(String name, String email_id, String mobile_no, String username, String password) {
        this.name = name;
        this.email_id = email_id;
        this.mobile_no = mobile_no;
        this.username = username;
        this.password = password;
    }

    public HelperClass() {
    }

}
