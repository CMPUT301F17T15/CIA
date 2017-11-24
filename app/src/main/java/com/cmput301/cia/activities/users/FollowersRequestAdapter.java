/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Context;
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
 * Created by Jessica on 2017-11-24.
 */

public class FollowersRequestAdapter extends RecyclerView.Adapter<FollowersRequestAdapter.ViewHolder>{

    private List<Profile> followRequests;
    private Context context;

    public FollowersRequestAdapter(Context context, List<Profile> followRequests) {
        this.followRequests = followRequests;
        this.context = context;
    }

    private Context getContext() {
        return context;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Profile contact = followRequests.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.followRequestee;
        textView.setText(contact.getName());
    }

    @Override
    public int getItemCount() {
        return followRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView followRequestee;
        public Button approveButton;
        public Button declineButton;

        public ViewHolder(View itemView) {
            super(itemView);

            followRequestee = (TextView) itemView.findViewById(R.id.followRequesteeName);
            approveButton = (Button) itemView.findViewById(R.id.approveButton);
            declineButton = (Button) itemView.findViewById(R.id.declineButton);
        }
    }
}
