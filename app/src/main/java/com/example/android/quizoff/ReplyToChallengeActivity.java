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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReplyToChallengeActivity extends AppCompatActivity {

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
    DatabaseReference challengeRef;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference usersRef = rootRef.child("Users");
    private int mOpponentsFinalScore;
    private int mMyFinalScore;
    private String mOpponentsKey;

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

    public static String[] getmRightAnswers() {
        return mRightAnswers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_to_challenge);
        final List<Integer> questionNumbers = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();


        DatabaseReference currentUserRef = usersRef.child(mAuth.getCurrentUser().getUid());
        final DatabaseReference challengesRef = currentUserRef.child("Challenges");
        challengeRef = challengesRef.child(getIntent().getStringExtra("username"));
        DatabaseReference questionNumbersRef = challengeRef.child("QuestionNumbers");
        final DatabaseReference myScoreRef = challengeRef.child("My Score");

        challengeRef.child("Challengers Key").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mOpponentsKey = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        questionNumbersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot child : dataSnapshot.getChildren()) {
                    questionNumbers.add(Integer.parseInt(child.getKey()));
                }
                QuizUtils.loadPredefinedQuestionsToArrays(ReplyToChallengeActivity.this, questionNumbers);
                loadDataToViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        buttonA = (Button) findViewById(R.id.buttonA);
        buttonB = (Button) findViewById(R.id.buttonB);
        buttonC = (Button) findViewById(R.id.buttonC);
        buttonD = (Button) findViewById(R.id.buttonD);
        questionTV = (TextView) findViewById(R.id.questionView);


        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButtons();
                if (buttonA.getText().equals(mRightAnswers[QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this)])) {
                    buttonA.setBackgroundTintList(getResources().getColorStateList(R.color.correct_button_color));
                    QuizUtils.setCurrentScore(ReplyToChallengeActivity.this, QuizUtils.getCurrentScore(ReplyToChallengeActivity.this) + 1);
                } else {
                    buttonA.setBackgroundTintList(getResources().getColorStateList(R.color.wrong_button_color));
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        QuizUtils.setCurrentQuestionNumber(ReplyToChallengeActivity.this, QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this) + 1);
                        if (QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this) < 3) {
                            loadDataToViews();
                            getButtonsBackToNormal();
                        } else {
                            myScoreRef.setValue(QuizUtils.getCurrentScore(ReplyToChallengeActivity.this));
                            deleteChallenge();
                            Intent intent = new Intent(ReplyToChallengeActivity.this, ChallengesActivity.class);
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
                if (buttonB.getText().equals(mRightAnswers[QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this)])) {
                    buttonB.setBackgroundTintList(getResources().getColorStateList(R.color.correct_button_color));
                    QuizUtils.setCurrentScore(ReplyToChallengeActivity.this, QuizUtils.getCurrentScore(ReplyToChallengeActivity.this) + 1);
                } else {
                    buttonB.setBackgroundTintList(getResources().getColorStateList(R.color.wrong_button_color));
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        QuizUtils.setCurrentQuestionNumber(ReplyToChallengeActivity.this, QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this) + 1);
                        if (QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this) < 3) {
                            loadDataToViews();
                            getButtonsBackToNormal();
                        } else {
                            myScoreRef.setValue(QuizUtils.getCurrentScore(ReplyToChallengeActivity.this));
                            deleteChallenge();
                            Intent intent = new Intent(ReplyToChallengeActivity.this, ChallengesActivity.class);
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
                if (buttonC.getText().equals(mRightAnswers[QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this)])) {
                    buttonC.setBackgroundTintList(getResources().getColorStateList(R.color.correct_button_color));
                    QuizUtils.setCurrentScore(ReplyToChallengeActivity.this, QuizUtils.getCurrentScore(ReplyToChallengeActivity.this) + 1);
                } else {
                    buttonC.setBackgroundTintList(getResources().getColorStateList(R.color.wrong_button_color));
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        QuizUtils.setCurrentQuestionNumber(ReplyToChallengeActivity.this, QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this) + 1);
                        if (QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this) < 3) {
                            loadDataToViews();
                            getButtonsBackToNormal();
                        } else {
                            myScoreRef.setValue(QuizUtils.getCurrentScore(ReplyToChallengeActivity.this));
                            deleteChallenge();
                            Intent intent = new Intent(ReplyToChallengeActivity.this, ChallengesActivity.class);
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
                if (buttonD.getText().equals(mRightAnswers[QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this)])) {
                    buttonD.setBackgroundTintList(getResources().getColorStateList(R.color.correct_button_color));
                    QuizUtils.setCurrentScore(ReplyToChallengeActivity.this, QuizUtils.getCurrentScore(ReplyToChallengeActivity.this) + 1);
                } else {
                    buttonD.setBackgroundTintList(getResources().getColorStateList(R.color.wrong_button_color));
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        QuizUtils.setCurrentQuestionNumber(ReplyToChallengeActivity.this, QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this) + 1);
                        if (QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this) < 3) {
                            loadDataToViews();
                            getButtonsBackToNormal();
                        } else {
                            myScoreRef.setValue(QuizUtils.getCurrentScore(ReplyToChallengeActivity.this));
                            deleteChallenge();
                            Intent intent = new Intent(ReplyToChallengeActivity.this, ChallengesActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 1500);
            }
        });


    }

    public void loadDataToViews() {


        List<String> answers = new ArrayList<>();
        answers.add(mRightAnswers[QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this)]);
        answers.add(mWrongAnswersOne[QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this)]);
        answers.add(mWrongAnswersTwo[QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this)]);
        answers.add(mWrongAnswersThree[QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this)]);
        Collections.shuffle(answers);

        buttonA.setText(answers.get(0));
        buttonB.setText(answers.get(1));
        buttonC.setText(answers.get(2));
        buttonD.setText(answers.get(3));
        questionTV.setText(mQuestions[QuizUtils.getCurrentQuestionNumber(ReplyToChallengeActivity.this)]);
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

    public void deleteChallenge() {

        mMyFinalScore = QuizUtils.getCurrentScore(ReplyToChallengeActivity.this);
        pickAWinner();


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ReplyToChallengeActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(ReplyToChallengeActivity.this);
        }
        builder.setTitle(getResources().getString(R.string.really_exit))
                .setMessage(getResources().getString(R.string.sure_you_wanna_exit_lose_challenge))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mMyFinalScore = 0;
                        pickAWinner();
                        Intent intent = new Intent(ReplyToChallengeActivity.this, ChallengesActivity.class);
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

    public void pickAWinner() {
        DatabaseReference opponentRef = usersRef.child(mOpponentsKey);
        final DatabaseReference notificationsRef = opponentRef.child("Notifications");


        challengeRef.child("Opponent's Score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mOpponentsFinalScore = Integer.parseInt(dataSnapshot.getValue().toString());
                if (mOpponentsFinalScore == mMyFinalScore) {
                    Toast.makeText(ReplyToChallengeActivity.this, R.string.tie, Toast.LENGTH_SHORT).show();
                    DatabaseReference notificationRef =
                            notificationsRef.child(mAuth.getCurrentUser().getDisplayName());
                    notificationRef.setValue(getString(R.string.ended_in_tie) + " " + mAuth.getCurrentUser().getDisplayName());
                } else if (mOpponentsFinalScore < mMyFinalScore) {
                    Toast.makeText(ReplyToChallengeActivity.this, R.string.win, Toast.LENGTH_SHORT).show();
                    DatabaseReference notificationRef =
                            notificationsRef.child(mAuth.getCurrentUser().getDisplayName());
                    notificationRef.setValue(getString(R.string.ended_in_loss) + " " + mAuth.getCurrentUser().getDisplayName());
                } else if (mOpponentsFinalScore > mMyFinalScore) {
                    Toast.makeText(ReplyToChallengeActivity.this, R.string.loss, Toast.LENGTH_SHORT).show();
                    DatabaseReference notificationRef =
                            notificationsRef.child(mAuth.getCurrentUser().getDisplayName());
                    notificationRef.setValue(getString(R.string.ended_in_win) + " " + mAuth.getCurrentUser().getDisplayName());
                }
                challengeRef.removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
