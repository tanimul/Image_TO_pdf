package com.example.imagetopdf.ModelClass;

public class ModelUser {
    private String userName, userEmail;

    public ModelUser(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
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
