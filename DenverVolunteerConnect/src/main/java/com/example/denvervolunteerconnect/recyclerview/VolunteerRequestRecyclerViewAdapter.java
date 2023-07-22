package com.example.denvervolunteerconnect.recyclerview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.denvervolunteerconnect.databinding.VolunteerRequestListItemBinding;
import com.example.denvervolunteerconnect.models.RequestModel;

import java.util.List;

public class VolunteerRequestRecyclerViewAdapter extends RecyclerView.Adapter<VolunteerRequestRecyclerViewAdapter.RequestItemViewHolder> {
    List<RequestModel> volunteerRequestList = List.of();
    Context context = null;

    public VolunteerRequestRecyclerViewAdapter(Context context) {
        this.context = context;
        Log.v("ROBERT", "VolunteerRequestRecyclerViewAdapter");
    }

    @NonNull
    @Override
    public RequestItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v("ROBERT", "onCreateViewHolder");
        VolunteerRequestListItemBinding viewBinding = VolunteerRequestListItemBinding
                .inflate(LayoutInflater.from(
                        parent.getContext()), parent, false);
        return new RequestItemViewHolder(viewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestItemViewHolder holder, int position) {
        holder.onBind(volunteerRequestList.get(position));
        Log.v("ROBERT", "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return volunteerRequestList.size();
    }

    public void updateList(List<RequestModel> volunteerRequestList) {
        this.volunteerRequestList = volunteerRequestList;
        notifyDataSetChanged();
    }

    public class RequestItemViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = RequestItemViewHolder.class.getSimpleName();
        private VolunteerRequestListItemBinding viewBinding;
        public RequestItemViewHolder(@NonNull VolunteerRequestListItemBinding volunteerRequestListItemBinding) {
            super(volunteerRequestListItemBinding.getRoot());
            viewBinding = volunteerRequestListItemBinding;
            Log.v("ROBERT", "RequestItemViewHolder");
        }

        public void onBind(RequestModel requestModel) {
            Log.v("ROBERT", requestModel.toString());
            viewBinding.titleText.setText(requestModel.getTitle());
            viewBinding.locationText.setText(requestModel.getLocation());
        }
    }
}