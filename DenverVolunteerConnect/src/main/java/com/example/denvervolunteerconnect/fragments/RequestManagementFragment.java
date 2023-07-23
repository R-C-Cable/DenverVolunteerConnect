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
import com.example.denvervolunteerconnect.ViewModels.MainActivityViewModel;
import com.example.denvervolunteerconnect.databinding.RequestManagementBinding;
import com.example.denvervolunteerconnect.models.RequestModel;

@MainThread
public class RequestManagementFragment extends Fragment {

    private static final String TAG = RequestManagementFragment.class.getSimpleName();
    private MainActivityViewModel mMainActivityViewModel = null;
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
        mMainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        mMainActivityViewModel.startUserDataObserver(this);
        setupBasedOnUserFlows();
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
        mMainActivityViewModel.resetCurrentRequestModel();
        editRequestFragmentBinding = null;
    }

    private void setupBasedOnUserFlows() {
        RequestModel requestModel = mMainActivityViewModel.getCurrentRequestModel();
        String userId = mMainActivityViewModel.getUserData().getUserId();
        // Default Value equals create request flow.
        if (requestModel.isUniqueIdDefault()) {
            Log.i(TAG, "Starting Request Creation Flow");
            editRequestFragmentBinding.requestActionButton.setText(R.string.accept_button_text);
            editRequestFragmentBinding.requestActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fieldsNotBlank()) {
                        mMainActivityViewModel.uploadVolunteerRequest(
                                editRequestFragmentBinding.titleEditText.getText().toString(),
                                editRequestFragmentBinding.locationEditText.getText().toString(),
                                editRequestFragmentBinding.descriptionEditText.getText().toString(),
                                mMainActivityViewModel.getUserData().getUserId()
                        );
                        NavHostFragment.findNavController(RequestManagementFragment.this)
                                .navigate(R.id.action_SecondFragment_to_FirstFragment);
                        Toast.makeText(getActivity(), "Request Submitted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            // Not Default Values equals reviewing flow.
        } else {
            setupReviewMode(requestModel);
            if (mMainActivityViewModel.isOwnerOfRequest()) {
                setupReviewModeForOwner();
            } else {
                Log.i(TAG, "Staring non-owner review flow");
                editRequestFragmentBinding.editButton.setVisibility(View.INVISIBLE);
                editRequestFragmentBinding.deleteButton.setVisibility(View.INVISIBLE);
                editRequestFragmentBinding.requestActionButton.setText("Volunteer");

            }
        }
    }

    private void setupReviewMode(RequestModel requestModel) {
        editRequestFragmentBinding.titleEditText.setText(requestModel.getTitle());
        editRequestFragmentBinding.locationEditText.setText(requestModel.getLocation());
        editRequestFragmentBinding.descriptionEditText.setText(requestModel.getDescription());
        textFieldsEditable(false);
    }

    private void setupReviewModeForOwner() {
        editRequestFragmentBinding.editButton.setVisibility(View.VISIBLE);
        editRequestFragmentBinding.deleteButton.setVisibility(View.VISIBLE);
        editRequestFragmentBinding.requestActionButton.setEnabled(false);
        editRequestFragmentBinding.deleteButton.setEnabled(false);
        editRequestFragmentBinding.requestActionButton.setText("Update Request");

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
                    editRequestFragmentBinding.requestActionButton.setEnabled(true);
                    editRequestFragmentBinding.deleteButton.setEnabled(true);
                    textFieldsEditable(true);
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

    private void textFieldsEditable(boolean editable) {
        editRequestFragmentBinding.titleEditText.setEnabled(editable);
        editRequestFragmentBinding.locationEditText.setEnabled(editable);
        editRequestFragmentBinding.descriptionEditText.setEnabled(editable);
    }
}