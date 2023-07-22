package com.example.denvervolunteerconnect.recyclerview;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.denvervolunteerconnect.databinding.VolunteerRequestListItemBinding;
import com.example.denvervolunteerconnect.models.RequestModel;

import java.util.Collections;
import java.util.List;

public class VolunteerRequestRecyclerViewAdapter extends RecyclerView.Adapter<VolunteerRequestRecyclerViewAdapter.RequestItemViewHolder> {
    List<RequestModel> volunteerRequestList = Collections.emptyList();
    Context context = null;

    public VolunteerRequestRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    private void createMockData() {
        volunteerRequestList.add(new RequestModel());
        volunteerRequestList.add(new RequestModel());
        volunteerRequestList.add(new RequestModel());
        volunteerRequestList.add(new RequestModel());
        volunteerRequestList.add(new RequestModel());

    }

    @NonNull
    @Override
    public RequestItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VolunteerRequestListItemBinding viewBinding = VolunteerRequestListItemBinding
                .inflate(LayoutInflater.from(
                                parent.getContext()), parent, false);
        return new RequestItemViewHolder(viewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestItemViewHolder holder, int position) {
        holder.onBind(volunteerRequestList.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RequestItemViewHolder extends RecyclerView.ViewHolder {
        private VolunteerRequestListItemBinding viewBinding;
        public RequestItemViewHolder(@NonNull VolunteerRequestListItemBinding volunteerRequestListItemBinding) {
            super(volunteerRequestListItemBinding.getRoot());
        }

        public void onBind(RequestModel requestModel) {
            viewBinding.titleText.setText(requestModel.getTitle());
        }
    }
}