package com.example.simplemath;

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
import java.util.Random;

public class ZahlenEinfuegen extends AppCompatActivity implements View.OnClickListener {
    private final Random random = new Random();
    CountDownTimer cTimer = null;
    private int scoreValue = 0;
    private int correctAnswers = 0;
    private int rounds, remainingTasks, missingPart, missingPartIndex, minutes, gameID;
    private boolean highscoreMode;
    private TextView score, time, taskText, taskProgress, resultCorrectOrWrong;
    private Button result1, result2, result3, result4, dontKnow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zahlen_einfuegen);
        score = findViewById(R.id.score);
        time = findViewById(R.id.timeLeft);
        taskText = findViewById(R.id.taskText);
        resultCorrectOrWrong = findViewById(R.id.ergebnisRichtigOderFalsch);
        taskProgress = findViewById(R.id.taskProgress);
        result1 = findViewById(R.id.resultButton1);
        result1.setOnClickListener(this);
        result2 = findViewById(R.id.resultButton2);
        result2.setOnClickListener(this);
        result3 = findViewById(R.id.resultButton3);
        result3.setOnClickListener(this);
        result4 = findViewById(R.id.resultButton4);
        result4.setOnClickListener(this);
        dontKnow = findViewById(R.id.dontKnowButton);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cTimer != null) {
            cTimer.cancel();
        }
    }

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

    public void correctButton() {
        resultCorrectOrWrong.setText("RICHTIG");
        resultCorrectOrWrong.setTextColor(Color.GREEN);
        resultCorrectOrWrong.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                resultCorrectOrWrong.setVisibility(View.GONE);
            }
        }, 2000);
        if (highscoreMode) {
            scoreValue += 1;
        } else {
            remainingTasks -= 1;
            correctAnswers += 1;
        }
    }

    public void wrongButton() {
        resultCorrectOrWrong.setText("FALSCH");
        resultCorrectOrWrong.setTextColor(Color.RED);
        resultCorrectOrWrong.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                resultCorrectOrWrong.setVisibility(View.GONE);
            }
        }, 2000);
        if (highscoreMode) {
            if (scoreValue > 0) {
                scoreValue -= 1;
            }
        } else {
            remainingTasks -= 1;
        }
    }


    public void startHighscoreGame(int minuten) {
        taskProgress.setVisibility(View.GONE);
        updateViews(generateTask());
        cTimer = new CountDownTimer(minuten * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(String.format("Zeit: %s", new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                openEvaluation();
                finish();
            }
        };
        cTimer.start();
    }

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

    public void startFreeGame() {
        remainingTasks = 15;
        updateViews(generateTask());
        score.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
    }

    public int[] generateTask() {
        int[] aufgabe = new int[6];
        aufgabe[0] = 1 + random.nextInt(9);
        aufgabe[1] = random.nextInt(3);
        aufgabe[2] = 1 + random.nextInt(9);
        aufgabe[3] = random.nextInt(3);
        aufgabe[4] = 1 + random.nextInt(9);
        aufgabe[5] = calculateResult(aufgabe);
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

    public void updateViews(int[] aufgabe) {
        if (!highscoreMode) {
            taskProgress.setText(String.format("Aufgabe %d von 15", 16 - remainingTasks));
        } else {
            score.setText(String.format("SCORE: %d", scoreValue));
        }

        missingPartIndex = random.nextInt(6);
        int[] buttonValues = new int[4];
        missingPart = aufgabe[missingPartIndex];
        if (missingPartIndex == 1 || missingPartIndex == 3) {
            result1.setText("+");
            result2.setText("-");
            result3.setText("*");
            result4.setVisibility(View.INVISIBLE);
            taskText.setText(taskAsString(aufgabe, missingPartIndex));
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
        result1.setText(String.valueOf(buttonValues[0]));
        result2.setText(String.valueOf(buttonValues[1]));
        result3.setText(String.valueOf(buttonValues[2]));
        result4.setText(String.valueOf(buttonValues[3]));
        result4.setVisibility(View.VISIBLE);
        taskText.setText(taskAsString(aufgabe, missingPartIndex));
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