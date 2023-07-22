package com.example.denvervolunteerconnect.clients;

import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.denvervolunteerconnect.models.RequestModel;
import com.example.denvervolunteerconnect.models.UserDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.Constants;
import utils.Utils;

@AnyThread
public class FirebaseDatabaseClient {
    private static final String TAG = FirebaseDatabaseClient.class.getSimpleName();
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static volatile FirebaseDatabaseClient INSTANCE = null;

    private MutableLiveData<UserDataModel> _liveUserData = new MutableLiveData<>();
    private DatabaseReference mRootDatabaseReference = null;
    private DatabaseReference requestEndPointReference = null;
    private DatabaseReference userDataEndPointReference = null;

    private FirebaseDatabaseClient() {
        mRootDatabaseReference = FirebaseDatabase.getInstance().getReference();
        requestEndPointReference = mRootDatabaseReference.child("Requests");
        userDataEndPointReference = mRootDatabaseReference.child("UserData");
    }

    /**
     * This a part of the Singleton pattern used to limit the number of instances of this class one.
     *
     * @return the instance of this class.
     */
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

    /**
     * Sets the Singleton instance of this class to null, and any other clean up logic.
     */
    public static void cleanUp() {
        INSTANCE = null;
    }

    public LiveData<UserDataModel> liveUserData() {
        return _liveUserData;
    }

    public void postUserData(FirebaseUser firebaseUser) {
        executorService.submit(() -> {
            UserDataModel userDataResult = Utils.convertFirebaseUserToUserDataModel(firebaseUser);

            if (!userDataResult.getUserId().equals(Constants.Strings.RESULT_FAILED)) {
                // TODO: check if user exists before adding them.
                userDataEndPointReference
                        .child(userDataResult.getUserId())
                        .setValue(userDataResult)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    _liveUserData.postValue(userDataResult);
                                }
                            }
                        });

            } else {
                Log.e(TAG, "Failed to post user data to FirebaseDatabase");
            }
        });
    }

    public void setCurrentUser(FirebaseUser firebaseUser) {
        executorService.execute(() -> {
            UserDataModel userDataResult
                    = Utils.convertFirebaseUserToUserDataModel(firebaseUser);
        });
    }

    public void postVolunteerRequest(RequestModel requestModel) {
        requestModel.setUniqueId(Instant.now().toEpochMilli() + new Random().nextLong());
        Log.v(TAG, requestModel.toString());
        executorService.submit(() -> {
            Log.v(TAG, requestModel.toString());
            requestEndPointReference
                    .child(String.valueOf(requestModel.getUniqueId()))
                    .setValue(requestModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

        });
    }


}
