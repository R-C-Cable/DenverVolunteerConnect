package com.example.denvervolunteerconnect.googleauth;

public class SignInResult {
    private UserData data = null;
    private String errorMessage = null;

    public SignInResult(UserData data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
    }

}
