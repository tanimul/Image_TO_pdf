package com.example.imagetopdf.ModelClass;

public class ModelUser {
    private String userName, userEmail;

    public ModelUser(String userEmail,String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public ModelUser() {
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
}
