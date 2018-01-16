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

public class NotificationsActivity extends AppCompatActivity {

    private static List<String> mNotifications;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference notificationsRef = rootRef.child("Users")
            .child(mAuth.getCurrentUser().getUid())
            .child("Notifications");
    private List<String> mKeys;
    private RecyclerView mNotificationsRecyclerView;
    private NotificationsAdapter mNotificationsAdapter;

    public static List<String> getmNotifications() {
        return mNotifications;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNotificationsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_notifications);

        LinearLayoutManager notificationsLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mNotificationsRecyclerView.setLayoutManager(notificationsLayoutManager);


        notificationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mKeys = new ArrayList<String>();
                mNotifications = new ArrayList<String>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    mKeys.add(child.getKey().toString());
                    mNotifications.add(child.getValue().toString());
                }

                mNotificationsAdapter = new NotificationsAdapter(NotificationsActivity.this,
                        mNotifications.size(), new NotificationsAdapter.NotificationsAdapterListener() {
                    @Override
                    public void deleteViewOnClick(View v, int position) {
                        notificationsRef.child(mKeys.get(position)).removeValue();
                    }
                });

                mNotificationsRecyclerView.setAdapter(mNotificationsAdapter);

                mNotificationsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
