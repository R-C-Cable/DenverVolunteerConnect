package com.example.denvervolunteerconnect.models;

import java.util.Objects;

import utils.Constants;

public class UserDataModel {
    private String userId = Constants.Strings.RESULT_EMPTY;
    private String userName = Constants.Strings.RESULT_EMPTY;
    private String profilePictureUrl = Constants.Strings.RESULT_EMPTY;

    public UserDataModel(String firebaseKey, String userId, String userName, String profilePictureUrl) {
        this.userId = userId;
        this.userName = userName;
        this.profilePictureUrl = profilePictureUrl;
    }

    public UserDataModel() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDataModel that = (UserDataModel) o;
        return userId.equals(that.userId);
    }
}
