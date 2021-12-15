package com.example.android.foodorderingapp;

public class UserModal {
    private String userName;
    private String userNumber;
    private String userEmail;
    private String userPinCode;
    private String userAddress;
    private String userPassword;

    public UserModal(String userName, String userNumber, String userEmail, String userPinCode, String userAddress, String userPassword) {
        this.userName = userName;
        this.userNumber = userNumber;
        this.userEmail = userEmail;
        this.userPinCode = userPinCode;
        this.userAddress = userAddress;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPincode() {
        return userPinCode;
    }

    public void setUserPincode(String userPincode) {
        this.userPinCode = userPincode;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
