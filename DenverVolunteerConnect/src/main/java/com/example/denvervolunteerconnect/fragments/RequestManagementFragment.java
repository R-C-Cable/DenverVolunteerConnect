package com.example.denvervolunteerconnect.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.denvervolunteerconnect.R;
import com.example.denvervolunteerconnect.ViewModels.RequestManagementViewModel;
import com.example.denvervolunteerconnect.databinding.RequestManagementBinding;
import com.example.denvervolunteerconnect.models.RequestModel;

import utils.Constants;

@MainThread
public class RequestManagementFragment extends Fragment {

    private static final String TAG = RequestManagementFragment.class.getSimpleName();
    private RequestManagementViewModel mRequestManagementViewModel = null;
    private RequestManagementBinding editRequestFragmentBinding = null;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.v(TAG, "onCreateView");
        editRequestFragmentBinding = RequestManagementBinding.inflate(inflater, container, false);
        return editRequestFragmentBinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.v(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mRequestManagementViewModel = new ViewModelProvider(requireActivity()).get(RequestManagementViewModel.class);
        mRequestManagementViewModel.startUserDataObserver(this);
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        setupButtonsBasedOnUserFlows();
    }

    @Override
    public void onStop() {
        Log.v(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.v(TAG, "onDestroyView");
        super.onDestroyView();
        mRequestManagementViewModel.resetRequestModel();
        editRequestFragmentBinding = null;
    }

    private void setupButtonsBasedOnUserFlows() {
        RequestModel requestModel = mRequestManagementViewModel.getRequestModel();
        String userId = mRequestManagementViewModel.getUserData().getUserId();
        // Default Value equals creating a requestModel flow
        if (requestModel.requesterIdIsDefault()) {
            Log.v(TAG, "Starting Request Creation Flow");
            editRequestFragmentBinding.requestActionButton.setText(R.string.accept_button_text);
            editRequestFragmentBinding.requestActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fieldsNotBlank()) {
                        mRequestManagementViewModel.uploadVolunteerRequest(
                                editRequestFragmentBinding.titleEditText.getText().toString(),
                                editRequestFragmentBinding.locationEditText.getText().toString(),
                                editRequestFragmentBinding.descriptionEditText.getText().toString(),
                                mRequestManagementViewModel.getUserData().getUserId()
                        );
                        NavHostFragment.findNavController(RequestManagementFragment.this)
                                .navigate(R.id.action_SecondFragment_to_FirstFragment);
                        Toast.makeText(getActivity(), "Request Submitted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            // Owner of requestModel
        } else if (
                userId.equals(requestModel.getRequesterId())
                && !mRequestManagementViewModel
                .getRequestModel()
                .getRequesterId()
                .equals(Constants.Strings.RESULT_EMPTY)
        ) {
            Log.v(TAG, "Staring Owner Request Review flow with requestModel: "
                    + mRequestManagementViewModel.getRequestModel().toString());
            setupForOwner();
            // Not Owner of requestModel
        } else {
            Log.v(TAG, "Staring non-owner requestModel view flow");
            editRequestFragmentBinding.editButton.setVisibility(View.INVISIBLE);
            editRequestFragmentBinding.deleteButton.setVisibility(View.INVISIBLE);
            editRequestFragmentBinding.requestActionButton.setText("Accept");

        }
    }

    private void setupForOwner() {
        editRequestFragmentBinding.editButton.setVisibility(View.VISIBLE);
        editRequestFragmentBinding.deleteButton.setVisibility(View.VISIBLE);

        editRequestFragmentBinding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldsNotBlank()) {
                    // Logic to delete request
                    Toast.makeText(getActivity(), "Deleting Request Request", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editRequestFragmentBinding.requestActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldsNotBlank()) {
                    // Logic to upload request
                    NavHostFragment.findNavController(RequestManagementFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                    Toast.makeText(getActivity(), "action button success", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editRequestFragmentBinding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldsNotBlank()) {
                    // Logic to update request
                    Toast.makeText(getActivity(), "editing request", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean fieldsNotBlank() {
        boolean result = true;
        boolean titleBlank = editRequestFragmentBinding.titleEditText.length() == 0;
        boolean locationBlank = editRequestFragmentBinding.locationEditText.length() == 0;
        boolean descriptionBlank = editRequestFragmentBinding.descriptionEditText.length() == 0;

        if (titleBlank) {
            editRequestFragmentBinding.titleEditText.setError("Title is required");
            result = false;
        }
        if (locationBlank) {
            editRequestFragmentBinding.locationEditText.setError("Location is required");
            result = false;
        }
        if (descriptionBlank) {
            editRequestFragmentBinding.descriptionEditText.setError("Description is required");
            result = false;
        }
        return result;
    }

}