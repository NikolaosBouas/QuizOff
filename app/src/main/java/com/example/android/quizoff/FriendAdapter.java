package com.example.android.quizoff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Nikos on 27/12/2017.
 */

class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendAdapterViewHolder> {


    private final Context mContext;
    public FriendsAdapterListener onClickListener;
    private int mSize = 0;


    public FriendAdapter(@NonNull Context context, int size, FriendAdapter.FriendsAdapterListener listener) {
        mContext = context;
        mSize = size;
        this.onClickListener = listener;
    }

    @Override
    public FriendAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item, parent, false);

        view.setFocusable(true);

        return new FriendAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendAdapterViewHolder friendAdapterViewHolder, final int position) {
        if (FriendsActivity.getmFriends().get(position) != null) {
            friendAdapterViewHolder.friendNameView.setText(FriendsActivity.getmFriends().get(position));
        }

        friendAdapterViewHolder.challengeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.challengeTextViewOnClick(v, position);
            }
        });

    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our list
     */
    @Override
    public int getItemCount() {
        return mSize;
    }


    public interface FriendsAdapterListener {

        void challengeTextViewOnClick(View v, int position);


    }

    class FriendAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView friendNameView;

        final TextView challengeTextView;

        FriendAdapterViewHolder(View view) {
            super(view);

            friendNameView = (TextView) view.findViewById(R.id.friend_name_tv);

            challengeTextView = (TextView) view.findViewById(R.id.challenge_friend);

        }

    }
}