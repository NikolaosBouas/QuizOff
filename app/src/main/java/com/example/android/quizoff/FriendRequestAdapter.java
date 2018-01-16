package com.example.android.quizoff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Nikos on 24/12/2017.
 */
class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestAdapterViewHolder> {


    private final Context mContext;
    public FriendRequestsAdapterListener onClickListener;
    private int mSize = 0;


    public FriendRequestAdapter(@NonNull Context context, int size, FriendRequestsAdapterListener listener) {
        mContext = context;
        mSize = size;
        this.onClickListener = listener;
    }

    @Override
    public FriendRequestAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.friend_request_list_item, parent, false);

        view.setFocusable(true);

        return new FriendRequestAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendRequestAdapterViewHolder friendRequestAdapterViewHolder, final int position) {
        if (FriendRequestsActivity.getmFriendRequests().get(position) != null) {
            friendRequestAdapterViewHolder.friendNameView.setText(FriendRequestsActivity.getmFriendRequests().get(position));
        }
        friendRequestAdapterViewHolder.acceptTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.acceptTextViewOnClick(v, position);
            }
        });

        friendRequestAdapterViewHolder.declineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.deleteViewOnClick(v, position);
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


    public interface FriendRequestsAdapterListener {

        void acceptTextViewOnClick(View v, int position);

        void deleteViewOnClick(View v, int position);

    }

    class FriendRequestAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView friendNameView;

        final TextView acceptTextView;

        final TextView declineTextView;

        FriendRequestAdapterViewHolder(View view) {
            super(view);

            friendNameView = (TextView) view.findViewById(R.id.friendrequest_tv);

            acceptTextView = (TextView) view.findViewById(R.id.accept_friend_request);

            declineTextView = (TextView) view.findViewById(R.id.delete_friend_request);

        }

    }

}
