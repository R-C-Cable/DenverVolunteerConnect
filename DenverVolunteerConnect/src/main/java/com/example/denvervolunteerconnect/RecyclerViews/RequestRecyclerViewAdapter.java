package com.example.denvervolunteerconnect.RecyclerViews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.denvervolunteerconnect.databinding.VolunteerRequestListItemBinding;
import com.example.denvervolunteerconnect.Models.RequestModel;

import java.util.ArrayList;

public class RequestRecyclerViewAdapter extends RecyclerView.Adapter<RequestRecyclerViewAdapter.RequestItemViewHolder> {
    ArrayList<RequestModel> volunteerRequestList = new ArrayList<>();
    private RequestItemOnClickListener requestItemOnClickListener;
    private MutableLiveData<String> liveUserId = new MutableLiveData<>();
    Context context = null;

    public RequestRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public interface RequestItemOnClickListener {
        void onClick(RequestModel requestModel);
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
        return volunteerRequestList.size();
    }

    public void updateList(ArrayList<RequestModel> volunteerRequestList) {
        this.volunteerRequestList = volunteerRequestList;
        notifyDataSetChanged();
    }

    public void setRequestItemOnClickListener(RequestItemOnClickListener requestItemOnClickListener) {
        this.requestItemOnClickListener = requestItemOnClickListener;
    }

    public void notifyUserChange(String currentUserId) {
        liveUserId.setValue(currentUserId);
    }


    @Override
    public void onViewRecycled(@NonNull RequestItemViewHolder holder) {
        super.onViewRecycled(holder);
        holder.unBind();
    }

    public class RequestItemViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = RequestItemViewHolder.class.getSimpleName();
        private VolunteerRequestListItemBinding viewBinding;

        public RequestItemViewHolder(@NonNull VolunteerRequestListItemBinding volunteerRequestListItemBinding) {
            super(volunteerRequestListItemBinding.getRoot());
            viewBinding = volunteerRequestListItemBinding;
        }

        public void onBind(RequestModel requestModel) {


            liveUserId.observe((LifecycleOwner) viewBinding.getRoot().getContext(), new Observer<String>() {
                @Override
                public void onChanged(String currentLoggedInUser) {
                    if(currentLoggedInUser.equals(requestModel.getVolunteerId())) {
                        viewBinding.volunteerStar.setVisibility(View.VISIBLE);
                    }
                }
            });

            viewBinding.titleText.setText(requestModel.getTitle());
            viewBinding.locationText.setText(requestModel.getLocation());
            viewBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (requestItemOnClickListener != null) {
                        requestItemOnClickListener.onClick(requestModel);
                    }
                }
            });
        }

        public void unBind(){
            Log.v(TAG, "RequestItemViewHolder unBind");
        }
    }
}