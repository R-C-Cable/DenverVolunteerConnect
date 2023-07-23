package com.example.denvervolunteerconnect.clients;

import android.util.Log;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.denvervolunteerconnect.models.RequestModel;
import com.example.denvervolunteerconnect.models.UserDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import utils.Constants;
import utils.Utils;

@AnyThread
public class FirebaseDatabaseClient {
    private static final String TAG = FirebaseDatabaseClient.class.getSimpleName();
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static volatile FirebaseDatabaseClient INSTANCE = null;

    private MutableLiveData<ArrayList<RequestModel>> _requestList = new MutableLiveData<>(new ArrayList<>());

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

    public LiveData<ArrayList<RequestModel>> liveRequestList() {
        return _requestList;
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

    public void postVolunteerRequest(RequestModel requestModel) {
        if (requestModel.isUniqueIdDefault()) {
            requestModel.setUniqueId(String.valueOf(Instant.now().toEpochMilli() + new Random().nextLong()));
        }
        executorService.submit(() -> {
            try {
                requestEndPointReference
                        .child(String.valueOf(requestModel.getUniqueId()))
                        .setValue(requestModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateVolunteerRequest(RequestModel requestModel) {
        executorService.submit(() -> {
            try {
                DatabaseReference childReference = requestEndPointReference.child(requestModel.getUniqueId());
                Map<String, Object> newRequestData = new HashMap<>();
                newRequestData.put("description", requestModel.getDescription());
                newRequestData.put("location", requestModel.getLocation());
                newRequestData.put("title", requestModel.getTitle());
                childReference.updateChildren(newRequestData);
            } catch (Exception e) {
                Log.e(TAG, "Failed to update volunteer request: ");
                e.printStackTrace();
            }

        });;
    }

    public void postVolunteerToRequest(RequestModel requestModel) {
        executorService.submit(() -> {
            try {
                DatabaseReference childReference = requestEndPointReference.child(requestModel.getUniqueId());
                Map<String, Object> newRequestData = new HashMap<>();
                UserDataModel userData = _liveUserData.getValue();
                if (userData != null) {
                    newRequestData.put("volunteerId", userData.getUserId());
                    childReference.updateChildren(newRequestData);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public void deleteVolunteerRequest(RequestModel requestModel) {
        executorService.submit(() -> {
            try {
                DatabaseReference childReference = requestEndPointReference.child(requestModel.getUniqueId());
                childReference.setValue(null);
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public void startVolunteerRequestListInitialUpdate(){
        executorService.submit(() -> {
            ArrayList<RequestModel> localRequestList = new ArrayList<>();
            requestEndPointReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                        @Override
                        public void accept(DataSnapshot request) {
                            localRequestList.add(request.getValue(RequestModel.class));
                        }
                    });
                    _requestList.postValue(localRequestList);
                    startVolunteerRequestListObserver();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG,"The read failed: " + databaseError.getCode());
                }
            });

        });
    }


    public void startVolunteerRequestListObserver() {
        requestEndPointReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                executorService.execute(() -> {
                    synchronized (FirebaseDatabaseClient.class) {
                        try {
                            RequestModel requestModel = snapshot.getValue(RequestModel.class);
                            if (requestModel == null) {
                                Log.e(TAG, "attempting to add null RequestModel");
                            } else if (_requestList.getValue() == null || _requestList.getValue().isEmpty()) {
                                final ArrayList<RequestModel> newRequestList = new ArrayList<>();
                                newRequestList.add(requestModel);
                                Log.v(TAG, "Initial setup of requestList: " + newRequestList);
                                _requestList.postValue(newRequestList);
                            } else if (!_requestList.getValue().contains(requestModel)) {
                                final ArrayList<RequestModel> newRequestList = new ArrayList<>(_requestList.getValue());
                                newRequestList.add(requestModel);
                                Log.v(TAG, "adding request to list: " + newRequestList);
                                _requestList.postValue(newRequestList);
                            } else {
                                Log.v(TAG, "Requests are not new from database.");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Failed to get request list from firebase");
                            e.printStackTrace();
                        }
                    }

                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });
    }
}
