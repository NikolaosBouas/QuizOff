package com.example.android.quizoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    String userId;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("Users");
    private EditText mUsernameEditText;
    private Button mSaveProfileButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsernameEditText = (EditText) findViewById(R.id.enter_username_edit_text);
        mSaveProfileButton = (Button) findViewById(R.id.save_profile_button);
        mProgressBar = (ProgressBar) findViewById(R.id.sign_up_pb);

        mAuth = FirebaseAuth.getInstance();

        mSaveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });
    }

    private void saveUserInfo() {
        final String username = mUsernameEditText.getText().toString().trim();
        if (username.isEmpty()) {
            mUsernameEditText.setError(getResources().getString(R.string.username_required));
            mUsernameEditText.requestFocus();
            return;
        }
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            UserProfileChangeRequest profile =
                    new UserProfileChangeRequest.Builder().setDisplayName(username).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        userId = user.getUid();
                        DatabaseReference mUserIdRef = mUsersRef.child(userId);
                        DatabaseReference mUsernameRef = mUserIdRef.child("username");
                        mUsernameRef.setValue(username);
                        Toast.makeText((MainActivity.this), R.string.profile_updated, Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.VISIBLE);
                        userLogin();
                    }
                }
            });
        }
    }

    private void userLogin() {
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        if (!email.isEmpty() && !password.isEmpty()) {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mProgressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MainActivity.this, MainQuizActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }


}
