package com.example.denvervolunteerconnect.ViewModels;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.denvervolunteerconnect.clients.FirebaseDatabaseClient;
import com.example.denvervolunteerconnect.models.RequestModel;
import com.example.denvervolunteerconnect.models.UserDataModel;

public class RequestManagementViewModel extends ViewModel {
    private static final String TAG = RequestManagementViewModel.class.getSimpleName();
    private RequestModel requestModel = new RequestModel();
    private UserDataModel userData = new UserDataModel();
    private MutableLiveData<UserDataModel> _liveUserData = new MutableLiveData<>();

    public UserDataModel getUserData() {
        return userData;
    }

    public RequestModel getRequestModel() {
        return requestModel;
    }

    public void resetRequestModel() {
        this.requestModel = new RequestModel();
    }

    public LiveData<UserDataModel> getLiveUserData() {
        return _liveUserData;
    }

    public void startUserDataObserver(LifecycleOwner lifecycleOwner) {
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


    public void uploadVolunteerRequest(String title, String location, String description, String requesterId) {
        FirebaseDatabaseClient.getInstance()
                .postVolunteerRequest(new RequestModel(title, location, description, requesterId));
    }
}
