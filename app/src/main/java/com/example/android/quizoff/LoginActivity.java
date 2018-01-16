package com.example.android.quizoff;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    private EditText mEmailView;
    private ProgressBar mProgressBar;
    private Button mLoginButton;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email_login);
        mPasswordView = (EditText) findViewById(R.id.password_login);
        mLoginButton = (Button) findViewById(R.id.login);
        mProgressBar = (ProgressBar) findViewById(R.id.login_pb);

        mAuth = FirebaseAuth.getInstance();


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        TextView button = (TextView) findViewById(R.id.to_go_sign_up_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin() {
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        if (email.isEmpty()) {
            mEmailView.setError(getResources().getString(R.string.user_required));
            mEmailView.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailView.setError(getResources().getString(R.string.invalid_email));
            mEmailView.requestFocus();
            return;
        }


        if (password.isEmpty()) {
            mPasswordView.setError(getResources().getString(R.string.password_required));
            mPasswordView.requestFocus();
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainQuizActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    mPasswordView.getText().clear();


                    Intent intent1 = new Intent(LoginActivity.this, QuizOffWidgetProvider.class);
                    intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
                    // since it seems the onUpdate() is only fired on that:
                    int[] ids = AppWidgetManager.getInstance(getApplication())
                            .getAppWidgetIds(new ComponentName(getApplication(), QuizOffWidgetProvider.class));
                    intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    sendBroadcast(intent1);

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
