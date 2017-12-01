/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Follow;
import com.cmput301.cia.models.Profile;

import java.util.List;

/**
 * An adapter for displaying the follow requests
 *
 * @author Jessica Prieto
 * November 24, 2017
 *
 * version 1.1
 */

public class FollowersRequestAdapter extends RecyclerView.Adapter<FollowersRequestAdapter.ViewHolder>{
    //a list of profiles that's currently requesting to follow the current user (followee)
    private List<Profile> followRequests;

    //the current user getting the follow requests
    private Profile followee;

    //the listener that listens when an item in the lsit is clicked
    private OnItemClickListener listener;

    private Context context;

    /**
     * Constructs a new adapter to be used in the recycler view for FollowRequestsActivity
     * @param context the RecyclerView context
     * @param followRequests the list of follow requests
     * @param followee the current user that has the requests
     */
    public FollowersRequestAdapter(Context context, List<Profile> followRequests, Profile followee) {
        this.followRequests = followRequests;
        this.followee = followee;
        this.context = context;
    }

    /** returns the Profile that was chosen in the recycler view
     *  @param position the index of the item in the list
     * **/
    public Profile getProfile(int position) {
        return followRequests.get(position);
    }

    /**
     * sets the follow requests to the current user
     * @param followerRequests
     */
    public void setFollowRequests(List<Profile> followerRequests) {
        followRequests = followerRequests;
        notifyDataSetChanged();
    }

    /**
     * the listener interface that waits when an item is clicked
     */
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    /**
     * the method that allows the parent activity or fragment to define the listener
     */
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
        //the user (follower) requesting to follow the current user (followee)
        final Profile follower = followRequests.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onItemClick(holder.itemView, position);
                }
            }
        });

        TextView textView = holder.followRequestee;
        textView.setText(follower.getName());
        Button approveButton = holder.approveButton;
        Button declineButton = holder.declineButton;

        /**
         * sets up a listener for the "Accept" button to accept the request
         */
        approveButton.setOnClickListener(new ButtonClickListener() {
            @Override
            public void handleClick() {
                followee.acceptFollowRequest(follower);
                followRequests.remove(position);
                notifyItemRemoved(position);
                /*if (follower.isFollowing(followee)){
                    followRequests.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Toast.makeText(context, "Error connecting to the database", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        /**
         * sets up a listener for the "Decline" button to ignore requests
         */
        declineButton.setOnClickListener(new ButtonClickListener() {
            @Override
            public void handleClick() {
                followee.removeFollowRequest(follower);
                followRequests.remove(position);
                notifyItemRemoved(position);
                /*if (!follower.isFollowing(followee)){
                    followRequests.remove(position);
                    notifyItemRemoved(position);    
                } else {
                    Toast.makeText(context, "Error connecting to the database", Toast.LENGTH_SHORT).show();
                }*/
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

            // listens when an item is clicked
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
