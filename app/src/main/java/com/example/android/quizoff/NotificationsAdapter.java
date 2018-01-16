package com.example.android.quizoff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Nikos on 30/12/2017.
 */
class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsAdapterViewHolder> {


    private final Context mContext;
    public NotificationsAdapterListener onClickListener;
    private int mSize = 0;


    public NotificationsAdapter(@NonNull Context context, int size, NotificationsAdapter.NotificationsAdapterListener listener) {
        mContext = context;
        mSize = size;
        this.onClickListener = listener;
    }

    @Override
    public NotificationsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_list_item, parent, false);

        view.setFocusable(true);

        return new NotificationsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationsAdapterViewHolder notificationsAdapterViewHolder, final int position) {
        if (NotificationsActivity.getmNotifications() != null) {
            if (NotificationsActivity.getmNotifications().get(position) != null) {
                notificationsAdapterViewHolder.notificationNameView.setText(NotificationsActivity.getmNotifications().get(position));
            }
        }


        notificationsAdapterViewHolder.deleteView.setOnClickListener(new View.OnClickListener() {
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


    public interface NotificationsAdapterListener {

        void deleteViewOnClick(View v, int position);


    }

    class NotificationsAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView notificationNameView;

        final TextView deleteView;

        NotificationsAdapterViewHolder(View view) {
            super(view);

            notificationNameView = (TextView) view.findViewById(R.id.notification_name);

            deleteView = (TextView) view.findViewById(R.id.delete_notification);

        }

    }
}