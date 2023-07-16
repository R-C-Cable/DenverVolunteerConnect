package com.example.denvervolunteerconnect.ViewModels;

import androidx.annotation.MainThread;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.denvervolunteerconnect.clients.FirebaseDatabaseClient;
import com.example.denvervolunteerconnect.models.UserDataModel;

@MainThread
public class BrowsingViewModel extends ViewModel {
    private static final String TAG = BrowsingViewModel.class.getSimpleName();
    private MutableLiveData<UserDataModel> _liveUserData = new MutableLiveData<>();

    public LiveData<UserDataModel> getLiveUserData() {
        return _liveUserData;
    }

    public void startUserDataObserver(LifecycleOwner lifecycleOwner) {
        FirebaseDatabaseClient.getInstance().onLiveUserDataUpdate().observe(
                lifecycleOwner,
                new Observer<UserDataModel>() {
            @Override
            public void onChanged(UserDataModel userDataModel) {
                _liveUserData.postValue(userDataModel);
            }
        });
    }
}