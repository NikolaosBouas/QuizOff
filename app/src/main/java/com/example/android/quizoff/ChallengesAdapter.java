package com.example.android.quizoff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Nikos on 29/12/2017.
 */

class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ChallengesAdapterViewHolder> {


    private final Context mContext;
    public ChallengesAdapterListener onClickListener;
    private int mSize = 0;


    public ChallengesAdapter(@NonNull Context context, int size, ChallengesAdapter.ChallengesAdapterListener listener) {
        mContext = context;
        mSize = size;
        this.onClickListener = listener;
    }

    @Override
    public ChallengesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.challenge_list_item, parent, false);

        view.setFocusable(true);

        return new ChallengesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChallengesAdapterViewHolder challengesAdapterViewHolder, final int position) {
        if (ChallengesActivity.getmChallenges() != null) {
            if (ChallengesActivity.getmChallenges().get(position) != null) {
                challengesAdapterViewHolder.challengeView.setText(ChallengesActivity.getmChallenges().get(position));
            }
        }


        challengesAdapterViewHolder.replyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.replyTextViewOnClick(v, position);
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


    public interface ChallengesAdapterListener {

        void replyTextViewOnClick(View v, int position);


    }

    class ChallengesAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView challengeView;

        final TextView replyTextView;

        ChallengesAdapterViewHolder(View view) {
            super(view);

            challengeView = (TextView) view.findViewById(R.id.challenge_tv);

            replyTextView = (TextView) view.findViewById(R.id.reply_tv);

        }

    }
}