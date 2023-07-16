package com.example.denvervolunteerconnect.clients;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.denvervolunteerconnect.models.UserDataModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseClient {
    private static final String TAG = FirebaseDatabaseClient.class.getSimpleName();
    private static volatile FirebaseDatabaseClient INSTANCE = null;
    private DatabaseReference mRootDatabaseReference = null;
    private DatabaseReference requestEndPointReference = null;
    private DatabaseReference userDataEndPointReference = null;

    public FirebaseDatabaseClient() {
        mRootDatabaseReference = FirebaseDatabase.getInstance().getReference();
        requestEndPointReference = mRootDatabaseReference.child("Requests");
        userDataEndPointReference = mRootDatabaseReference.child("UserData");
    }

    @NonNull
    public static FirebaseDatabaseClient getInstance() {
        if (INSTANCE == null) {
            synchronized (FirebaseDatabaseClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FirebaseDatabaseClient();
                }
            }
        }
        return INSTANCE;
    }

    public static void cleanUp() {
        INSTANCE = null;
    }

    public void postUserData(UserDataModel userData) {
        // TODO: check if user exists before adding them.
        userDataEndPointReference
                .child(userData.getUserId())
                .setValue(userData);
    }



}
