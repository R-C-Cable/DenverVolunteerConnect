package com.example.denvervolunteerconnect.googleauth;

public class UserData {
    private String userId = null;
    private String userName = null;
    private String profilePictureUrl = null;

    public UserData(String userId, String userName, String profilePictureUrl) {
        this.userId = userId;
        this.userName = userName;
        this.profilePictureUrl = profilePictureUrl;
    }
}
