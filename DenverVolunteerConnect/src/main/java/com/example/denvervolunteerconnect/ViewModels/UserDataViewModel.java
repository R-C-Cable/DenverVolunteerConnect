package com.example.denvervolunteerconnect.ViewModels;

import android.content.Context;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.denvervolunteerconnect.googleauth.GoogleAuthClient;
import com.example.denvervolunteerconnect.googleauth.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel to handle updating userdata based on UI events.
 */
@MainThread
public class UserDataViewModel extends ViewModel {
    private static final String TAG = UserDataViewModel.class.getSimpleName();
    private static final ExecutorService userDataExecutorService = Executors.newSingleThreadExecutor();
    private MutableLiveData<UserData> liveUserData = new MutableLiveData<>();

    public LiveData<UserData> getLiveUserData() {
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
                UserData result = new UserData();


                if (firebaseUser != null) {
                    if (firebaseUser.getDisplayName() != null) {
                        result.setUserName(firebaseUser.getDisplayName());
                    } else {
                        logUser = true;
                    }
                    if (firebaseUser.getPhotoUrl() != null) {
                        result.setProfilePictureUrl(firebaseUser.getPhotoUrl().toString());
                    } else {
                        logUser = true;
                    }
                    result.setUserId(firebaseUser.getUid());
                    liveUserData.postValue(result);
                    getLiveUserData().notifyAll();
                } else {
                    Log.e(TAG, "Failed to update liveUserData due to null firebaseUser.");
                    return;
                }
                if (logUser)
                    Log.i(TAG, "Unable to populate all values from user: " + result);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}