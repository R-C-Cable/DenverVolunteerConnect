package utils;

import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.WorkerThread;

import com.example.denvervolunteerconnect.models.UserDataModel;
import com.google.firebase.auth.FirebaseUser;

@AnyThread
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    @WorkerThread
    public static UserDataModel convertFirebaseUserToUserDataModel(FirebaseUser firebaseUser) {
        UserDataModel userDataResult = new UserDataModel();
        boolean logUser = false;

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
        } else {
            Log.e(TAG, "Failed to Firebase user to UserDataModel due to null firebaseUser.");
        }
        if (logUser) {
            Log.i(TAG, "Unable to populate all values from user: " + userDataResult);
        }
        return userDataResult;
    }
}
