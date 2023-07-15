package com.example.denvervolunteerconnect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.denvervolunteerconnect.databinding.ViewRequestFragmentBinding;

public class ViewRequestFragment extends Fragment {

    private ViewRequestFragmentBinding viewRequestFragmentBinding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        viewRequestFragmentBinding = ViewRequestFragmentBinding.inflate(inflater, container, false);
        return viewRequestFragmentBinding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewRequestFragmentBinding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ViewRequestFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewRequestFragmentBinding = null;
    }

}