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
import androidx.recyclerview.widget.RecyclerView;

import com.example.denvervolunteerconnect.R;
import com.example.denvervolunteerconnect.ViewModels.MainActivityViewModel;
import com.example.denvervolunteerconnect.databinding.BrowsingBinding;
import com.example.denvervolunteerconnect.models.RequestModel;
import com.example.denvervolunteerconnect.models.UserDataModel;
import com.example.denvervolunteerconnect.recyclerview.RequestRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

@MainThread
public class BrowsingFragment extends Fragment {

    private static final String TAG = BrowsingFragment.class.getSimpleName();
    private BrowsingBinding mBrowsingFragmentBinding = null;
    private MainActivityViewModel mMainActivityViewModel = null;
    private RecyclerView requestRecyclerView = null;
    private RequestRecyclerViewAdapter requestRecyclerViewAdapter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        mMainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        mMainActivityViewModel.startUserDataObserver(getActivity());
        mMainActivityViewModel.startRequestListObserver(getActivity());


    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.v(TAG, "onCreateView");
        mBrowsingFragmentBinding = BrowsingBinding.inflate(inflater, container, false);
        requestRecyclerView = mBrowsingFragmentBinding.volunteerRequestList;
        requestRecyclerView.setHasFixedSize(false);
        requestRecyclerViewAdapter = new RequestRecyclerViewAdapter(getContext(), mMainActivityViewModel);
        requestRecyclerView.setAdapter(requestRecyclerViewAdapter);

        requestRecyclerViewAdapter.setOnClickListener(new RequestRecyclerViewAdapter.OnClickListener() {
            @Override
            public void onClick(RequestModel requestModel) {
                mMainActivityViewModel.setCurrentRequestModel(requestModel);
                NavHostFragment.findNavController(BrowsingFragment.this)
                        .navigate(R.id.start_create_request_flow);
            }
        });
        return mBrowsingFragmentBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.v(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        this.startUserObserver();
        this.startRequestListObserver();

        mBrowsingFragmentBinding.createRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainActivityViewModel.resetCurrentRequestModel();
                NavHostFragment.findNavController(BrowsingFragment.this)
                        .navigate(R.id.start_create_request_flow);
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
        Log.v(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.v(TAG,"onDestroyView");
        super.onDestroyView();
        mMainActivityViewModel = null;
        mBrowsingFragmentBinding = null;
    }

    private void startUserObserver() {
        mMainActivityViewModel.getLiveUserData().observe(getViewLifecycleOwner(), new Observer<UserDataModel>() {
            @Override
            public void onChanged(UserDataModel userData) {
                try {
                    TextView userNameText = mBrowsingFragmentBinding.userNameText;
                    userNameText.setText(userData.getUserName());
                } catch (Exception e) {
                    Log.e(TAG, "Failed to update UI with user name");
                    e.printStackTrace();
                }
            }
        });
    }

    private void startRequestListObserver() {
        mMainActivityViewModel.getLiveRequestList().observe(getViewLifecycleOwner(), new Observer<ArrayList<RequestModel>>() {
            @Override
            public void onChanged(ArrayList<RequestModel> requestModels) {
                Log.e("ROBERT Fragment", requestModels.toString());
                requestRecyclerViewAdapter.updateList(requestModels);
            }
        });
    }


}