package com.example.android.quizoff;

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

public class FriendRequestsActivity extends AppCompatActivity {

    private static List<String> mFriendRequests;
    FirebaseAuth mAuth;
    private RecyclerView mFriendRequestRecyclerView;
    private FriendRequestAdapter mFriendRequestAdapter;
    private List<String> mKeys;

    public static List<String> getmFriendRequests() {
        return mFriendRequests;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFriendRequestRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_friendrequests);

        LinearLayoutManager friendRequestLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mFriendRequestRecyclerView.setLayoutManager(friendRequestLayoutManager);

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersRef = rootRef.child("Users");
        DatabaseReference currentUserRef = usersRef.child(mAuth.getCurrentUser().getUid());
        final DatabaseReference friendRequestsRef = currentUserRef.child("PendingFriendRequests");
        final DatabaseReference friendsRef = currentUserRef.child("Friends");


        friendRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mKeys = new ArrayList<>();
                mFriendRequests = new ArrayList<>();

                for (final DataSnapshot child : dataSnapshot.getChildren()) {
                    mFriendRequests.add(child.getValue().toString());
                    mKeys.add(child.getKey().toString());
                }
                mFriendRequestAdapter = new FriendRequestAdapter(FriendRequestsActivity.this, mFriendRequests.size(), new FriendRequestAdapter.FriendRequestsAdapterListener() {
                    @Override
                    public void acceptTextViewOnClick(View v, int position) {
                        friendsRef.child(mKeys.get(position)).setValue(mFriendRequests.get(position));
                        friendRequestsRef.child(mKeys.get(position)).removeValue();
                        DatabaseReference friendlyUserRef = usersRef.child(mKeys.get(position)).child("Friends")
                                .child(mAuth.getCurrentUser().getUid());
                        friendlyUserRef.setValue(mAuth.getCurrentUser().getDisplayName());

                    }

                    @Override
                    public void deleteViewOnClick(View v, int position) {
                        friendRequestsRef.child(mKeys.get(position)).removeValue();
                    }
                });

                mFriendRequestAdapter.notifyDataSetChanged();

                mFriendRequestRecyclerView.setAdapter(mFriendRequestAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
