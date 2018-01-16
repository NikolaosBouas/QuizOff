package com.example.android.quizoff;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    public static List<Integer> mQuestionNumbers = new ArrayList<>();
    private static String[] mQuestions = new String[3];
    private static String[] mRightAnswers = new String[3];
    private static String[] mWrongAnswersOne = new String[3];
    private static String[] mWrongAnswersTwo = new String[3];
    private static String[] mWrongAnswersThree = new String[3];
    Button buttonA;
    Button buttonB;
    Button buttonC;
    Button buttonD;
    TextView questionTV;
    FirebaseAuth mAuth;
    DatabaseReference opponentsScoreRef;
    private String mFriendlyKey;

    public static void setQuestions(String[] questions) {
        mQuestions = questions;
    }

    public static void setRightAnswers(String[] rightAnswers) {
        mRightAnswers = rightAnswers;
    }


    public static void setWrongAnswersOne(String[] wrongAnswersOne) {
        mWrongAnswersOne = wrongAnswersOne;
    }

    public static void setWrongAnswersTwo(String[] wrongAnswersTwo) {
        mWrongAnswersTwo = wrongAnswersTwo;
    }

    public static void setWrongAnswersThree(String[] wrongAnswersThree) {
        mWrongAnswersThree = wrongAnswersThree;
    }

    public static List<Integer> getmQuestionNumbers() {
        return mQuestionNumbers;
    }

    public static String[] getmRightAnswers() {
        return mRightAnswers;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mFriendlyKey = getIntent().getStringExtra("key");
        if (savedInstanceState == null) {
            QuizUtils.getQuestionsNumbers(mFriendlyKey, GameActivity.this);
        } else {
            int i;
            for (i = 0; i < 3; i++) {
                mQuestionNumbers.set(i, QuizUtils.getQuestionNumber(GameActivity.this, i, mFriendlyKey));
            }
        }

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference usersRef = rootRef.child("Users");
        DatabaseReference currentOpponentRef = usersRef.child(mFriendlyKey);
        final DatabaseReference challengesRef = currentOpponentRef.child("Challenges");
        DatabaseReference challengeRef = challengesRef.child(mAuth.getCurrentUser().getDisplayName());
        DatabaseReference challengersKeyRef = challengeRef.child("Challengers Key");
        challengersKeyRef.setValue(mAuth.getCurrentUser().getUid());
        DatabaseReference questionNumbersRef = challengeRef.child("QuestionNumbers");
        opponentsScoreRef = challengeRef.child("Opponent's Score");

        int i;
        for (i = 0; i < 3; i++) {
            DatabaseReference numberRef = questionNumbersRef.child(mQuestionNumbers.get(i).toString());
            numberRef.setValue(true);
        }

        QuizUtils.loadQuestionsToArrays(this);

        buttonA = (Button) findViewById(R.id.buttonA);
        buttonB = (Button) findViewById(R.id.buttonB);
        buttonC = (Button) findViewById(R.id.buttonC);
        buttonD = (Button) findViewById(R.id.buttonD);
        questionTV = (TextView) findViewById(R.id.questionView);


        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                if (buttonA.getText().equals(mRightAnswers[QuizUtils.getCurrentQuestionNumber(GameActivity.this)])) {
                    buttonA.setBackgroundTintList(getResources().getColorStateList(R.color.correct_button_color));
                    QuizUtils.setCurrentScore(GameActivity.this, QuizUtils.getCurrentScore(GameActivity.this) + 1);
                } else {
                    buttonA.setBackgroundTintList(getResources().getColorStateList(R.color.wrong_button_color));
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        QuizUtils.setCurrentQuestionNumber(GameActivity.this, QuizUtils.getCurrentQuestionNumber(GameActivity.this) + 1);
                        if (QuizUtils.getCurrentQuestionNumber(GameActivity.this) < 3) {
                            loadDataToViews();
                            getButtonsBackToNormal();
                        } else {
                            opponentsScoreRef.setValue(QuizUtils.getCurrentScore(GameActivity.this));
                            Intent intent = new Intent(GameActivity.this, FriendsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 1500);


            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                if (buttonB.getText().equals(mRightAnswers[QuizUtils.getCurrentQuestionNumber(GameActivity.this)])) {
                    buttonB.setBackgroundTintList(getResources().getColorStateList(R.color.correct_button_color));
                    QuizUtils.setCurrentScore(GameActivity.this, QuizUtils.getCurrentScore(GameActivity.this) + 1);
                } else {
                    buttonB.setBackgroundTintList(getResources().getColorStateList(R.color.wrong_button_color));
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        QuizUtils.setCurrentQuestionNumber(GameActivity.this, QuizUtils.getCurrentQuestionNumber(GameActivity.this) + 1);
                        if (QuizUtils.getCurrentQuestionNumber(GameActivity.this) < 3) {
                            loadDataToViews();
                            getButtonsBackToNormal();
                        } else {
                            opponentsScoreRef.setValue(QuizUtils.getCurrentScore(GameActivity.this));
                            Intent intent = new Intent(GameActivity.this, FriendsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 1500);


            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                if (buttonC.getText().equals(mRightAnswers[QuizUtils.getCurrentQuestionNumber(GameActivity.this)])) {
                    buttonC.setBackgroundTintList(getResources().getColorStateList(R.color.correct_button_color));
                    QuizUtils.setCurrentScore(GameActivity.this, QuizUtils.getCurrentScore(GameActivity.this) + 1);
                } else {
                    buttonC.setBackgroundTintList(getResources().getColorStateList(R.color.wrong_button_color));
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        QuizUtils.setCurrentQuestionNumber(GameActivity.this, QuizUtils.getCurrentQuestionNumber(GameActivity.this) + 1);
                        if (QuizUtils.getCurrentQuestionNumber(GameActivity.this) < 3) {
                            loadDataToViews();
                            getButtonsBackToNormal();
                        } else {
                            opponentsScoreRef.setValue(QuizUtils.getCurrentScore(GameActivity.this));
                            Intent intent = new Intent(GameActivity.this, FriendsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 1500);


            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                if (buttonD.getText().equals(mRightAnswers[QuizUtils.getCurrentQuestionNumber(GameActivity.this)])) {
                    buttonD.setBackgroundTintList(getResources().getColorStateList(R.color.correct_button_color));
                    QuizUtils.setCurrentScore(GameActivity.this, QuizUtils.getCurrentScore(GameActivity.this) + 1);
                } else {
                    buttonD.setBackgroundTintList(getResources().getColorStateList(R.color.wrong_button_color));
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        QuizUtils.setCurrentQuestionNumber(GameActivity.this, QuizUtils.getCurrentQuestionNumber(GameActivity.this) + 1);
                        if (QuizUtils.getCurrentQuestionNumber(GameActivity.this) < 3) {
                            loadDataToViews();
                            getButtonsBackToNormal();
                        } else {
                            opponentsScoreRef.setValue(QuizUtils.getCurrentScore(GameActivity.this));
                            Intent intent = new Intent(GameActivity.this, FriendsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 1500);
            }
        });

        loadDataToViews();


    }

    public void loadDataToViews() {


        List<String> answers = new ArrayList<>();
        answers.add(mRightAnswers[QuizUtils.getCurrentQuestionNumber(GameActivity.this)]);
        answers.add(mWrongAnswersOne[QuizUtils.getCurrentQuestionNumber(GameActivity.this)]);
        answers.add(mWrongAnswersTwo[QuizUtils.getCurrentQuestionNumber(GameActivity.this)]);
        answers.add(mWrongAnswersThree[QuizUtils.getCurrentQuestionNumber(GameActivity.this)]);
        Collections.shuffle(answers);

        buttonA.setText(answers.get(0));
        buttonB.setText(answers.get(1));
        buttonC.setText(answers.get(2));
        buttonD.setText(answers.get(3));
        questionTV.setText(mQuestions[QuizUtils.getCurrentQuestionNumber(GameActivity.this)]);
    }

    public void getButtonsBackToNormal() {

        buttonA.setBackgroundTintList(getResources().getColorStateList(R.color.defaultButtonColor));
        buttonB.setBackgroundTintList(getResources().getColorStateList(R.color.defaultButtonColor));
        buttonC.setBackgroundTintList(getResources().getColorStateList(R.color.defaultButtonColor));
        buttonD.setBackgroundTintList(getResources().getColorStateList(R.color.defaultButtonColor));

        buttonA.setClickable(true);
        buttonB.setClickable(true);
        buttonC.setClickable(true);
        buttonD.setClickable(true);

    }

    public void disableButtons() {
        buttonA.setClickable(false);
        buttonB.setClickable(false);
        buttonC.setClickable(false);
        buttonD.setClickable(false);

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(GameActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(GameActivity.this);
        }
        builder.setTitle(getResources().getString(R.string.really_exit))
                .setMessage(getResources().getString(R.string.sure_you_wanna_exit_score_0))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(GameActivity.this,R.string.sent_challenge_score_0,Toast.LENGTH_SHORT).show();
                        opponentsScoreRef.setValue(0);
                        Intent intent = new Intent(GameActivity.this, FriendsActivity.class);
                        startActivity(intent);
                        finish();
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
