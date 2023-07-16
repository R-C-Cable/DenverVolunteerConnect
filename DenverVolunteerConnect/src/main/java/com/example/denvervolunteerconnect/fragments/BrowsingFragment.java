package com.example.denvervolunteerconnect.fragments;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.denvervolunteerconnect.R;
import com.example.denvervolunteerconnect.ViewModels.BrowsingViewModel;
import com.example.denvervolunteerconnect.databinding.BrowsingFragmentBinding;
import com.example.denvervolunteerconnect.clients.GoogleAuthClient;
import com.example.denvervolunteerconnect.models.UserDataModel;

@MainThread
public class BrowsingFragment extends Fragment {

    private static final String TAG = BrowsingFragment.class.getSimpleName();
    private BrowsingFragmentBinding mBrowsingFragmentBinding = null;
    private BrowsingViewModel mBrowsingViewModel = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        mBrowsingViewModel = new ViewModelProvider(requireActivity()).get(BrowsingViewModel.class);
        mBrowsingViewModel.startUserDataObserver(this);
        startUserObserver();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.v(TAG, "onCreateView");
        mBrowsingFragmentBinding = BrowsingFragmentBinding.inflate(inflater, container, false);
        return mBrowsingFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.v(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

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
        mBrowsingViewModel.getLiveUserData().removeObservers(this);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.v(TAG,"onDestroyView");
        super.onDestroyView();
        mBrowsingViewModel = null;
        mBrowsingFragmentBinding = null;
    }

    private void startUserObserver() {
        mBrowsingViewModel.getLiveUserData().observe(requireActivity(), new Observer<UserDataModel>() {
            @Override
            public void onChanged(UserDataModel userData) {
                try {
                    Log.v(TAG, "ROBERT startUserObserver#onChanged");
                    TextView userNameText = mBrowsingFragmentBinding.userNameText;
                    userNameText.setText(userData.getUserName());
                } catch (Exception e) {
                    Log.e(TAG, "Failed to update UI with user name");
                    e.printStackTrace();
                }
            }
        });
    }

}