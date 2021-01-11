package com.example.simplemath;

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
import java.util.Random;

import static com.example.simplemath.R.color.unserBlau;

public class Multiplayer extends AppCompatActivity implements View.OnClickListener {
    private final Random random = new Random();
    private CountDownTimer cTimer = null;
    private Button startPlayer1, startPlayer2, result1Player1, result2Player1, result3Player1, result4Player1, result1Player2, result2Player2, result3Player2, result4Player2;
    private TextView taskPlayer1, taskPlayer2, scorePlayer1, scorePlayer2, timeLeftPlayer1, timeLeftPlayer2, countdownPlayer1, countdownPlayer2, resultRightOrWrongPlayer1, resultRightOrWrongPlayer2;
    private int missingPartIndex, missingPart;
    private int scoreValuePlayer1 = 0, scoreValuePlayer2 = 0;
    private boolean player1Ready = false, player2Ready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        startPlayer1 = findViewById(R.id.startPlayer1);
        startPlayer1.setOnClickListener(this);
        startPlayer2 = findViewById(R.id.startPlayer2);
        startPlayer2.setOnClickListener(this);

        countdownPlayer1 = findViewById(R.id.countdownSpieler1);
        countdownPlayer2 = findViewById(R.id.countdownSpieler2);
        resultRightOrWrongPlayer1 = findViewById(R.id.ergebnisRichtigOderFalschSpieler1);
        resultRightOrWrongPlayer2 = findViewById(R.id.ergebnisRichtigOderFalschSpieler2);


        result1Player1 = findViewById(R.id.ergebnisButton1Spieler1);
        result1Player1.setOnClickListener(this);
        result2Player1 = findViewById(R.id.ergebnisButton2Spieler1);
        result2Player1.setOnClickListener(this);
        result3Player1 = findViewById(R.id.ergebnisButton3Spieler1);
        result3Player1.setOnClickListener(this);
        result4Player1 = findViewById(R.id.ergebnisButton4Spieler1);
        result4Player1.setOnClickListener(this);

        result1Player2 = findViewById(R.id.ergebnisButton1Spieler2);
        result1Player2.setOnClickListener(this);
        result2Player2 = findViewById(R.id.ergebnisButton2Spieler2);
        result2Player2.setOnClickListener(this);
        result3Player2 = findViewById(R.id.ergebnisButton3Spieler2);
        result3Player2.setOnClickListener(this);
        result4Player2 = findViewById(R.id.ergebnisButton4Spieler2);
        result4Player2.setOnClickListener(this);

        taskPlayer1 = findViewById(R.id.aufgabePlayer1);
        taskPlayer2 = findViewById(R.id.aufgabePlayer2);
        scorePlayer1 = findViewById(R.id.scoreSpieler1);
        scorePlayer2 = findViewById(R.id.scoreSpieler2);
        timeLeftPlayer1 = findViewById(R.id.zeitUebrigSpieler1);
        timeLeftPlayer2 = findViewById(R.id.zeitUebrigSpieler2);

    }

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
            case R.id.ergebnisButton1Spieler1:
                validateButton(result1Player1, 1);
                break;
            case R.id.ergebnisButton2Spieler1:
                validateButton(result2Player1, 1);
                break;
            case R.id.ergebnisButton3Spieler1:
                validateButton(result3Player1, 1);
                break;
            case R.id.ergebnisButton4Spieler1:
                validateButton(result4Player1, 1);
                break;
            case R.id.ergebnisButton1Spieler2:
                validateButton(result1Player2, 2);
                break;
            case R.id.ergebnisButton2Spieler2:
                validateButton(result2Player2, 2);
                break;
            case R.id.ergebnisButton3Spieler2:
                validateButton(result3Player2, 2);
                break;
            case R.id.ergebnisButton4Spieler2:
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

    private void startCountdown() {
        cTimer = new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                countdownPlayer1.setText(String.format("%s", new SimpleDateFormat("s").format(new Date(millisUntilFinished+1000))));
                countdownPlayer2.setText(String.format("%s", new SimpleDateFormat("s").format(new Date(millisUntilFinished+1000))));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cTimer != null) {
            cTimer.cancel();
        }
    }

    public void startGame(int minuten) {
        cTimer = new CountDownTimer(minuten * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftPlayer1.setText(String.format("Zeit: %s", new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished))));
                timeLeftPlayer2.setText(String.format("Zeit: %s", new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished))));
            }

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
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 8000);

            }
        };
        updateViews(generateTask());
        cTimer.start();
    }

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

    public boolean isInArray(int[] array, int number) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == number) {
                return true;
            }
        }
        return false;
    }

    public String taskAsString(int[] aufgabe, int missingPartIndex) {
        StringBuilder ergebnisString = new StringBuilder();
        for (int i = 0; i < aufgabe.length; i++) {
            if (i == aufgabe.length - 1) {
                ergebnisString.append("= ");
            }
            if (i == missingPartIndex) {
                ergebnisString.append("___");
            } else {
                if (i == 1 || i == 3) {
                    ergebnisString.append(mathSymbolFromNumber(aufgabe[i]));
                } else {
                    ergebnisString.append(aufgabe[i]);
                }
            }
            ergebnisString.append(" ");
        }
        return ergebnisString.toString();
    }

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
        handler.postDelayed(new Runnable() {
                                public void run() {
                                    changeResultButtonVisibility(View.VISIBLE);
                                    resultRightOrWrongPlayer1.setVisibility(View.GONE);
                                    resultRightOrWrongPlayer2.setVisibility(View.GONE);
                                    updateViews(generateTask());
                                }
                            }
                , delay);


    }

    public void changeResultButtonVisibility(int value){

        result1Player1.setVisibility(value);
        result2Player1.setVisibility(value);
        result3Player1.setVisibility(value);
        result4Player1.setVisibility(value);
        result1Player2.setVisibility(value);
        result2Player2.setVisibility(value);
        result3Player2.setVisibility(value);
        result4Player2.setVisibility(value);

    }

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


    public void updateViews(int[] task) {
        scorePlayer1.setText(String.format("SCORE: %d", scoreValuePlayer1));
        scorePlayer2.setText(String.format("SCORE: %d", scoreValuePlayer2));

        missingPartIndex = random.nextInt(6);
        int[] buttonValues = new int[4];
        missingPart = task[missingPartIndex];
        if (missingPartIndex == 1 || missingPartIndex == 3) {
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
            int offset = missingPart / 10 * 10;
            int correctAnswerIndex = random.nextInt(4);
            for (int i = 0; i < buttonValues.length; i++) {
                int newNumber;
                do {
                    newNumber = offset + random.nextInt(10);
                } while (isInArray(buttonValues, newNumber) || missingPart == newNumber);
                buttonValues[i] = newNumber;
            }
            buttonValues[correctAnswerIndex] = missingPart;
        } else {
            int correctAnswerIndex = random.nextInt(4);
            for (int i = 0; i < buttonValues.length; i++) {
                int newNumber;
                do {
                    newNumber = 1 + random.nextInt(9);
                } while (isInArray(buttonValues, newNumber) || missingPart == newNumber);
                buttonValues[i] = newNumber;
            }
            buttonValues[correctAnswerIndex] = missingPart;
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