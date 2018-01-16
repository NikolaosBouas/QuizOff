package com.example.android.quizoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    private static List<String> mFriends;
    FirebaseAuth mAuth;
    private RecyclerView mFriendRecyclerView;
    private FriendAdapter mFriendAdapter;
    private List<String> mKeys;

    public static List<String> getmFriends() {
        return mFriends;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mFriendRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_friends);

        LinearLayoutManager friendRequestLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mFriendRecyclerView.setLayoutManager(friendRequestLayoutManager);

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersRef = rootRef.child("Users");
        DatabaseReference currentUserRef = usersRef.child(mAuth.getCurrentUser().getUid());
        final DatabaseReference friendsRef = currentUserRef.child("Friends");


        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mKeys = new ArrayList<>();
                mFriends = new ArrayList<>();

                for (final DataSnapshot child : dataSnapshot.getChildren()) {
                    mFriends.add(child.getValue().toString());
                    mKeys.add(child.getKey().toString());
                }
                mFriendAdapter = new FriendAdapter(FriendsActivity.this, mFriends.size(), new FriendAdapter.FriendsAdapterListener() {
                    @Override
                    public void challengeTextViewOnClick(View v, final int position) {
                        DatabaseReference currentOpponentRef = usersRef.child(mKeys.get(position));
                        final DatabaseReference challengesRef = currentOpponentRef.child("Challenges");
                        DatabaseReference challengeRef = challengesRef.child(mAuth.getCurrentUser().getDisplayName());
                        final DatabaseReference stateRef = challengeRef.child("state");
                        stateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String state;
                                if (dataSnapshot.getValue() != null) {
                                    state = dataSnapshot.getValue().toString();
                                } else {
                                    state = "not even started";
                                }
                                if (!state.equals("ongoing")) {
                                    QuizUtils.setCurrentQuestionNumber(FriendsActivity.this, 0);
                                    QuizUtils.setCurrentScore(FriendsActivity.this, 0);
                                    stateRef.setValue("ongoing");
                                    Intent intent = new Intent(FriendsActivity.this, GameActivity.class);
                                    intent.putExtra("key", mKeys.get(position));
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(FriendsActivity.this, R.string.try_again_later, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

                mFriendAdapter.notifyDataSetChanged();

                mFriendRecyclerView.setAdapter(mFriendAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
