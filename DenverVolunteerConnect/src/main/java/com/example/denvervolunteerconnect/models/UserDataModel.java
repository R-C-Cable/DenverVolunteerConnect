package com.example.denvervolunteerconnect.models;

import utils.Constants;

public class UserDataModel {
    private String userId;
    private String userName;
    private String profilePictureUrl;

    public UserDataModel(String firebaseKey, String userId, String userName, String profilePictureUrl) {
        this.userId = userId;
        this.userName = userName;
        this.profilePictureUrl = profilePictureUrl;
    }
    public UserDataModel() {
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
        return "UserDataModel{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                '}';
    }
}
