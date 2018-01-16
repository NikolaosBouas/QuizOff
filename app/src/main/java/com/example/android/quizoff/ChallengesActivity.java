package com.example.android.quizoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChallengesActivity extends AppCompatActivity {

    private static List<String> mChallenges;
    FirebaseAuth mAuth;
    private ChallengesAdapter mChallengesAdapter;

    public static List<String> getmChallenges() {
        return mChallenges;
    }

    @Bind(R.id.recyclerview_challenges) RecyclerView mChallengesRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        LinearLayoutManager challengesLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mChallengesRecyclerView.setLayoutManager(challengesLayoutManager);

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersRef = rootRef.child("Users");
        DatabaseReference currentUserRef = usersRef.child(mAuth.getCurrentUser().getUid());
        final DatabaseReference challengesRef = currentUserRef.child("Challenges");


        challengesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mChallenges = new ArrayList<>();

                for (final DataSnapshot child : dataSnapshot.getChildren()) {
                    mChallenges.add(child.getKey().toString());
                }
                mChallengesAdapter = new ChallengesAdapter(ChallengesActivity.this, mChallenges.size(), new ChallengesAdapter.ChallengesAdapterListener() {
                    @Override
                    public void replyTextViewOnClick(View v, int position) {
                        QuizUtils.setCurrentQuestionNumber(ChallengesActivity.this, 0);
                        QuizUtils.setCurrentScore(ChallengesActivity.this, 0);
                        Intent intent = new Intent(ChallengesActivity.this, ReplyToChallengeActivity.class);
                        intent.putExtra("username", mChallenges.get(position));
                        startActivity(intent);
                    }
                });

                mChallengesAdapter.notifyDataSetChanged();

                mChallengesRecyclerView.setAdapter(mChallengesAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
