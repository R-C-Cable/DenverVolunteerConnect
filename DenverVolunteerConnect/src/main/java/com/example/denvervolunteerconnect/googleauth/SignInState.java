package com.example.denvervolunteerconnect.googleauth;

public class SignInState {
    private Boolean isSignInSuccessful = false;
    private String signInError = null;
    public SignInState(Boolean isSignInSuccessful, String signInError) {
        this.isSignInSuccessful = isSignInSuccessful;
        this.signInError = signInError;
    }

    public Boolean getSignInSuccessful() {
        return isSignInSuccessful;
    }

    public void setSignInSuccessful(Boolean signInSuccessful) {
        isSignInSuccessful = signInSuccessful;
    }

    public String getSignInError() {
        return signInError;
    }

    public void setSignInError(String signInError) {
        this.signInError = signInError;
    }
}
