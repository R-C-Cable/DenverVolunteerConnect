package com.example.denvervolunteerconnect.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.denvervolunteerconnect.R;
import com.example.denvervolunteerconnect.ViewModels.MainActivityViewModel;
import com.example.denvervolunteerconnect.databinding.RequestManagementBinding;
import com.example.denvervolunteerconnect.Models.RequestModel;

@MainThread
public class RequestManagementFragment extends BaseFragment {

    private static final String TAG = RequestManagementFragment.class.getSimpleName();
    private MainActivityViewModel mMainActivityViewModel = null;
    private RequestManagementBinding editRequestFragmentBinding = null;
    RequestViewState requestViewState = new RequestViewState();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
    }

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
        requestViewState.initialize();
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

    /**
     * Class to control and monitor the state of the RequestManagementFragment
     */
    private class RequestViewState implements RequestViewStatesInterface {
        private CurrentState currentState = CurrentState.notInitialized;

        @Override
        public void initialize() {
            if (mMainActivityViewModel
                    .getCurrentRequestModel()
                    .isUniqueIdDefault()) {
                // Default Value equals create request flow.
                requestViewState.enterCreateMode();
            } else {
                // Not Default Values equals reviewing flow.
                requestViewState.enterReviewMode();
            }
        }

        @Override
        public void enterCreateMode() {
            currentState = CurrentState.createMode;
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
                                .navigate(R.id.RequestManagment_to_BrowsingActivity);
                        Toast.makeText(getActivity(), "Request Submitted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void enterReviewMode() {
            currentState = CurrentState.reviewMode;
            Log.i(TAG, "Starting Request Review Flow");
            RequestModel requestModel = mMainActivityViewModel.getCurrentRequestModel();

            editRequestFragmentBinding.titleEditText.setText(requestModel.getTitle());
            editRequestFragmentBinding.locationEditText.setText(requestModel.getLocation());
            editRequestFragmentBinding.descriptionEditText.setText(requestModel.getDescription());
            textFieldsEditable(false);

            if (mMainActivityViewModel.isOwnerOfRequest()) {
                requestViewState.enterOwnerReviewMode();
            } else {
                requestViewState.enterNotOwnerReviewMode();
            }
        }

        @Override
        public void enterNotOwnerReviewMode() {
            currentState = CurrentState.nonOwnerReviewMode;
            Log.i(TAG, "Staring non-owner, or volunteer review flow");
            editRequestFragmentBinding.editButton.setVisibility(View.INVISIBLE);
            editRequestFragmentBinding.deleteButton.setVisibility(View.INVISIBLE);
            editRequestFragmentBinding.requestActionButton.setText("Volunteer");
            if (mMainActivityViewModel.hasVolunteeredCurrentRequest()) {
                editRequestFragmentBinding.requestActionButton.setEnabled(false);
            } else {
                editRequestFragmentBinding.requestActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fieldsNotBlank()) {
                            // Logic to upload request
                            Toast.makeText(getActivity(), "Thanks You For Volunteering!", Toast.LENGTH_SHORT).show();
                            mMainActivityViewModel.addVolunteerToRequest();
                            NavHostFragment.findNavController(RequestManagementFragment.this)
                                    .navigate(R.id.RequestManagment_to_BrowsingActivity);
                        }
                    }
                });
            }
        }

        @Override
        public void enterOwnerReviewMode() {
            currentState = CurrentState.ownerReviewMode;
            Log.i(TAG, "Starting Owner Review Flow");
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
                        mMainActivityViewModel.deleteVolunteerRequest(mMainActivityViewModel.getCurrentRequestModel());
                        Toast.makeText(getActivity(), "Deleting Volunteer Request", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(RequestManagementFragment.this)
                                .navigate(R.id.RequestManagment_to_BrowsingActivity);
                    }
                }
            });
            editRequestFragmentBinding.requestActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fieldsNotBlank()) {
                        // Logic to upload request
                        Toast.makeText(getActivity(), "Updating Volunteer Request", Toast.LENGTH_SHORT).show();
                        mMainActivityViewModel.updateVolunteerRequest(
                                editRequestFragmentBinding.titleEditText.getText().toString(),
                                editRequestFragmentBinding.locationEditText.getText().toString(),
                                editRequestFragmentBinding.descriptionEditText.getText().toString());
                        NavHostFragment.findNavController(RequestManagementFragment.this)
                                .navigate(R.id.RequestManagment_to_BrowsingActivity);
                    }
                }
            });
            editRequestFragmentBinding.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterEditMode();
                }
            });

        }

        @Override
        public void enterEditMode() {
            currentState = CurrentState.editMode;
            if (fieldsNotBlank()) {
                editRequestFragmentBinding.requestActionButton.setEnabled(true);
                editRequestFragmentBinding.deleteButton.setEnabled(true);
                editRequestFragmentBinding.editButton.setEnabled(false);
                textFieldsEditable(true);
            }
        }

        private void textFieldsEditable(boolean editable) {
            editRequestFragmentBinding.titleEditText.setEnabled(editable);
            editRequestFragmentBinding.locationEditText.setEnabled(editable);
            editRequestFragmentBinding.descriptionEditText.setEnabled(editable);
        }

        @Override
        public CurrentState getState() {
            return currentState;
        }
    }
}