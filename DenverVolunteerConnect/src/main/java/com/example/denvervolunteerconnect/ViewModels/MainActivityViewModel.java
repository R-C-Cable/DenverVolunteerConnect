package com.example.denvervolunteerconnect.ViewModels;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.denvervolunteerconnect.clients.FirebaseDatabaseClient;
import com.example.denvervolunteerconnect.models.RequestModel;
import com.example.denvervolunteerconnect.models.UserDataModel;

import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel {
    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private RequestModel requestModel = new RequestModel();
    private UserDataModel userData = new UserDataModel();
    private MutableLiveData<UserDataModel> _liveUserData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<RequestModel>> _liveRequestList = new MutableLiveData<>();

    public UserDataModel getUserData() {
        return userData;
    }

    public RequestModel getCurrentRequestModel() {
        return requestModel;
    }

    public void resetCurrentRequestModel() {
        this.requestModel = new RequestModel();
    }

    public void setCurrentRequestModel(RequestModel requestModel) {
        this.requestModel = requestModel;
    }

    public LiveData<UserDataModel> getLiveUserData() {
        return _liveUserData;
    }

    public LiveData<ArrayList<RequestModel>> getLiveRequestList() {
        return _liveRequestList;
    }

    public boolean isOwnerOfRequest() {
        try {
            return userData.getUserId().equals(requestModel.getRequesterId());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasVolunteeredCurrentRequest() {
        try {
            return userData.getUserId().equals(requestModel.getVolunteerId());
        } catch (Exception e) {
            return false;
        }
    }

    public void startUserDataObserver(LifecycleOwner lifecycleOwner) {
        Log.v(TAG, "Start User Data Observer");
        LiveData<UserDataModel> firebaseDatabaseClientUserData
                = FirebaseDatabaseClient.getInstance().liveUserData();
        userData = firebaseDatabaseClientUserData.getValue();
        firebaseDatabaseClientUserData.observe(
                lifecycleOwner,
                new Observer<UserDataModel>() {
                    @Override
                    public void onChanged(UserDataModel userDataModel) {
                        _liveUserData.postValue(userDataModel);
                        userData = userDataModel;
                    }
                });
    }

    public void startRequestListObserver(LifecycleOwner lifecycleOwner) {
        Log.v(TAG, "Starting Request List Observer");
        LiveData<ArrayList<RequestModel>> firebaseDatabaseClientRequestList
                = FirebaseDatabaseClient.getInstance().liveRequestList();
        firebaseDatabaseClientRequestList.observe(
                lifecycleOwner, new Observer<ArrayList<RequestModel>>() {
                    @Override
                    public void onChanged(ArrayList<RequestModel> requestModels) {
                        _liveRequestList.postValue(requestModels);
                    }
                });

    }

    public void addVolunteerToRequest(){
        FirebaseDatabaseClient.getInstance().postVolunteerToRequest(requestModel);
    }


    public void uploadVolunteerRequest(String title, String location, String description, String requesterId) {
        FirebaseDatabaseClient.getInstance()
                .postVolunteerRequest(new RequestModel(title, location, description, requesterId));
    }

    public void updateVolunteerRequest(String title, String location, String description) {
        requestModel.setTitle(title);
        requestModel.setLocation(location);
        requestModel.setDescription(description);
        FirebaseDatabaseClient.getInstance().updateVolunteerRequest(requestModel);

    }

    public void deleteVolunteerRequest(RequestModel requestModel) {
        FirebaseDatabaseClient.getInstance().deleteVolunteerRequest(requestModel);
    }
}
