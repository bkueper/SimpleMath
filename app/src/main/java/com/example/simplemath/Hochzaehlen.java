package com.example.simplemath;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static java.lang.String.format;

/**
 * This is the Javaclass for the game/activity "Hochzählen".
 *
 * @author Bjarne Küper and Sascha Rührup
 */
public class Hochzaehlen extends AppCompatActivity implements View.OnClickListener {
    private final Random random = new Random();
    private GridLayout gridLayout;
    private final ArrayList<Button> allButtons = new ArrayList<>();
    private int currentPosition, remainingTasks, spielId, rounds, minutes, correctAnswers, firstNumber, secondNumber;
    private int scoreValue = 0;
    private TextView score, time, taskText, taskProgress;
    private boolean highscoreMode;
    private CountDownTimer cTimer;
    private Button confirm;

    /**
     * The onCreate method sets the Content and finds the Views in the layout file.
     * It adds 25 buttons and finds out whether the user started a highscore game or not.
     * The information (duration of game in minutes, the game ID,
     * the amount of times the game has to be played, a boolean that says whether the game is a highscoregame or not)
     * get taken out of the intent, that started the activity.
     *
     * @param savedInstanceState android related
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hochzaehlen);
        score = findViewById(R.id.score);
        time = findViewById(R.id.timeLeft);
        taskText = findViewById(R.id.taskText);
        taskProgress = findViewById(R.id.taskProgress);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < 25; i++) {
            addButton(i);
        }
        Intent intent = getIntent();
        highscoreMode = intent.getBooleanExtra("HIGHSCOREMODE", true);
        minutes = intent.getIntExtra("MINUTES", 1);
        if (highscoreMode) {
            remainingTasks = 1;
            startHighscoreGame(minutes);
            spielId = intent.getIntExtra("SPIELID", 1);
        } else {
            rounds = intent.getIntExtra("DURCHLAEUFE", 1);
            startFreeGame();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cTimer != null) {
            cTimer.cancel();
        }
    }

    /**
     * Starts a Highscore game for given minutes. Updates the views for the first time
     * and starts the counter for the given amount of minutes. The visibility of the
     * View that shows up in "Freies Spiel" gets set to "GONE".
     *
     * @param minutes time the game runs for.
     */
    public void startHighscoreGame(int minutes) {
        remainingTasks = 1;
        updateViews();
        scoreValue = 0;
        taskProgress.setVisibility(View.GONE);
        cTimer = new CountDownTimer(minutes * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(format("Zeit: %s", new SimpleDateFormat("mm:ss", Locale.GERMAN).format(new Date(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                openEvaluation();
                finish();
            }
        };
        cTimer.start();
    }

    /**
     * Starts a game in the gamemode "Freies Spiel" with 15
     * remaining tasks and sets the visibility of the score
     * and time views to "GONE".
     */
    public void startFreeGame() {
        remainingTasks = 15;
        updateViews();
        score.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
    }

    /**
     * Finds out whether the game needs to start another round
     * or needs to be closed.
     *
     * @param requestCode number that gets answered from the other Acitvity.
     * @param resultCode  received code.
     * @param data        intent which is filled with information from the other Activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean anotherRound = false;
        if (requestCode == 1) {
            assert data != null;
            anotherRound = data.getBooleanExtra("WEITERERUNDE", false);
        }
        if (anotherRound) {
            correctAnswers = 0;
            startFreeGame();
        } else {
            finish();
        }
    }

    /**
     * Validates whether the given answer is correct or not.
     */
    public void validate() {
        if (currentPosition + 1 == firstNumber + secondNumber) {
            if (highscoreMode) {
                scoreValue++;
            } else {
                correctAnswers++;
                remainingTasks--;
            }
            correctAnswer();
        } else {
            if (highscoreMode) {
                if (scoreValue > 0) {
                    scoreValue--;
                }
            } else {
                remainingTasks--;
            }
            wrongAnswer();
        }
    }

    /**
     * Colors all buttons green for one second.
     */
    public void correctAnswer() {
        Handler handler = new Handler();
        confirm.setVisibility(View.GONE);
        for (Button button : allButtons) {
            button.setBackgroundResource(R.drawable.round_button_bestaetigt);
        }
        handler.postDelayed(() -> {
            confirm.setVisibility(View.VISIBLE);
            updateViews();
        }, 1000);

    }

    /**
     * Colors all buttons red for one second.
     */
    public void wrongAnswer() {
        Handler handler = new Handler();
        confirm.setVisibility(View.GONE);
        for (Button button : allButtons) {
            button.setBackgroundResource(R.drawable.round_button_falsch);
        }
        handler.postDelayed(() -> {
            confirm.setVisibility(View.VISIBLE);
            updateViews();
        }, 1000);
    }

    /**
     * Updates the score and updates the views for the possible
     * different clicked circles (Buttons).
     */
    public void updateViews() {
        if (!highscoreMode) {
            taskProgress.setText(format(Locale.GERMAN, "Aufgabe %d von %d", (16 - remainingTasks), 15));
        } else {
            score.setText(format(Locale.GERMAN, "SCORE: %d", scoreValue));
        }
        taskText.setText(taskAsString());
        for (int i = 0; i < firstNumber; i++) {
            allButtons.get(i).setBackgroundResource(R.drawable.round_button_bestaetigt);
        }
        for (int i = firstNumber; i < allButtons.size(); i++) {
            allButtons.get(i).setBackgroundResource(R.drawable.round_button);
        }
        currentPosition = firstNumber - 1;
    }

    /**
     * Sets the first and second number for the task and creates
     * a String out of them for the task TextView.
     *
     * @return the task as a string
     */
    public String taskAsString() {
        firstNumber = random.nextInt(21);
        secondNumber = 1 + random.nextInt(25 - firstNumber);
        return format(Locale.GERMAN, "%d + %d", firstNumber, secondNumber);
    }

    /**
     * Puts needed information in the intent and Starts the evaluation Activity.
     */
    public void openEvaluation() {
        Intent intent = new Intent(this, Evaluation.class);
        if (highscoreMode) {
            intent.putExtra("SCOREWERT", scoreValue);
            intent.putExtra("MINUTES", minutes);
            intent.putExtra("HIGHSCOREMODE", true);
            startActivity(intent);
        } else {
            intent.putExtra("PUNKTZAHL", correctAnswers);
            rounds -= 1;
            intent.putExtra("DURCHLAEUFE", rounds);
            intent.putExtra("HIGHSCOREMODE", false);
            startActivityForResult(intent, 1);
        }
    }

    /**
     * Adds a circular button to the grid layout.
     *
     * @param i value for the id, which gets set in the method.
     */
    public void addButton(int i) {
        Button button = new Button(this);
        int width = Math.round(convertDpToPixel(50, getApplicationContext()));
        int height = Math.round(convertDpToPixel(50, getApplicationContext()));
        button.setBackgroundResource(R.drawable.round_button);
        button.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        button.setOnClickListener(this);
        button.setId(i);
        allButtons.add(button);
        gridLayout.addView(button);
    }

    /**
     * Converts a number of dp to a number of pixels.
     *
     * @param dp      number of dp to convert.
     * @param context required to get access to the devices display dimensions.
     * @return dp converted to pixels depending on the devices display dimensions.
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * Describes how to handle the different onClick events from the Buttons.
     *
     * @param v View that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm) {
            validate();
            if (!highscoreMode) {
                if (remainingTasks == 0) {
                    openEvaluation();
                }
            }
        } else {
            int position = v.getId();
            if (position < currentPosition) {
                if (position > firstNumber - 2) {
                    for (int i = position + 1; i <= currentPosition; i++) {
                        allButtons.get(i).setBackgroundResource(R.drawable.round_button);
                    }
                }
            } else if (position > currentPosition) {
                if (position > firstNumber - 1) {
                    for (int i = firstNumber; i <= position; i++) {
                        allButtons.get(i).setBackgroundResource(R.drawable.round_button_gefaerbt);
                    }
                }
            }
            currentPosition = position;
        }
    }
}