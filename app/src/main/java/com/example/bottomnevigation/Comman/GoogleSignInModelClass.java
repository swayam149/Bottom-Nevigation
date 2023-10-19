package com.example.bottomnevigation.Comman;

public class GoogleSignInModelClass {
    String userId,userName,userEmail,userProfilePhoto;

    public GoogleSignInModelClass(String userId, String userName, String userEmail, String userProfilePhoto) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userProfilePhoto = userProfilePhoto;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

    public GoogleSignInModelClass()
    {}

}
