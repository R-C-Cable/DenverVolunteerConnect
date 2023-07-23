package com.example.denvervolunteerconnect.models;

import utils.Constants;

/**
 * RequestModel model
 */
public class RequestModel {
    private String uniqueId = Constants.Strings.RESULT_EMPTY;
    private String title = Constants.Strings.RESULT_EMPTY;
    private String location = Constants.Strings.RESULT_EMPTY;
    private String description = Constants.Strings.RESULT_EMPTY;
    private String requesterId = Constants.Strings.RESULT_EMPTY;
    private String volunteerId = Constants.Strings.RESULT_EMPTY;


    public RequestModel() {}

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

    public String getUniqueId() {
        return uniqueId;
    }

    public boolean isUniqueIdDefault() {
        return (uniqueId.equals(Constants.Strings.RESULT_EMPTY));
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

    public boolean isRequesterIdDefault() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestModel that = (RequestModel) o;
        return uniqueId == that.uniqueId;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "uniqueId=" + uniqueId +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", requesterId='" + requesterId + '\'' +
                ", volunteerId='" + volunteerId + '\'' +
                '}';
    }
}
