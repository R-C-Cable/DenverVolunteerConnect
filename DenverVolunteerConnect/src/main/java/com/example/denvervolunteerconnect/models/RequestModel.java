package com.example.denvervolunteerconnect.models;

import androidx.annotation.NonNull;

import java.util.Objects;

import utils.Constants;

/**
 * RequestModel model
 */
public class RequestModel {
    private long uniqueId = Constants.Integers.RESULT_EMPTY;
    private String title = Constants.Strings.RESULT_EMPTY;
    private String location = Constants.Strings.RESULT_EMPTY;
    private String description = Constants.Strings.RESULT_EMPTY;
    private String requesterId = Constants.Strings.RESULT_EMPTY;
    private String volunteerId = Constants.Strings.RESULT_EMPTY;


    public RequestModel() {
    }

    public RequestModel(
            String title,
            String location,
            String description,
            String requesterId
    ) {
        this.title = title;
        this.location = location;
        this.description = description;
        this.requesterId = requesterId;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public boolean requesterIdIsDefault() {
        return requesterId.equals(Constants.Strings.RESULT_EMPTY);
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(String volunteerId) {
        this.volunteerId = volunteerId;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "requestId='" + uniqueId + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", RequesterId='" + requesterId + '\'' +
                ", volunteerId='" + volunteerId + '\'' +
                '}';
    }
}
