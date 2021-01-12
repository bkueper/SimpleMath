package com.example.simplemath;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static java.lang.String.format;
import static java.lang.String.valueOf;


/**
 * This is the Javaclass for the game/activity "Größer Kleiner".
 * @author Bjarne Küper and Sascha Rührup
 *
 */
public class GroesserKleiner extends AppCompatActivity implements View.OnTouchListener {
    private final Random random = new Random();
    private Button result1, result2, result3, result4, confirm;
    private boolean highscoreMode;
    private boolean firstRound = true;
    private int remainingTasks, scoreValue, rounds, sortingType, correctAnswers, minutes;
    private TextView sortingTask, largerSmallerSymbol1, largerSmallerSymbol2, largerSmallerSymbol3, score, taskProgress, time;
    private View solution1, solution2, solution3, solution4;
    private float dX, dY;
    private final Rect normalRect = new Rect();
    private final int[] location = new int[2];
    private Button[] occupiedSpace = new Button[4];
    private float[] startingPositions;
    private CountDownTimer cTimer;

    /**
     * The onCreate method sets the Content and finds the Views in the layout file.
     * It retrieves information about gamemode and game duration or number of rounds respectively.
     * Finally a Highscore or Freeplay game is started.
     * @param savedInstanceState android related
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groesser_kleiner);
        result1 = findViewById(R.id.groesserKleinerErgebnis1);
        result1.setOnTouchListener(this);
        result2 = findViewById(R.id.groesserKleinerErgebnis2);
        result2.setOnTouchListener(this);
        result3 = findViewById(R.id.groesserKleinerErgebnis3);
        result3.setOnTouchListener(this);
        result4 = findViewById(R.id.groesserKleinerErgebnis4);
        result4.setOnTouchListener(this);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(v -> {
            validateResult();
            if (remainingTasks <= 0) {
                openEvaluation();
            } else {
                updateViews();
            }
        });
        sortingTask = findViewById(R.id.sortierAufgabe);
        largerSmallerSymbol1 = findViewById(R.id.groesserKleinerZeichen1);
        largerSmallerSymbol2 = findViewById(R.id.groesserKleinerZeichen2);
        largerSmallerSymbol3 = findViewById(R.id.groesserKleinerZeichen3);
        taskProgress = findViewById(R.id.taskProgress);
        score = findViewById(R.id.score);
        time = findViewById(R.id.timeLeft);
        solution1 = findViewById(R.id.solutionSpace1);
        solution2 = findViewById(R.id.solutionSpace2);
        solution3 = findViewById(R.id.solutionSpace3);
        solution4 = findViewById(R.id.solutionSpace4);

        Intent intent = getIntent();
        highscoreMode = intent.getBooleanExtra("HIGHSCOREMODE", true);
        minutes = intent.getIntExtra("MINUTES", 1);
        if (highscoreMode) {
            remainingTasks = 1;
            startHighscoreGame(minutes);
        } else {
            rounds = intent.getIntExtra("DURCHLAEUFE", 1);
            startFreeplayGame();
        }
    }

    /**
     * cTimer gets released to free up system resources.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cTimer != null) {
            cTimer.cancel();
        }
    }

    /**
     * Sets required parameters remainingTasks and scoreValue.
     * Starts a timer for the Highscore game and opens the evaluation after it runs out.
     * @param minutes time the Highscore game will last in minutes.
     */
    public void startHighscoreGame(int minutes) {
        remainingTasks = 1;
        updateViews();
        scoreValue = 0;
        taskProgress.setVisibility(View.GONE);
        cTimer = new CountDownTimer(minutes * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText("Zeit: " + new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
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
     * Sets remainingTasks and disables Views related to Highscore mode only.
     */
    public void startFreeplayGame() {
        remainingTasks = 15;
        updateViews();
        score.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
    }

    /**
     * Checks position of selected View and wether it is inside specified area.
     * If the view is inside it, the View gets placed at a preset position.
     * @param v current View.
     * @param event current MotionEvent.
     * @return consumed the event.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (firstRound) {
            startingPositions = new float[]{result1.getX(), result1.getY(),
                    result2.getX(), result2.getY(), result3.getX(),
                    result3.getY(), result4.getX(), result4.getY()};
        }
        firstRound = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = v.getX() - event.getRawX();
                dY = v.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                v.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
                break;
            case MotionEvent.ACTION_UP:
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                if (isViewInBounds(solution1, x, y)) {
                    occupiedSpace[0] = (Button) v;
                    solution1.dispatchTouchEvent(event);
                    v.setX(solution1.getX() - 20);
                    v.setY(solution1.getY() + 60);
                }
                if (isViewInBounds(solution2, x, y)) {
                    occupiedSpace[1] = (Button) v;
                    solution2.dispatchTouchEvent(event);
                    v.setX(solution2.getX() - 20);
                    v.setY(solution2.getY() + 60);
                }
                if (isViewInBounds(solution3, x, y)) {
                    occupiedSpace[2] = (Button) v;
                    solution3.dispatchTouchEvent(event);
                    v.setX(solution3.getX() - 20);
                    v.setY(solution3.getY() + 60);
                }
                if (isViewInBounds(solution4, x, y)) {
                    occupiedSpace[3] = (Button) v;
                    solution4.dispatchTouchEvent(event);
                    v.setX(solution4.getX() - 20);
                    v.setY(solution4.getY() + 60);
                }
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Starts another Freeplay game depending on the received data or ends this Activitiy.
     * @param requestCode number that gets answered from the other Acitvity.
     * @param resultCode received code.
     * @param data intent which is filled with information from the other Activity.
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
            startFreeplayGame();
        } else {
            finish();
        }
    }

    /**
     * Evaluates the task and increments or decreases scores and remainingTasks accordingly.
     */
    public void validateResult() {
        if (!highscoreMode) {
            remainingTasks--;
        }
        for (int i = 0; i < 4; i++) {
            if (occupiedSpace[i] == null) {
                if (scoreValue > 0) {
                    scoreValue--;
                }
                return;
            }
        }
        if (sortingType == 0) {
            for (int i = 0; i < 3; i++) {
                if (Integer.parseInt(occupiedSpace[i].getText().toString()) <= Integer.parseInt(occupiedSpace[i + 1].getText().toString())) {
                    if (scoreValue > 0) {
                        scoreValue--;
                    }
                    return;
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                if (Integer.parseInt(occupiedSpace[i].getText().toString()) >= Integer.parseInt(occupiedSpace[i + 1].getText().toString())) {
                    if (scoreValue > 0) {
                        scoreValue--;
                    }
                    return;
                }
            }
        }
        if (highscoreMode) {
            scoreValue++;
        }
        correctAnswers++;
    }

    /**
     * updates game progress indicator or score and generates a new round as well as resetting the playing field.
     */
    public void updateViews() {
        if (!highscoreMode) {
            taskProgress.setText(format("Aufgabe %d von %d", (16 - remainingTasks), 15));
        } else {
            score.setText(format("SCORE: %d", scoreValue));
        }
        sortingType = random.nextInt(2);
        if (sortingType == 0) {
            sortingTask.setText("Sortiere von groß nach klein");
            largerSmallerSymbol1.setText(">");
            largerSmallerSymbol2.setText(">");
            largerSmallerSymbol3.setText(">");
        } else {
            sortingTask.setText("Sortiere von klein nach groß");
            largerSmallerSymbol1.setText("<");
            largerSmallerSymbol2.setText("<");
            largerSmallerSymbol3.setText("<");
        }
        int[] buttonValues = new int[4];
        for (int i = 0; i < 4; i++) {
            boolean alreadyExistent;
            int randomNumber;
            do {
                alreadyExistent = false;
                randomNumber = random.nextInt(100);
                for (int j = 0; j < 4; j++) {
                    if (buttonValues[j] == randomNumber) {
                        alreadyExistent = true;
                        break;
                    }
                }
            } while (alreadyExistent);
            buttonValues[i] = randomNumber;
        }
        result1.setText(valueOf(buttonValues[0]));
        result2.setText(valueOf(buttonValues[1]));
        result3.setText(valueOf(buttonValues[2]));
        result4.setText(valueOf(buttonValues[3]));
        occupiedSpace = new Button[4];
        if (!firstRound) {
            result1.setX(startingPositions[0]);
            result1.setY(startingPositions[1]);

            result2.setX(startingPositions[2]);
            result2.setY(startingPositions[3]);

            result3.setX(startingPositions[4]);
            result3.setY(startingPositions[5]);

            result4.setX(startingPositions[6]);
            result4.setY(startingPositions[7]);
        }
    }

    /**
     * checks if the given view contains the x/y coordinates.
     * @param view View to check coordinate against.
     * @param x coordinate
     * @param y coordinate
     * @return true if View contains the coordinates, false otherwise.
     */
    private boolean isViewInBounds(View view, int x, int y) {
        view.getDrawingRect(normalRect);
        view.getLocationOnScreen(location);
        normalRect.offset(location[0], location[1]);
        return normalRect.contains(x, y);
    }

    /**
     * starts the evaluation activity and adds needed information to the intent.
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
}