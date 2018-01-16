package com.example.android.quizoff;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainQuizActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Button mNavigateToFriendActivityButton;
    private Button mNavigateToFriendRequestsButton;
    private Button mNavigateToFriendsButton;
    private Button mNavigateToChallengesButton;
    private Button mNavigateToNotificationsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);

        mAuth = FirebaseAuth.getInstance();

        mNavigateToNotificationsButton = (Button) findViewById(R.id.navigate_notifications_activity);
        mNavigateToFriendActivityButton = (Button) findViewById(R.id.navigate_add_friend_activity);
        mNavigateToFriendRequestsButton = (Button) findViewById(R.id.navigate_friend_requests_activity);
        mNavigateToFriendsButton = (Button) findViewById(R.id.navigate_friends_activity);
        mNavigateToChallengesButton = (Button) findViewById(R.id.navigate_challenges_activity);

        mNavigateToFriendRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainQuizActivity.this, FriendRequestsActivity.class);
                startActivity(intent);
            }
        });

        mNavigateToChallengesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainQuizActivity.this, ChallengesActivity.class);
                startActivity(intent);
            }
        });


        mNavigateToFriendActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainQuizActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });

        mNavigateToFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainQuizActivity.this, FriendsActivity.class);
                startActivity(intent);
            }
        });

        mNavigateToNotificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainQuizActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MainQuizActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MainQuizActivity.this);
        }
        builder.setTitle(getResources().getString(R.string.really_exit))
                .setMessage(getResources().getString(R.string.sure_you_wanna_exit_sign_out))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();

                        Intent intent1 = new Intent(MainQuizActivity.this, QuizOffWidgetProvider.class);
                        intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
                        // since it seems the onUpdate() is only fired on that:
                        int[] ids = AppWidgetManager.getInstance(getApplication())
                                .getAppWidgetIds(new ComponentName(getApplication(), QuizOffWidgetProvider.class));
                        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                        sendBroadcast(intent1);

                        Toast.makeText(MainQuizActivity.this,getString(R.string.you_were_signed_out),Toast.LENGTH_SHORT).show();
                        MainQuizActivity.super.onBackPressed();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


}
