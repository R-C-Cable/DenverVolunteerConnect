package com.example.denvervolunteerconnect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.denvervolunteerconnect.databinding.BrowsingFragmentBinding;

public class BrowsingFragment extends Fragment {

    private BrowsingFragmentBinding browsingFragmentBinding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        browsingFragmentBinding = BrowsingFragmentBinding.inflate(inflater, container, false);
        return browsingFragmentBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        browsingFragmentBinding.userNameText.setText("UserName will go here.");

        browsingFragmentBinding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(BrowsingFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        browsingFragmentBinding = null;
    }

}