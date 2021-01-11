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
    private Button startPlayer1, startPlayer2, ergebnis1Spieler1, ergebnis2Spieler1, ergebnis3Spieler1, ergebnis4Spieler1, ergebnis1Spieler2, ergebnis2Spieler2, ergebnis3Spieler2, ergebnis4Spieler2;
    private TextView aufgabePlayer1, aufgabePlayer2, scoreSpieler1, scoreSpieler2, zeitUebrigSpieler1, zeitUebrigSpieler2, countdownSpieler1, countdownSpieler2, ergebnisRichtigOderFalschSpieler1, ergebnisRichtigOderFalschSpieler2;
    private int missingPartIndex, missingPart;
    private int scoreWertSpieler1 = 0, scoreWertSpieler2 = 0;
    private boolean player1Ready = false, player2Ready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        startPlayer1 = findViewById(R.id.startPlayer1);
        startPlayer1.setOnClickListener(this);
        startPlayer2 = findViewById(R.id.startPlayer2);
        startPlayer2.setOnClickListener(this);

        countdownSpieler1 = findViewById(R.id.countdownSpieler1);
        countdownSpieler2 = findViewById(R.id.countdownSpieler2);
        ergebnisRichtigOderFalschSpieler1 = findViewById(R.id.ergebnisRichtigOderFalschSpieler1);
        ergebnisRichtigOderFalschSpieler2 = findViewById(R.id.ergebnisRichtigOderFalschSpieler2);


        ergebnis1Spieler1 = findViewById(R.id.ergebnisButton1Spieler1);
        ergebnis1Spieler1.setOnClickListener(this);
        ergebnis2Spieler1 = findViewById(R.id.ergebnisButton2Spieler1);
        ergebnis2Spieler1.setOnClickListener(this);
        ergebnis3Spieler1 = findViewById(R.id.ergebnisButton3Spieler1);
        ergebnis3Spieler1.setOnClickListener(this);
        ergebnis4Spieler1 = findViewById(R.id.ergebnisButton4Spieler1);
        ergebnis4Spieler1.setOnClickListener(this);

        ergebnis1Spieler2 = findViewById(R.id.ergebnisButton1Spieler2);
        ergebnis1Spieler2.setOnClickListener(this);
        ergebnis2Spieler2 = findViewById(R.id.ergebnisButton2Spieler2);
        ergebnis2Spieler2.setOnClickListener(this);
        ergebnis3Spieler2 = findViewById(R.id.ergebnisButton3Spieler2);
        ergebnis3Spieler2.setOnClickListener(this);
        ergebnis4Spieler2 = findViewById(R.id.ergebnisButton4Spieler2);
        ergebnis4Spieler2.setOnClickListener(this);

        aufgabePlayer1 = findViewById(R.id.aufgabePlayer1);
        aufgabePlayer2 = findViewById(R.id.aufgabePlayer2);
        scoreSpieler1 = findViewById(R.id.scoreSpieler1);
        scoreSpieler2 = findViewById(R.id.scoreSpieler2);
        zeitUebrigSpieler1 = findViewById(R.id.zeitUebrigSpieler1);
        zeitUebrigSpieler2 = findViewById(R.id.zeitUebrigSpieler2);

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
                validateButton(ergebnis1Spieler1, 1);
                break;
            case R.id.ergebnisButton2Spieler1:
                validateButton(ergebnis2Spieler1, 1);
                break;
            case R.id.ergebnisButton3Spieler1:
                validateButton(ergebnis3Spieler1, 1);
                break;
            case R.id.ergebnisButton4Spieler1:
                validateButton(ergebnis4Spieler1, 1);
                break;
            case R.id.ergebnisButton1Spieler2:
                validateButton(ergebnis1Spieler2, 2);
                break;
            case R.id.ergebnisButton2Spieler2:
                validateButton(ergebnis2Spieler2, 2);
                break;
            case R.id.ergebnisButton3Spieler2:
                validateButton(ergebnis3Spieler2, 2);
                break;
            case R.id.ergebnisButton4Spieler2:
                validateButton(ergebnis4Spieler2, 2);
                break;
        }
        if (player1Ready && player2Ready) {
            player1Ready = false;
            player2Ready = false;
            startPlayer1.setVisibility(View.GONE);
            startPlayer2.setVisibility(View.GONE);
            countdownSpieler1.setVisibility(View.VISIBLE);
            countdownSpieler2.setVisibility(View.VISIBLE);

            startCountdown();
        }
    }

    private void startCountdown() {
        cTimer = new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                countdownSpieler1.setText(String.format("%s", new SimpleDateFormat("s").format(new Date(millisUntilFinished+1000))));
                countdownSpieler2.setText(String.format("%s", new SimpleDateFormat("s").format(new Date(millisUntilFinished+1000))));
            }

            @Override
            public void onFinish() {
                countdownSpieler1.setVisibility(View.GONE);
                countdownSpieler2.setVisibility(View.GONE);
                changeErgebnisButtonVisibility(View.VISIBLE);

                aufgabePlayer1.setVisibility(View.VISIBLE);
                aufgabePlayer2.setVisibility(View.VISIBLE);
                scoreSpieler1.setVisibility(View.VISIBLE);
                scoreSpieler2.setVisibility(View.VISIBLE);
                zeitUebrigSpieler1.setVisibility(View.VISIBLE);
                zeitUebrigSpieler2.setVisibility(View.VISIBLE);
                starteSpiel(1);
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

    public void starteSpiel(int minuten) {
        cTimer = new CountDownTimer(minuten * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                zeitUebrigSpieler1.setText(String.format("Zeit: %s", new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished))));
                zeitUebrigSpieler2.setText(String.format("Zeit: %s", new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                changeErgebnisButtonVisibility(View.GONE);

                if (scoreWertSpieler1 > scoreWertSpieler2) {
                    aufgabePlayer1.setText("GEWONNEN");
                    aufgabePlayer1.setTextColor(Color.GREEN);
                    aufgabePlayer2.setText("VERLOREN");
                    aufgabePlayer2.setTextColor(Color.RED);
                } else if (scoreWertSpieler1 < scoreWertSpieler2) {
                    aufgabePlayer1.setText("VERLOREN");
                    aufgabePlayer1.setTextColor(Color.RED);
                    aufgabePlayer2.setText("GEWONNEN");
                    aufgabePlayer2.setTextColor(Color.GREEN);
                } else {
                    aufgabePlayer1.setText("UNENTSCHIEDEN");
                    aufgabePlayer1.setTextColor(getResources().getColor(unserBlau));
                    aufgabePlayer2.setText("UNENTSCHIEDEN");
                    aufgabePlayer2.setTextColor(getResources().getColor(unserBlau));
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 8000);

            }
        };
        updateViews(aufgabeGenerieren());
        cTimer.start();
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

    public void validateButton(Button button, int spielerNr) {
        if (missingPartIndex == 1 || missingPartIndex == 3) {
            if (button.getText().toString().equals(zeichenAusZahlenwert(missingPart))) {
                correctButton(spielerNr);
            } else {
                wrongButton(spielerNr);
            }
        } else {
            if (button.getText().toString().equals(String.valueOf(missingPart))) {
                correctButton(spielerNr);
            } else {
                wrongButton(spielerNr);
            }
        }
        changeErgebnisButtonVisibility(View.GONE);
        long delay = 500;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                public void run() {
                                    changeErgebnisButtonVisibility(View.VISIBLE);
                                    ergebnisRichtigOderFalschSpieler1.setVisibility(View.GONE);
                                    ergebnisRichtigOderFalschSpieler2.setVisibility(View.GONE);
                                    updateViews(aufgabeGenerieren());
                                }
                            }
                , delay);


    }

    public void changeErgebnisButtonVisibility(int value){

        ergebnis1Spieler1.setVisibility(value);
        ergebnis2Spieler1.setVisibility(value);
        ergebnis3Spieler1.setVisibility(value);
        ergebnis4Spieler1.setVisibility(value);
        ergebnis1Spieler2.setVisibility(value);
        ergebnis2Spieler2.setVisibility(value);
        ergebnis3Spieler2.setVisibility(value);
        ergebnis4Spieler2.setVisibility(value);

    }

    public void correctButton(int spielerNr) {
        ergebnisRichtigOderFalschSpieler1.setVisibility(View.VISIBLE);
        ergebnisRichtigOderFalschSpieler2.setVisibility(View.VISIBLE);
        if (spielerNr == 1) {
            scoreWertSpieler1++;
            ergebnisRichtigOderFalschSpieler1.setText("RICHTIG");
            ergebnisRichtigOderFalschSpieler1.setTextColor(Color.GREEN);

            ergebnisRichtigOderFalschSpieler2.setText("ZU LANGSAM");
            ergebnisRichtigOderFalschSpieler2.setTextColor(Color.RED);
        } else if (spielerNr == 2) {
            scoreWertSpieler2++;
            ergebnisRichtigOderFalschSpieler1.setText("ZU LANGSAM");
            ergebnisRichtigOderFalschSpieler1.setTextColor(Color.RED);

            ergebnisRichtigOderFalschSpieler2.setText("RICHTIG");
            ergebnisRichtigOderFalschSpieler2.setTextColor(Color.GREEN);
        }
    }

    public void wrongButton(int spielerNr) {
        ergebnisRichtigOderFalschSpieler1.setVisibility(View.VISIBLE);
        ergebnisRichtigOderFalschSpieler2.setVisibility(View.VISIBLE);
        if (spielerNr == 1) {
            scoreWertSpieler2++;
            if (scoreWertSpieler1 > 0) {
                scoreWertSpieler1--;
                ergebnisRichtigOderFalschSpieler1.setText("FALSCH");
                ergebnisRichtigOderFalschSpieler1.setTextColor(Color.RED);

                ergebnisRichtigOderFalschSpieler2.setText("GLÜCK GEHABT");
                ergebnisRichtigOderFalschSpieler2.setTextColor(Color.GREEN);
            }
        } else if (spielerNr == 2) {
            scoreWertSpieler1++;
            if (scoreWertSpieler2 > 0) {
                scoreWertSpieler2--;
                ergebnisRichtigOderFalschSpieler1.setText("GLÜCK GEHABT");
                ergebnisRichtigOderFalschSpieler1.setTextColor(Color.GREEN);

                ergebnisRichtigOderFalschSpieler2.setText("FALSCH");
                ergebnisRichtigOderFalschSpieler2.setTextColor(Color.RED);

            }
        }
    }


    public void updateViews(int[] aufgabe) {
        scoreSpieler1.setText(String.format("SCORE: %d", scoreWertSpieler1));
        scoreSpieler2.setText(String.format("SCORE: %d", scoreWertSpieler2));

        missingPartIndex = random.nextInt(6);
        int[] buttonValues = new int[4];
        missingPart = aufgabe[missingPartIndex];
        if (missingPartIndex == 1 || missingPartIndex == 3) {
            ergebnis1Spieler1.setText("+");
            ergebnis2Spieler1.setText("-");
            ergebnis3Spieler1.setText("*");
            ergebnis4Spieler1.setVisibility(View.INVISIBLE);
            ergebnis1Spieler2.setText("+");
            ergebnis2Spieler2.setText("-");
            ergebnis3Spieler2.setText("*");
            ergebnis4Spieler2.setVisibility(View.INVISIBLE);
            aufgabePlayer1.setText(aufgabeAlsString(aufgabe, missingPartIndex));
            aufgabePlayer2.setText(aufgabeAlsString(aufgabe, missingPartIndex));
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
        ergebnis1Spieler1.setText(String.valueOf(buttonValues[0]));
        ergebnis2Spieler1.setText(String.valueOf(buttonValues[1]));
        ergebnis3Spieler1.setText(String.valueOf(buttonValues[2]));
        ergebnis4Spieler1.setText(String.valueOf(buttonValues[3]));
        ergebnis4Spieler1.setVisibility(View.VISIBLE);
        aufgabePlayer1.setText(aufgabeAlsString(aufgabe, missingPartIndex));

        ergebnis1Spieler2.setText(String.valueOf(buttonValues[0]));
        ergebnis2Spieler2.setText(String.valueOf(buttonValues[1]));
        ergebnis3Spieler2.setText(String.valueOf(buttonValues[2]));
        ergebnis4Spieler2.setText(String.valueOf(buttonValues[3]));
        ergebnis4Spieler2.setVisibility(View.VISIBLE);
        aufgabePlayer2.setText(aufgabeAlsString(aufgabe, missingPartIndex));
    }
}