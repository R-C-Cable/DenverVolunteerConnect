package com.example.denvervolunteerconnect.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.denvervolunteerconnect.MainActivity;
import com.example.denvervolunteerconnect.R;
import com.example.denvervolunteerconnect.ViewModels.UserDataViewModel;
import com.example.denvervolunteerconnect.databinding.BrowsingFragmentBinding;
import com.example.denvervolunteerconnect.googleauth.GoogleAuthClient;
import com.example.denvervolunteerconnect.googleauth.UserData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.concurrent.Executors;

@MainThread
public class BrowsingFragment extends Fragment {

    private static final String TAG = BrowsingFragment.class.getSimpleName();
    private BrowsingFragmentBinding mBrowsingFragmentBinding = null;
    private UserDataViewModel mUserDataViewModel = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        mUserDataViewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        mUserDataViewModel.startUserDataUpdate(requireActivity().getApplicationContext());
        setupUserDataObserver();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        mBrowsingFragmentBinding = BrowsingFragmentBinding.inflate(inflater, container, false);
        return mBrowsingFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBrowsingFragmentBinding.userNameText.setText("UserName will go here.");

        mBrowsingFragmentBinding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(BrowsingFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        mBrowsingFragmentBinding.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAuthClient googleAuthClient = GoogleAuthClient.getInstance(getContext());
            }
        });
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.v(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUserDataViewModel.getLiveUserData().removeObservers(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUserDataViewModel = null;
        mBrowsingFragmentBinding = null;
    }

    private void setupUserDataObserver() {
        Log.v(TAG, "setupUserDataObserver");
        mUserDataViewModel.getLiveUserData().observe(requireActivity(), new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {
                Log.v(TAG, "getLiveUserData#onChanged()");
                TextView userNameText = mBrowsingFragmentBinding.userNameText;
                userNameText.setText(userData.getUserName());
            }
        });
    }

}