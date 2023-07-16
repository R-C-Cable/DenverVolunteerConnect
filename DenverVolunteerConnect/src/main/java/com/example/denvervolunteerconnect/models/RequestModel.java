package com.example.denvervolunteerconnect.models;

import utils.Constants;

/**
 * RequestModel model
 */
public class RequestModel {
    private String uniqueId;
    private String title;
    private String location;
    private String description;
    private String requesterId;
    private String volunteerId;


    public RequestModel() {
        this.uniqueId = Constants.Strings.RESULT_FAILED;
        this.title = Constants.Strings.RESULT_FAILED;
        this.location = Constants.Strings.RESULT_FAILED;
        this.description = Constants.Strings.RESULT_FAILED;
    }

    public RequestModel(
            String uniqueId,
            String title,
            String location,
            String description,
            String requesterId,
            String volunteerId
    ) {
        this.uniqueId = uniqueId;
        this.title = title;
        this.location = location;
        this.description = description;
        this.requesterId = requesterId;
        this.volunteerId = volunteerId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
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
