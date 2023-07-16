package com.example.denvervolunteerconnect.ViewModels;

import android.content.Context;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.denvervolunteerconnect.clients.FirebaseDatabaseClient;
import com.example.denvervolunteerconnect.clients.GoogleAuthClient;
import com.example.denvervolunteerconnect.models.UserDataModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel to handle updating userdata based on UI events.
 * NOTE: This is not names after a Activity or Fragment because it should be shared between fragments and MainActivity.
 */
@MainThread
public class EditRequestViewModel extends ViewModel {
    private static final String TAG = EditRequestViewModel.class.getSimpleName();
    private static final ExecutorService userDataExecutorService = Executors.newSingleThreadExecutor();
    private MutableLiveData<UserDataModel> liveUserData = new MutableLiveData<>();

    public LiveData<UserDataModel> getLiveUserData() {
        return liveUserData;
    }

    //TODO: add observer of the database to update the UI if the database changes.


    /**
     * Will start the process of updating the liveUserData.
     * Observe liveUserData to get the return value.
     *
     * @param context of the calling activity or fragment.
     */
    public void startUserDataUpdate(Context context) {
        Log.v(TAG, "startUserDataUpdate");
        try {
            userDataExecutorService.submit(() -> {
                Boolean logUser = false;
                FirebaseUser firebaseUser =
                        GoogleAuthClient.getInstance(
                                context.getApplicationContext()).getSignedInFirebaseUser();
                UserDataModel userDataResult = new UserDataModel();


                if (firebaseUser != null) {
                    if (firebaseUser.getDisplayName() != null) {
                        userDataResult.setUserName(firebaseUser.getDisplayName());
                    } else {
                        logUser = true;
                    }
                    if (firebaseUser.getPhotoUrl() != null) {
                        userDataResult.setProfilePictureUrl(firebaseUser.getPhotoUrl().toString());
                    } else {
                        logUser = true;
                    }
                    userDataResult.setUserId(firebaseUser.getUid());
                    liveUserData.postValue(userDataResult);

                    // TODO: REMOVE THIS TESTING LOGIC
                    Log.e(TAG, "postUserData()");
                    FirebaseDatabaseClient.getInstance().postUserData(userDataResult);
                    // END TEST LOGIC

                } else {
                    Log.e(TAG, "Failed to update liveUserData due to null firebaseUser.");
                    return;
                }
                if (logUser)
                    Log.i(TAG, "Unable to populate all values from user: " + userDataResult);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}