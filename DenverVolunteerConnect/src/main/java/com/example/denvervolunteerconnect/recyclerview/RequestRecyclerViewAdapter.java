package com.example.denvervolunteerconnect.recyclerview;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;


import com.example.denvervolunteerconnect.R;
import com.example.denvervolunteerconnect.ViewModels.MainActivityViewModel;
import com.example.denvervolunteerconnect.databinding.VolunteerRequestListItemBinding;
import com.example.denvervolunteerconnect.fragments.BrowsingFragment;
import com.example.denvervolunteerconnect.models.RequestModel;

import java.util.ArrayList;
import java.util.List;

public class RequestRecyclerViewAdapter extends RecyclerView.Adapter<RequestRecyclerViewAdapter.RequestItemViewHolder> {
    ArrayList<RequestModel> volunteerRequestList = new ArrayList<>();
    private OnClickListener onClickListener;
    MainActivityViewModel mMainActivityViewModel = null;
    Context context = null;

    public RequestRecyclerViewAdapter(Context context, MainActivityViewModel mainActivityViewModel) {
        this.context = context;
        this.mMainActivityViewModel = mainActivityViewModel;
        Log.v("ROBERT", "RequestRecyclerViewAdapter");
    }

    public interface OnClickListener {
        void onClick(RequestModel requestModel);
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

    public void updateList(ArrayList<RequestModel> volunteerRequestList) {
        this.volunteerRequestList = volunteerRequestList;
        Log.e("ROBERT","adapter " + volunteerRequestList);
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
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
            Log.v("ROBERT", "RequestItemViewHolder");
        }

        public void onBind(RequestModel requestModel) {
            Log.v("ROBERT", requestModel.toString());
            viewBinding.titleText.setText(requestModel.getTitle());
            viewBinding.locationText.setText(requestModel.getLocation());
            viewBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickListener != null) {
                        onClickListener.onClick(requestModel);
                    }
                }
            });
        }

        public void unBind(){
            Log.v(TAG, "RequestItemViewHolder unBind");
        }
    }
}