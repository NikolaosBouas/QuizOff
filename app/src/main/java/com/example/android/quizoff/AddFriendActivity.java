package com.example.android.quizoff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddFriendActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("Users");
    private String mFriendKey;

    @Bind(R.id.username_to_search) EditText mEditText;
    @Bind(R.id.friend_textview) TextView mFriendTextView;
    @Bind(R.id.add_friend_button) Button mAddFriendButton;
    @Bind(R.id.search_button) Button mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mEditText.getText().toString().trim();
                if (!username.isEmpty()) {
                    if (!username.equals(mAuth.getCurrentUser().getDisplayName())) {
                        mUsersRef.orderByChild("username").equalTo(username).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        mFriendKey = child.getKey();
                                        mFriendTextView.setText(R.string.friend_found);
                                    }
                                } else {
                                    mFriendKey = null;
                                    Toast.makeText(AddFriendActivity.this, R.string.no_user_with_username, Toast.LENGTH_SHORT).show();
                                    mFriendTextView.setText(R.string.no_friend_found);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(AddFriendActivity.this, R.string.this_is_ur_username, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddFriendActivity.this, R.string.type_username_then_search, Toast.LENGTH_SHORT).show();
                }
            }
        });


        mAddFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFriendKey != null) {

                    DatabaseReference friendsRef = mUsersRef.child(mAuth.getCurrentUser().getUid())
                            .child("Friends").child(mFriendKey);
                    friendsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(AddFriendActivity.this, R.string.already_friends
                                        , Toast.LENGTH_SHORT).show();
                            } else {
                                addFriend(mFriendKey);
                                Toast.makeText(AddFriendActivity.this, R.string.friend_request_sent, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(AddFriendActivity.this, R.string.find_valid_friend, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void addFriend(String key) {
        mUsersRef.child(key).child("PendingFriendRequests").child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getDisplayName());
    }
}
