package com.example.android.quizoff;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nikos on 28/12/2017.
 */
class QuizUtils {

    private static final String CURRENT_SCORE_KEY = "current_score";
    private static final String CURRENT_QUESTION_NUMBER_KEY = "current_question_number";
    private static final String QUESTION_NUMBERS_KEY = "current_question_numbers";


    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("questions.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static int getQuestionNumber(Context context, int i, String friendlyKey) {
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        return mPreferences.getInt(QUESTION_NUMBERS_KEY + friendlyKey + i, 0);
    }

    public static void setQuestionNumber(Context context, int questionNumber, int i, String friendlyKey) {
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putInt(QUESTION_NUMBERS_KEY + friendlyKey + i, questionNumber);
        editor.apply();


    }

    public static void getQuestionsNumbers(String friendlyKey, Context context) {
        int i;
        List<Integer> myArray = new ArrayList<>();
        List<Integer> helperArray = new ArrayList<>();
        for (i = 0; i < 11; i++) {
            myArray.add(i);
        }
        Collections.shuffle(myArray);
        int j;
        for (j = 0; j < 3; j++) {
            helperArray.add(myArray.get(j));
            myArray.remove(j);
        }
        GameActivity.mQuestionNumbers = helperArray;
        int k;
        for (k = 0; k < 3; k++) {
            setQuestionNumber(context, helperArray.get(k), k, friendlyKey);
        }


    }

    public static void loadQuestionsToArrays(Context context) {
        String simpleJSON = QuizUtils.loadJSONFromAsset(context);
        List<Integer> questionNumbers = GameActivity.getmQuestionNumbers();
        String[] questions = new String[3];
        String[] rightAnswers = new String[3];
        String[] wrongAnswersOne = new String[3];
        String[] wrongAnswersTwo = new String[3];
        String[] wrongAnswersThree = new String[3];
        try {
            JSONArray jsonArray = new JSONArray(simpleJSON);
            int j;
            for (j = 0; j < 3; j++) {
                JSONObject jsonObject = jsonArray.getJSONObject(questionNumbers.get(j));
                String question = jsonObject.getString("name");
                String rightAnswer = jsonObject.getString("rightAnswer");
                String wrongAnswerOne = jsonObject.getString("wrongAnswerOne");
                String wrongAnswerTwo = jsonObject.getString("wrongAnswerTwo");
                String wrongAnswerThree = jsonObject.getString("wrongAnswerThree");
                questions[j] = question;
                rightAnswers[j] = rightAnswer;
                wrongAnswersOne[j] = wrongAnswerOne;
                wrongAnswersTwo[j] = wrongAnswerTwo;
                wrongAnswersThree[j] = wrongAnswerThree;
            }
            GameActivity.setQuestions(questions);
            GameActivity.setRightAnswers(rightAnswers);
            GameActivity.setWrongAnswersOne(wrongAnswersOne);
            GameActivity.setWrongAnswersTwo(wrongAnswersTwo);
            GameActivity.setWrongAnswersThree(wrongAnswersThree);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void loadPredefinedQuestionsToArrays(Context context, List<Integer> questionNumbers) {
        String simpleJSON = QuizUtils.loadJSONFromAsset(context);
        String[] questions = new String[3];
        String[] rightAnswers = new String[3];
        String[] wrongAnswersOne = new String[3];
        String[] wrongAnswersTwo = new String[3];
        String[] wrongAnswersThree = new String[3];
        try {
            JSONArray jsonArray = new JSONArray(simpleJSON);
            int j;
            for (j = 0; j < 3; j++) {
                JSONObject jsonObject = jsonArray.getJSONObject(questionNumbers.get(j));
                String question = jsonObject.getString("name");
                String rightAnswer = jsonObject.getString("rightAnswer");
                String wrongAnswerOne = jsonObject.getString("wrongAnswerOne");
                String wrongAnswerTwo = jsonObject.getString("wrongAnswerTwo");
                String wrongAnswerThree = jsonObject.getString("wrongAnswerThree");
                questions[j] = question;
                rightAnswers[j] = rightAnswer;
                wrongAnswersOne[j] = wrongAnswerOne;
                wrongAnswersTwo[j] = wrongAnswerTwo;
                wrongAnswersThree[j] = wrongAnswerThree;
            }
            ReplyToChallengeActivity.setQuestions(questions);
            ReplyToChallengeActivity.setRightAnswers(rightAnswers);
            ReplyToChallengeActivity.setWrongAnswersOne(wrongAnswersOne);
            ReplyToChallengeActivity.setWrongAnswersTwo(wrongAnswersTwo);
            ReplyToChallengeActivity.setWrongAnswersThree(wrongAnswersThree);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method for getting the user's current score.
     *
     * @param context The application context.
     * @return The user's current score.
     */
    public static int getCurrentScore(Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return mPreferences.getInt(CURRENT_SCORE_KEY, 0);
    }

    /**
     * Helper method for setting the user's current score.
     *
     * @param context      The application context.
     * @param currentScore The user's current score.
     */
    public static void setCurrentScore(Context context, int currentScore) {
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(CURRENT_SCORE_KEY, currentScore);
        editor.apply();
    }

    /**
     * Helper method for getting the user's current questionNUmber.
     *
     * @param context The application context.
     * @return The user's current questionNumber.
     */
    public static int getCurrentQuestionNumber(Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return mPreferences.getInt(CURRENT_QUESTION_NUMBER_KEY, 0);
    }

    /**
     * Helper method for setting the user's current questionNumber.
     *
     * @param context               The application context.
     * @param currentQuestionNumber The user's current questionNumber.
     */
    public static void setCurrentQuestionNumber(Context context, int currentQuestionNumber) {
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(CURRENT_QUESTION_NUMBER_KEY, currentQuestionNumber);
        editor.apply();
    }


}