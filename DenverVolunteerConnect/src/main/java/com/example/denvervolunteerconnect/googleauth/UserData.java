package com.example.denvervolunteerconnect.googleauth;

import utils.Constants;

public class UserData {
    private String userId = null;
    private String userName = null;
    private String profilePictureUrl = null;

    public UserData(String userId, String userName, String profilePictureUrl) {
        this.userId = userId;
        this.userName = userName;
        this.profilePictureUrl = profilePictureUrl;
    }

    public UserData() {
        this.userId = Constants.Strings.RESULT_FAILED;
        this.userName = Constants.Strings.RESULT_FAILED;
        this.profilePictureUrl = Constants.Strings.RESULT_FAILED;
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

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                '}';
    }
}
