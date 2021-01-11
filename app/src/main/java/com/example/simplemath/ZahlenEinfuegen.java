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
    private int scoreWert = 0;
    private int correctAnswers = 0;
    private int durchlaeufe, restlicheAufgaben, missingPart, missingPartIndex, minuten, spielId;
    private boolean highscoreMode;
    private TextView score, zeit, aufgabeText, aufgabenFortschritt, ergebnisRichtigOderFalsch;
    private Button ergebnis1, ergebnis2, ergebnis3, ergebnis4, weissIchNicht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zahlen_einfuegen);
        score = findViewById(R.id.score);
        zeit = findViewById(R.id.zeitUebrig);
        aufgabeText = findViewById(R.id.aufgabe);
        ergebnisRichtigOderFalsch = findViewById(R.id.ergebnisRichtigOderFalsch);
        aufgabenFortschritt = findViewById(R.id.aufgabenFortschritt);
        ergebnis1 = findViewById(R.id.ergebnisButton1);
        ergebnis1.setOnClickListener(this);
        ergebnis2 = findViewById(R.id.ergebnisButton2);
        ergebnis2.setOnClickListener(this);
        ergebnis3 = findViewById(R.id.ergebnisButton3);
        ergebnis3.setOnClickListener(this);
        ergebnis4 = findViewById(R.id.ergebnisButton4);
        ergebnis4.setOnClickListener(this);
        weissIchNicht = findViewById(R.id.weißIchNichtButton);
        weissIchNicht.setOnClickListener(this);
        Intent intent = getIntent();
        highscoreMode = intent.getBooleanExtra("HIGHSCOREMODE", true);
        minuten = intent.getIntExtra("MINUTES", 1);
        if (highscoreMode) {
            restlicheAufgaben = 1;
            startHighscoreGame(minuten);
            spielId = intent.getIntExtra("SPIELID", 1);
        } else {
            durchlaeufe = intent.getIntExtra("DURCHLAEUFE", 1);
            startFreiesSpiel();
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
            case R.id.ergebnisButton1:
                validateButton(ergebnis1);
                break;
            case R.id.ergebnisButton2:
                validateButton(ergebnis2);
                break;
            case R.id.ergebnisButton3:
                validateButton(ergebnis3);
                break;
            case R.id.ergebnisButton4:
                validateButton(ergebnis4);
                break;
            case R.id.weißIchNichtButton:
                if (!highscoreMode) {
                    restlicheAufgaben -= 1;
                }
                if (restlicheAufgaben <= 0) {
                    openAuswertung();
                } else {
                    updateViews(aufgabeGenerieren());
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    public void validateButton(Button button) {
        if (missingPartIndex == 1 || missingPartIndex == 3) {
            if (button.getText().toString().equals(zeichenAusZahlenwert(missingPart))) {
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
        if (restlicheAufgaben == 0) {
            openAuswertung();
        } else {
            updateViews(aufgabeGenerieren());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean weitereRunde = false;
        if (requestCode == 1) {
            assert data != null;
            weitereRunde = data.getBooleanExtra("WEITERERUNDE", false);
        }
        if (weitereRunde) {
            correctAnswers = 0;
            startFreiesSpiel();
        } else {
            finish();
        }
    }

    public void correctButton() {
        ergebnisRichtigOderFalsch.setText("RICHTIG");
        ergebnisRichtigOderFalsch.setTextColor(Color.GREEN);
        ergebnisRichtigOderFalsch.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ergebnisRichtigOderFalsch.setVisibility(View.GONE);
            }
        }, 2000);
        if (highscoreMode) {
            scoreWert += 1;
        } else {
            restlicheAufgaben -= 1;
            correctAnswers += 1;
        }
    }

    public void wrongButton() {
        ergebnisRichtigOderFalsch.setText("FALSCH");
        ergebnisRichtigOderFalsch.setTextColor(Color.RED);
        ergebnisRichtigOderFalsch.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ergebnisRichtigOderFalsch.setVisibility(View.GONE);
            }
        }, 2000);
        if (highscoreMode) {
            if (scoreWert > 0) {
                scoreWert -= 1;
            }
        } else {
            restlicheAufgaben -= 1;
        }
    }


    public void startHighscoreGame(int minuten) {
        aufgabenFortschritt.setVisibility(View.GONE);
        updateViews(aufgabeGenerieren());
        cTimer = new CountDownTimer(minuten * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                zeit.setText(String.format("Zeit: %s", new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                openAuswertung();
                finish();
            }
        };
        cTimer.start();
    }

    public void openAuswertung() {
        if (highscoreMode) {

            Intent intent = new Intent(this, AuswertungZahlenEinfuegen.class);
            intent.putExtra("SCOREWERT", scoreWert);
            intent.putExtra("HIGHSCOREMODE", true);

            intent.putExtra("SPIELID", spielId);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, AuswertungZahlenEinfuegen.class);
            intent.putExtra("PUNKTZAHL", correctAnswers);
            durchlaeufe -= 1;
            intent.putExtra("DURCHLAEUFE", durchlaeufe);
            intent.putExtra("HIGHSCOREMODE", false);
            startActivityForResult(intent, 1);
        }
    }

    public void startFreiesSpiel() {
        restlicheAufgaben = 15;
        updateViews(aufgabeGenerieren());
        score.setVisibility(View.GONE);
        zeit.setVisibility(View.GONE);
    }

    public int[] aufgabeGenerieren() {
        int[] aufgabe = new int[6];
        aufgabe[0] = 1 + random.nextInt(9);
        aufgabe[1] = random.nextInt(3);
        aufgabe[2] = 1 + random.nextInt(9);
        aufgabe[3] = random.nextInt(3);
        aufgabe[4] = 1 + random.nextInt(9);
        aufgabe[5] = ergebnisAusrechnen(aufgabe);
        if (aufgabe[5] > 100 || aufgabe[5] < 1) {
            aufgabe = aufgabeGenerieren();
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
            aufgabenFortschritt.setText(String.format("Aufgabe %d von 15", 16 - restlicheAufgaben));
        } else {
            score.setText(String.format("SCORE: %d", scoreWert));
        }

        missingPartIndex = random.nextInt(6);
        int[] buttonValues = new int[4];
        missingPart = aufgabe[missingPartIndex];
        if (missingPartIndex == 1 || missingPartIndex == 3) {
            ergebnis1.setText("+");
            ergebnis2.setText("-");
            ergebnis3.setText("*");
            ergebnis4.setVisibility(View.INVISIBLE);
            aufgabeText.setText(aufgabeAlsString(aufgabe, missingPartIndex));
            return;
        } else if (missingPartIndex == 5) {
            int offset = missingPart / 10 * 10;
            Toast.makeText(this, "offset: " + String.valueOf(offset), Toast.LENGTH_SHORT).show();
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
        ergebnis1.setText(String.valueOf(buttonValues[0]));
        ergebnis2.setText(String.valueOf(buttonValues[1]));
        ergebnis3.setText(String.valueOf(buttonValues[2]));
        ergebnis4.setText(String.valueOf(buttonValues[3]));
        ergebnis4.setVisibility(View.VISIBLE);
        aufgabeText.setText(aufgabeAlsString(aufgabe, missingPartIndex));
    }

    public String aufgabeAlsString(int[] aufgabe, int missingPartIndex) {
        StringBuilder ergebnisString = new StringBuilder();
        for (int i = 0; i < aufgabe.length; i++) {
            if (i == aufgabe.length - 1) {
                ergebnisString.append("= ");
            }
            if (i == missingPartIndex) {
                ergebnisString.append("___");
            } else {
                if (i == 1 || i == 3) {
                    ergebnisString.append(zeichenAusZahlenwert(aufgabe[i]));
                } else {
                    ergebnisString.append(aufgabe[i]);
                }
            }
            ergebnisString.append(" ");
        }
        return ergebnisString.toString();
    }

    public String zeichenAusZahlenwert(int zahlenwert) {
        String zeichen = " ";
        switch (zahlenwert) {
            case 0:
                zeichen = "+";
                break;
            case 1:
                zeichen = "-";
                break;
            case 2:
                zeichen = "*";
                break;
            default:
                break;
        }
        return zeichen;
    }

    public int ergebnisAusrechnen(int[] aufgabe) {
        int ergebnis = 0;
        int index = aufgabe[1] + 3 * aufgabe[3];
        switch (index) {
            case 0:
                ergebnis = aufgabe[0] + aufgabe[2] + aufgabe[4];
                break;
            case 1:
                ergebnis = aufgabe[0] - aufgabe[2] + aufgabe[4];
                break;
            case 2:
                ergebnis = aufgabe[0] * aufgabe[2] + aufgabe[4];
                break;
            case 3:
                ergebnis = aufgabe[0] + aufgabe[2] - aufgabe[4];
                break;
            case 4:
                ergebnis = aufgabe[0] - aufgabe[2] - aufgabe[4];
                break;
            case 5:
                ergebnis = aufgabe[0] * aufgabe[2] - aufgabe[4];
                break;
            case 6:
                ergebnis = aufgabe[0] + aufgabe[2] * aufgabe[4];
                break;
            case 7:
                ergebnis = aufgabe[0] - aufgabe[2] * aufgabe[4];
                break;
            case 8:
                ergebnis = aufgabe[0] * aufgabe[2] * aufgabe[4];
                break;
            default:
                Toast.makeText(this, "FEHLER bei ergebnisAusrechnen", Toast.LENGTH_SHORT).show();
                break;
        }
        return ergebnis;
    }

}