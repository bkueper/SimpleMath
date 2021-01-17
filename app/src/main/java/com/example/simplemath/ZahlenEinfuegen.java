package com.example.simplemath;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * This is the Javaclass for the game/activity "Zahlen Einfügen".
 *
 * @author Bjarne Küper and Sascha Rührup
 */
public class ZahlenEinfuegen extends AppCompatActivity implements View.OnClickListener {
    private final Random random = new Random();
    private CountDownTimer cTimer = null;
    private int scoreValue = 0;
    private int correctAnswers = 0;
    private int rounds, remainingTasks, missingPart, missingPartIndex, minutes, gameID;
    private boolean highscoreMode;
    private TextView score, time, taskText, taskProgress, resultCorrectOrWrong;
    private Button result1;
    private Button result2;
    private Button result3;
    private Button result4;

    /**
     * The onCreate method sets the Content and finds the Views in the layout file.
     * Gets additional information about the game to start from the intent and finally starts a new game.
     *
     * @param savedInstanceState android related
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zahlen_einfuegen);
        score = findViewById(R.id.score);
        time = findViewById(R.id.timeLeft);
        taskText = findViewById(R.id.taskText);
        resultCorrectOrWrong = findViewById(R.id.feedback);
        taskProgress = findViewById(R.id.taskProgress);
        result1 = findViewById(R.id.resultButton1);
        result1.setOnClickListener(this);
        result2 = findViewById(R.id.resultButton2);
        result2.setOnClickListener(this);
        result3 = findViewById(R.id.resultButton3);
        result3.setOnClickListener(this);
        result4 = findViewById(R.id.resultButton4);
        result4.setOnClickListener(this);
        Button dontKnow = findViewById(R.id.dontKnowButton);
        dontKnow.setOnClickListener(this);
        Intent intent = getIntent();
        highscoreMode = intent.getBooleanExtra("HIGHSCOREMODE", true);
        minutes = intent.getIntExtra("MINUTES", 1);
        if (highscoreMode) {
            remainingTasks = 1;
            startHighscoreGame(minutes);
            gameID = intent.getIntExtra("SPIELID", 1);
        } else {
            rounds = intent.getIntExtra("DURCHLAEUFE", 1);
            startFreeGame();
        }
    }

    /**
     * releases the cTimer, if not null to free up system resources.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cTimer != null) {
            cTimer.cancel();
        }
    }

    /**
     * Depending on the button clicked, either validates the button or in case of the "dont know" button,
     * opens the evaluation or generates a new task.
     *
     * @param v View clicked on.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resultButton1:
                validateButton(result1);
                break;
            case R.id.resultButton2:
                validateButton(result2);
                break;
            case R.id.resultButton3:
                validateButton(result3);
                break;
            case R.id.resultButton4:
                validateButton(result4);
                break;
            case R.id.dontKnowButton:
                if (!highscoreMode) {
                    remainingTasks -= 1;
                }
                if (remainingTasks <= 0) {
                    openEvaluation();
                } else {
                    updateViews(generateTask());
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    /**
     * Receives a button to validate. Compares the buttons' text to the correct solution and calls
     * correctButton or wrongButton accordingly. Finally it opens the evaluation or generates a new Task.
     *
     * @param button clicked, that gets validated.
     */
    public void validateButton(Button button) {
        if (missingPartIndex == 1 || missingPartIndex == 3) {
            if (button.getText().toString().equals(mathSymbolFromNumber(missingPart))) {
                correctButton();
            } else {
                wrongButton();
            }
        } else {
            if (button.getText().toString().equals(String.valueOf(missingPart))) {
                correctButton();
            } else {
                wrongButton();
            }
        }
        if (remainingTasks == 0) {
            openEvaluation();
        } else {
            updateViews(generateTask());
        }
    }

    /**
     * In case of a "Freies Spiel" game, this method retrieves a boolean from the intent to start a new
     * "Freies Spiel" game, if necessary.
     *
     * @param requestCode start code
     * @param resultCode  received code
     * @param data        intent sent back
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
     * Displays a TextView to indicate the press of the correct button, that stays visible for set delay.
     * Increases scores and decreases remainingTasks accordingly.
     */
    @SuppressLint("SetTextI18n")
    public void correctButton() {
        resultCorrectOrWrong.setText("RICHTIG");
        resultCorrectOrWrong.setTextColor(Color.GREEN);
        resultCorrectOrWrong.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        int delay = 2000;
        handler.postDelayed(() -> resultCorrectOrWrong.setVisibility(View.GONE), delay);
        if (highscoreMode) {
            scoreValue += 1;
        } else {
            remainingTasks -= 1;
            correctAnswers += 1;
        }
    }

    /**
     * Displays a TextView to indicate the press of a wrong button, that stays visible for set delay.
     * Decreases scores and remainingTasks accordingly.
     */
    @SuppressLint("SetTextI18n")
    public void wrongButton() {
        resultCorrectOrWrong.setText("FALSCH");
        resultCorrectOrWrong.setTextColor(Color.RED);
        resultCorrectOrWrong.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        int delay = 2000;
        handler.postDelayed(() -> resultCorrectOrWrong.setVisibility(View.GONE), delay);
        if (highscoreMode) {
            if (scoreValue > 0) {
                scoreValue -= 1;
            }
        } else {
            remainingTasks -= 1;
        }
    }

    /**
     * Generates a new Task, starts a timer with given minutes and opens the evaluation and
     * finishes this activity, after the timer runs out.
     *
     * @param minutes time to play
     */
    public void startHighscoreGame(int minutes) {
        taskProgress.setVisibility(View.GONE);
        updateViews(generateTask());
        cTimer = new CountDownTimer(minutes * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(String.format("Zeit: %s", new SimpleDateFormat("mm:ss", Locale.GERMAN).format(new Date(millisUntilFinished))));
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
     * opens the activity Evaluation.
     * Depending on the gamemode, adds needed information to the intent
     */
    public void openEvaluation() {
        Intent intent = new Intent(this, Evaluation.class);
        if (highscoreMode) {

            intent.putExtra("SCOREWERT", scoreValue);
            intent.putExtra("MINUTES", minutes);
            intent.putExtra("HIGHSCOREMODE", true);

            intent.putExtra("SPIELID", gameID);

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
     * sets remainingTasks, generates a new Task and disables Views related to Highscore mode.
     */
    public void startFreeGame() {
        remainingTasks = 15;
        updateViews(generateTask());
        score.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
    }

    /**
     * Generates a new task. The solution to the task gets calculated and in case it is not
     * in range 1-100, the method calls itself recursively.
     *
     * @return int array containing the task.
     */
    public int[] generateTask() {
        int[] task = new int[6];
        task[0] = 1 + random.nextInt(9);
        task[1] = random.nextInt(3);
        task[2] = 1 + random.nextInt(9);
        task[3] = random.nextInt(3);
        task[4] = 1 + random.nextInt(9);
        task[5] = calculateResult(task);
        if (task[5] > 100 || task[5] < 1) {
            task = generateTask();
        }
        return task;
    }

    /**
     * iterates over the array and checks for the number.
     *
     * @param array  to compare against
     * @param number to check
     * @return false if number is not present in array, true otherwise
     */
    public boolean isInArray(int[] array, int number) {
        for (int value : array) {
            if (value == number) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates progress or score.
     * Chooses a random part of the given task as missing. In case it is a symbol, the buttons' texts
     * are set to the possible symbols and the displayed task is updated.
     * In case the missing part is the result of the task, the buttons receive random numbers calculated by
     * rounding the correct answer down to the lower multiple of ten and adding a random number between
     * 0 and 9. Finally the correct answer replaces one of the generated numbers and all buttons are set.
     * Otherwise the buttons get a random value from 1 to 9 and the correct answer is placed after that and
     * all buttons are set.
     *
     * @param task to update views with.
     */
    public void updateViews(int[] task) {
        if (!highscoreMode) {
            taskProgress.setText(String.format(Locale.GERMAN, "Aufgabe %d von 15", 16 - remainingTasks));
        } else {
            score.setText(String.format(Locale.GERMAN, "SCORE: %d", scoreValue));
        }

        missingPartIndex = random.nextInt(6);
        int[] buttonValues = new int[4];
        missingPart = task[missingPartIndex];
        if (missingPartIndex == 1 || missingPartIndex == 3) {
            // missing part is a symbol
            result1.setText("+");
            result2.setText("-");
            result3.setText("*");
            result4.setVisibility(View.INVISIBLE);
            taskText.setText(taskAsString(task, missingPartIndex));
            return;

        } else if (missingPartIndex == 5) {
            // missing part is the result
            int offset = missingPart / 10 * 10;
            for (int i = 0; i < buttonValues.length; i++) {
                int newNumber;
                do {
                    newNumber = offset + random.nextInt(10);
                } while (isInArray(buttonValues, newNumber) || missingPart == newNumber);
                buttonValues[i] = newNumber;
            }
            buttonValues[random.nextInt(4)] = missingPart;
        } else {
            // missing part is a number from the calculation
            for (int i = 0; i < buttonValues.length; i++) {
                int newNumber;
                do {
                    newNumber = 1 + random.nextInt(9);
                } while (isInArray(buttonValues, newNumber) || missingPart == newNumber);
                buttonValues[i] = newNumber;
            }
            buttonValues[random.nextInt(4)] = missingPart;
        }

        result1.setText(String.valueOf(buttonValues[0]));
        result2.setText(String.valueOf(buttonValues[1]));
        result3.setText(String.valueOf(buttonValues[2]));
        result4.setText(String.valueOf(buttonValues[3]));
        result4.setVisibility(View.VISIBLE);
        taskText.setText(taskAsString(task, missingPartIndex));
    }

    /**
     * Converts a given task with a missing part to a custom String.
     *
     * @param task             task to convert
     * @param missingPartIndex index of the missing part of the task
     * @return task as custom String
     */
    public String taskAsString(int[] task, int missingPartIndex) {
        StringBuilder resultString = new StringBuilder();
        for (int i = 0; i < task.length; i++) {
            if (i == task.length - 1) {
                resultString.append("= ");
            }
            if (i == missingPartIndex) {
                resultString.append("___");
            } else {
                if (i == 1 || i == 3) {
                    resultString.append(mathSymbolFromNumber(task[i]));
                } else {
                    resultString.append(task[i]);
                }
            }
            resultString.append(" ");
        }
        return resultString.toString();
    }

    /**
     * Converts a number to String with the following pattern: 1:+, 2:-, 3:*
     *
     * @param number of the symbol to convert
     * @return symbol as String
     */
    public String mathSymbolFromNumber(int number) {
        String mathSymbol = " ";
        switch (number) {
            case 0:
                mathSymbol = "+";
                break;
            case 1:
                mathSymbol = "-";
                break;
            case 2:
                mathSymbol = "*";
                break;
            default:
                break;
        }
        return mathSymbol;
    }

    /**
     * Calculates the given task and returns the solution
     *
     * @param task to calculate the result for
     * @return int solution of the task
     */
    public int calculateResult(int[] task) {
        int result = 0;
        int index = task[1] + 3 * task[3];
        switch (index) {
            case 0:
                result = task[0] + task[2] + task[4];
                break;
            case 1:
                result = task[0] - task[2] + task[4];
                break;
            case 2:
                result = task[0] * task[2] + task[4];
                break;
            case 3:
                result = task[0] + task[2] - task[4];
                break;
            case 4:
                result = task[0] - task[2] - task[4];
                break;
            case 5:
                result = task[0] * task[2] - task[4];
                break;
            case 6:
                result = task[0] + task[2] * task[4];
                break;
            case 7:
                result = task[0] - task[2] * task[4];
                break;
            case 8:
                result = task[0] * task[2] * task[4];
                break;
            default:
                Toast.makeText(this, "FEHLER bei ergebnisAusrechnen", Toast.LENGTH_SHORT).show();
                break;
        }
        return result;
    }

}