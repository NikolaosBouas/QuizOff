package com.example.android.quizoff;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Nikos on 31/12/2017.
 */


public class RemoteFetchService extends IntentService {

    public static ArrayList<String> notificationsList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference notificationsRef;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RemoteFetchService() {
        super("RemoteFetchService");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (mAuth.getCurrentUser() != null) {
            notificationsRef = rootRef.child("Users").child(mAuth.getCurrentUser().getUid())
                    .child("Notifications");

            notificationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    notificationsList = new ArrayList<String>();

                    for (final DataSnapshot child : dataSnapshot.getChildren()) {
                        notificationsList.add(child.getValue().toString());
                    }
                    populateWidget();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            //Empty notifications is a node i ve created in the firebase realtime database that
            //contains the number 0
            //The purpose of this is to have a small time delay so that the widget updates
            //correctly. Just calling notificationsList = new ArrayList<String>();
            //populateWidget(); would not work
            DatabaseReference emptyNotReference = rootRef.child("EmptyNotifications");
            emptyNotReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    notificationsList = new ArrayList<String>();
                    populateWidget();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }


        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
            appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
    }




    /**
     * Method which sends broadcast to WidgetProvider
     * so that widget is notified to do necessary action
     * and here action == WidgetProvider.DATA_FETCHED
     */
    private void populateWidget() {

        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(QuizOffWidgetProvider.DATA_FETCHED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId);
        sendBroadcast(widgetUpdateIntent);
        this.stopSelf();
    }

}