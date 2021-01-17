package com.example.simplemath;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static com.example.simplemath.R.color.unserBlau;

/**
 * This is the Javaclass for the game/activity "Multiplayer".
 *
 * @author Bjarne Küper and Sascha Rührup
 */
public class Multiplayer extends AppCompatActivity implements View.OnClickListener {
    private final Random random = new Random();
    private CountDownTimer cTimer = null;
    private Button startPlayer1, startPlayer2, result1Player1, result2Player1, result3Player1, result4Player1, result1Player2, result2Player2, result3Player2, result4Player2;
    private TextView taskPlayer1, taskPlayer2, scorePlayer1, scorePlayer2, timeLeftPlayer1, timeLeftPlayer2, countdownPlayer1, countdownPlayer2, resultRightOrWrongPlayer1, resultRightOrWrongPlayer2;
    private int missingPartIndex, missingPart;
    private int scoreValuePlayer1 = 0, scoreValuePlayer2 = 0;
    private boolean player1Ready = false, player2Ready = false;

    /**
     * The onCreate method sets the Content and finds the Views in the layout file.
     * sets onClick listeners for all Buttons, implemented by this class.
     *
     * @param savedInstanceState android related
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        startPlayer1 = findViewById(R.id.startPlayer1);
        startPlayer1.setOnClickListener(this);
        startPlayer2 = findViewById(R.id.startPlayer2);
        startPlayer2.setOnClickListener(this);

        countdownPlayer1 = findViewById(R.id.countdownPlayer1);
        countdownPlayer2 = findViewById(R.id.countdownPlayer2);
        resultRightOrWrongPlayer1 = findViewById(R.id.feedbackPlayer1);
        resultRightOrWrongPlayer2 = findViewById(R.id.feedbackPlayer2);


        result1Player1 = findViewById(R.id.option1Player1);
        result1Player1.setOnClickListener(this);
        result2Player1 = findViewById(R.id.option2Player1);
        result2Player1.setOnClickListener(this);
        result3Player1 = findViewById(R.id.option3Player1);
        result3Player1.setOnClickListener(this);
        result4Player1 = findViewById(R.id.option4Player1);
        result4Player1.setOnClickListener(this);

        result1Player2 = findViewById(R.id.option1Player2);
        result1Player2.setOnClickListener(this);
        result2Player2 = findViewById(R.id.option2Player2);
        result2Player2.setOnClickListener(this);
        result3Player2 = findViewById(R.id.option3Player2);
        result3Player2.setOnClickListener(this);
        result4Player2 = findViewById(R.id.option4Player2);
        result4Player2.setOnClickListener(this);

        taskPlayer1 = findViewById(R.id.taskPlayer1);
        taskPlayer2 = findViewById(R.id.taskPlayer2);
        scorePlayer1 = findViewById(R.id.scorePlayer1);
        scorePlayer2 = findViewById(R.id.scorePlayer2);
        timeLeftPlayer1 = findViewById(R.id.timeLeftPlayer1);
        timeLeftPlayer2 = findViewById(R.id.timeLeftPlayer2);

    }

    /**
     * Sets onClick listeners for all Buttons. The Buttons startPlayer1/2 set a boolean to true and change color.
     * When both booleans are true, the game countdown starts, and the start Buttons are set to "GONE".
     * The result Buttons call the validateButton function.
     *
     * @param v View that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startPlayer1:
                player1Ready = true;
                startPlayer1.setBackgroundColor(Color.GREEN);
                break;
            case R.id.startPlayer2:
                player2Ready = true;
                startPlayer2.setBackgroundColor(Color.GREEN);
                break;
            case R.id.option1Player1:
                validateButton(result1Player1, 1);
                break;
            case R.id.option2Player1:
                validateButton(result2Player1, 1);
                break;
            case R.id.option3Player1:
                validateButton(result3Player1, 1);
                break;
            case R.id.option4Player1:
                validateButton(result4Player1, 1);
                break;
            case R.id.option1Player2:
                validateButton(result1Player2, 2);
                break;
            case R.id.option2Player2:
                validateButton(result2Player2, 2);
                break;
            case R.id.option3Player2:
                validateButton(result3Player2, 2);
                break;
            case R.id.option4Player2:
                validateButton(result4Player2, 2);
                break;
        }
        if (player1Ready && player2Ready) {
            player1Ready = false;
            player2Ready = false;
            startPlayer1.setVisibility(View.GONE);
            startPlayer2.setVisibility(View.GONE);
            countdownPlayer1.setVisibility(View.VISIBLE);
            countdownPlayer2.setVisibility(View.VISIBLE);

            startCountdown();
        }
    }

    /**
     * Starts the games countdown and onFinish disables the countdown related views as well as
     * making the Views needed to play the game visible.
     */
    private void startCountdown() {
        cTimer = new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                countdownPlayer1.setText(String.format("%s", new SimpleDateFormat("s", Locale.GERMAN).format(new Date(millisUntilFinished + 1000))));
                countdownPlayer2.setText(String.format("%s", new SimpleDateFormat("s", Locale.GERMAN).format(new Date(millisUntilFinished + 1000))));
            }

            @Override
            public void onFinish() {
                countdownPlayer1.setVisibility(View.GONE);
                countdownPlayer2.setVisibility(View.GONE);
                changeResultButtonVisibility(View.VISIBLE);

                taskPlayer1.setVisibility(View.VISIBLE);
                taskPlayer2.setVisibility(View.VISIBLE);
                scorePlayer1.setVisibility(View.VISIBLE);
                scorePlayer2.setVisibility(View.VISIBLE);
                timeLeftPlayer1.setVisibility(View.VISIBLE);
                timeLeftPlayer2.setVisibility(View.VISIBLE);
                startGame(1);
            }
        };
        cTimer.start();
    }

    /**
     * Frees up system resources for cTimer in case it is not null.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cTimer != null) {
            cTimer.cancel();
        }
    }

    /**
     * Starts a multiplayer game by creating a new countdown and generating the first Task.
     * In onFinish, the game gets evaluated and the players get a message. After a delay, the activity finishes.
     *
     * @param minutes time a game lasts.
     */
    public void startGame(int minutes) {
        cTimer = new CountDownTimer(minutes * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftPlayer1.setText(String.format("Zeit: %s", new SimpleDateFormat("mm:ss", Locale.GERMAN).format(new Date(millisUntilFinished))));
                timeLeftPlayer2.setText(String.format("Zeit: %s", new SimpleDateFormat("mm:ss", Locale.GERMAN).format(new Date(millisUntilFinished))));
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                changeResultButtonVisibility(View.GONE);

                if (scoreValuePlayer1 > scoreValuePlayer2) {
                    taskPlayer1.setText("GEWONNEN");
                    taskPlayer1.setTextColor(Color.GREEN);
                    taskPlayer2.setText("VERLOREN");
                    taskPlayer2.setTextColor(Color.RED);
                } else if (scoreValuePlayer1 < scoreValuePlayer2) {
                    taskPlayer1.setText("VERLOREN");
                    taskPlayer1.setTextColor(Color.RED);
                    taskPlayer2.setText("GEWONNEN");
                    taskPlayer2.setTextColor(Color.GREEN);
                } else {
                    taskPlayer1.setText("UNENTSCHIEDEN");
                    taskPlayer1.setTextColor(getResources().getColor(unserBlau));
                    taskPlayer2.setText("UNENTSCHIEDEN");
                    taskPlayer2.setTextColor(getResources().getColor(unserBlau));
                }
                Handler handler = new Handler();
                int delay = 8000;
                handler.postDelayed(() -> finish(), delay);

            }
        };
        updateViews(generateTask());
        cTimer.start();
    }

    /**
     * Generates a new task. The solution to the task gets calculated and in case it is not
     * in range 1-100, the method calls itself recursively.
     *
     * @return int array containing the task
     */
    public int[] generateTask() {
        int[] aufgabe = new int[6];
        aufgabe[0] = 1 + random.nextInt(9);
        aufgabe[1] = random.nextInt(3);
        aufgabe[2] = 1 + random.nextInt(9);
        aufgabe[3] = random.nextInt(3);
        aufgabe[4] = 1 + random.nextInt(9);
        aufgabe[5] = calculateTask(aufgabe);
        if (aufgabe[5] > 100 || aufgabe[5] < 1) {
            aufgabe = generateTask();
        }
        return aufgabe;
    }

    /**
     * iterates over the array and checks for the number.
     *
     * @param array  to compare against
     * @param number to check
     * @return false if number is not present in array, true otherwise.
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
    public int calculateTask(int[] task) {
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

    public void validateButton(Button button, int playerNumber) {
        if (missingPartIndex == 1 || missingPartIndex == 3) {
            if (button.getText().toString().equals(mathSymbolFromNumber(missingPart))) {
                correctButton(playerNumber);
            } else {
                wrongButton(playerNumber);
            }
        } else {
            if (button.getText().toString().equals(String.valueOf(missingPart))) {
                correctButton(playerNumber);
            } else {
                wrongButton(playerNumber);
            }
        }
        changeResultButtonVisibility(View.GONE);
        long delay = 500;
        Handler handler = new Handler();
        handler.postDelayed(() -> {
                    changeResultButtonVisibility(View.VISIBLE);
                    resultRightOrWrongPlayer1.setVisibility(View.GONE);
                    resultRightOrWrongPlayer2.setVisibility(View.GONE);
                    updateViews(generateTask());
                }
                , delay);


    }

    /**
     * Sets the visibility of the result buttons.
     *
     * @param value Value to set visibility to. View.GONE, View.VISIBLE, View.INVISIBLE .
     */
    public void changeResultButtonVisibility(int value) {

        result1Player1.setVisibility(value);
        result2Player1.setVisibility(value);
        result3Player1.setVisibility(value);
        result4Player1.setVisibility(value);
        result1Player2.setVisibility(value);
        result2Player2.setVisibility(value);
        result3Player2.setVisibility(value);
        result4Player2.setVisibility(value);

    }

    /**
     * Increases the players score and displays messages to both players.
     *
     * @param playerNumber Playernumber that pressed the correct button.
     */
    @SuppressLint("SetTextI18n")
    public void correctButton(int playerNumber) {
        resultRightOrWrongPlayer1.setVisibility(View.VISIBLE);
        resultRightOrWrongPlayer2.setVisibility(View.VISIBLE);
        if (playerNumber == 1) {
            scoreValuePlayer1++;
            resultRightOrWrongPlayer1.setText("RICHTIG");
            resultRightOrWrongPlayer1.setTextColor(Color.GREEN);

            resultRightOrWrongPlayer2.setText("ZU LANGSAM");
            resultRightOrWrongPlayer2.setTextColor(Color.RED);
        } else if (playerNumber == 2) {
            scoreValuePlayer2++;
            resultRightOrWrongPlayer1.setText("ZU LANGSAM");
            resultRightOrWrongPlayer1.setTextColor(Color.RED);

            resultRightOrWrongPlayer2.setText("RICHTIG");
            resultRightOrWrongPlayer2.setTextColor(Color.GREEN);
        }
    }

    /**
     * Decreases the score of the player, that pressed the button and increases
     * the score of the other player.
     * Sets texts to display messages to both players.
     *
     * @param playerNumber Playernumber that pressed the wrong button.
     */
    @SuppressLint("SetTextI18n")
    public void wrongButton(int playerNumber) {
        resultRightOrWrongPlayer1.setVisibility(View.VISIBLE);
        resultRightOrWrongPlayer2.setVisibility(View.VISIBLE);
        if (playerNumber == 1) {
            scoreValuePlayer2++;
            if (scoreValuePlayer1 > 0) {
                scoreValuePlayer1--;
                resultRightOrWrongPlayer1.setText("FALSCH");
                resultRightOrWrongPlayer1.setTextColor(Color.RED);

                resultRightOrWrongPlayer2.setText("GLÜCK GEHABT");
                resultRightOrWrongPlayer2.setTextColor(Color.GREEN);
            }
        } else if (playerNumber == 2) {
            scoreValuePlayer1++;
            if (scoreValuePlayer2 > 0) {
                scoreValuePlayer2--;
                resultRightOrWrongPlayer1.setText("GLÜCK GEHABT");
                resultRightOrWrongPlayer1.setTextColor(Color.GREEN);

                resultRightOrWrongPlayer2.setText("FALSCH");
                resultRightOrWrongPlayer2.setTextColor(Color.RED);

            }
        }
    }

    /**
     * Updates the players scores.
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
        scorePlayer1.setText(String.format(Locale.GERMAN, "SCORE: %d", scoreValuePlayer1));
        scorePlayer2.setText(String.format(Locale.GERMAN, "SCORE: %d", scoreValuePlayer2));

        missingPartIndex = random.nextInt(6);
        int[] buttonValues = new int[4];
        missingPart = task[missingPartIndex];
        if (missingPartIndex == 1 || missingPartIndex == 3) {
            // missing symbol
            result1Player1.setText("+");
            result2Player1.setText("-");
            result3Player1.setText("*");
            result4Player1.setVisibility(View.INVISIBLE);
            result1Player2.setText("+");
            result2Player2.setText("-");
            result3Player2.setText("*");
            result4Player2.setVisibility(View.INVISIBLE);
            taskPlayer1.setText(taskAsString(task, missingPartIndex));
            taskPlayer2.setText(taskAsString(task, missingPartIndex));
            return;

        } else if (missingPartIndex == 5) {
            // missing solution
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
            // missing number in calculation
            for (int i = 0; i < buttonValues.length; i++) {
                int newNumber;
                do {
                    newNumber = 1 + random.nextInt(9);
                } while (isInArray(buttonValues, newNumber) || missingPart == newNumber);
                buttonValues[i] = newNumber;
            }
            buttonValues[random.nextInt(4)] = missingPart;
        }
        result1Player1.setText(String.valueOf(buttonValues[0]));
        result2Player1.setText(String.valueOf(buttonValues[1]));
        result3Player1.setText(String.valueOf(buttonValues[2]));
        result4Player1.setText(String.valueOf(buttonValues[3]));
        result4Player1.setVisibility(View.VISIBLE);
        taskPlayer1.setText(taskAsString(task, missingPartIndex));

        result1Player2.setText(String.valueOf(buttonValues[0]));
        result2Player2.setText(String.valueOf(buttonValues[1]));
        result3Player2.setText(String.valueOf(buttonValues[2]));
        result4Player2.setText(String.valueOf(buttonValues[3]));
        result4Player2.setVisibility(View.VISIBLE);
        taskPlayer2.setText(taskAsString(task, missingPartIndex));
    }
}