/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Profile;

import java.util.List;

/**
 * @author Jessica Prieto
 * November 24, 2017
 *
 * vresion 1
 */

public class FollowersRequestAdapter extends RecyclerView.Adapter<FollowersRequestAdapter.ViewHolder>{
    private List<Profile> followRequests;
    private Context context;
    private Profile profile;

    public FollowersRequestAdapter(Context context, List<Profile> followRequests, Profile profile) {
        this.followRequests = followRequests;
        this.context = context;
        this.profile = profile;
    }

    private Context getContext() {
        return context;
    }

    public Profile getProfile(int position) {
        return followRequests.get(position);
    }

    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.list_item_follow_requests, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Profile userRequestee = followRequests.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onItemClick(holder.itemView, position);
                }
            }
        });

        // Set item views based on your views and data model
        TextView textView = holder.followRequestee;
        textView.setText(userRequestee.getName());

        Button approveButton = holder.approveButton;
        approveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                profile.acceptFollowRequest(userRequestee);
                profile.removeFollowRequest(userRequestee);
                profile.save();
                userRequestee.save();
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return followRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView followRequestee;
        public Button approveButton;
        public Button declineButton;

        public ViewHolder(final View itemView) {
            super(itemView);

            followRequestee = (TextView) itemView.findViewById(R.id.followRequesteeName);
            approveButton = (Button) itemView.findViewById(R.id.approveButton);
            declineButton = (Button) itemView.findViewById(R.id.declineButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}
