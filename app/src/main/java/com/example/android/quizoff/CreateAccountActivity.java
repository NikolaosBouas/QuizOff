package com.example.android.quizoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateAccountActivity extends AppCompatActivity {



    @Bind(R.id.email_create) EditText mEmailView;
    @Bind(R.id.sign_up_button) Button mButton;
    @Bind(R.id.password_create) EditText mPasswordView;
    @Bind(R.id.create_account_pb) ProgressBar mProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });


    }

    private void registerUser(){
        final String email = mEmailView.getText().toString().trim();
        final String password = mPasswordView.getText().toString().trim();

        if (email.isEmpty()){
            mEmailView.setError(getResources().getString(R.string.user_required));
            mEmailView.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailView.setError(getResources().getString(R.string.invalid_email));
            mEmailView.requestFocus();
            return;
        }


        if (password.isEmpty()){
            mPasswordView.setError(getResources().getString(R.string.password_required));
            mPasswordView.requestFocus();
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        mProgressBar.setVisibility(View.GONE);

                        if(task.isSuccessful()){
                            Toast.makeText(CreateAccountActivity.this,
                                    R.string.user_created,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateAccountActivity.this,MainActivity.class);
                            intent.putExtra("email",email);
                            intent.putExtra("password",password);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }


                        if (!task.isSuccessful()) {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(CreateAccountActivity.this,
                                        R.string.email_already_used, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(CreateAccountActivity.this,
                                         task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });
    }
}
