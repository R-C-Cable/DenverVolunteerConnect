package com.example.denvervolunteerconnect.Fragments;

import com.example.denvervolunteerconnect.Models.RequestModel;

public interface RequestViewStatesInterface {
    enum CurrentState {
        notInitialized,
        createMode,
        reviewMode,
        nonOwnerReviewMode,
        ownerReviewMode,
        editMode
    }

    void initialize();
    void enterCreateMode();
    void enterReviewMode();
    void enterOwnerReviewMode();
    void enterNotOwnerReviewMode();
    void enterEditMode();
    CurrentState getState();
}