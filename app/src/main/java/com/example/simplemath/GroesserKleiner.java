package com.example.simplemath;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class GroesserKleiner extends AppCompatActivity implements View.OnTouchListener {
    private Button ergebnis1, ergebnis2, ergebnis3, ergebnis4, ergebnis5;
    private boolean highscoreMode;
    private int minuten, restlicheAufgaben, durchlaeufe, sortierArt;
    private Random random = new Random();
    private TextView sortierAufgabe, groesserKleinerZeichen1, groesserKleinerZeichen2, groesserKleinerZeichen3;


    float dX, dY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groesser_kleiner);
        ergebnis1 = findViewById(R.id.groesserKleinerErgebnis1);
        ergebnis1.setOnTouchListener(this);
        ergebnis2 = findViewById(R.id.groesserKleinerErgebnis2);
        ergebnis2.setOnTouchListener(this);
        ergebnis3 = findViewById(R.id.groesserKleinerErgebnis3);
        ergebnis3.setOnTouchListener(this);
        ergebnis4 = findViewById(R.id.groesserKleinerErgebnis4);
        ergebnis4.setOnTouchListener(this);
        sortierAufgabe = findViewById(R.id.sortierAufgabe);
        groesserKleinerZeichen1 = findViewById(R.id.groesserKleinerZeichen1);
        groesserKleinerZeichen2 = findViewById(R.id.groesserKleinerZeichen2);
        groesserKleinerZeichen3 = findViewById(R.id.groesserKleinerZeichen3);
        updateViews();
        Intent intent = getIntent();
        highscoreMode = intent.getBooleanExtra("HIGHSCOREMODE", true);
        minuten = intent.getIntExtra("MINUTES", 1);
        if (highscoreMode) {
            restlicheAufgaben = 1;
            startHighscoreGame(minuten);
        } else {
            durchlaeufe = intent.getIntExtra("DURCHLAEUFE", 1);
            startFreiesSpiel();
        }
    }
    public void startHighscoreGame(int minuten){

    }
    public void startFreiesSpiel(){

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
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
            default:
                return false;
        }
        return true;
    }

    public void updateViews() {
        sortierArt = random.nextInt(2);
        if (sortierArt == 0) {
            sortierAufgabe.setText("Sortiere von groß nach klein");
            groesserKleinerZeichen1.setText(">");
            groesserKleinerZeichen2.setText(">");
            groesserKleinerZeichen3.setText(">");
        } else {
            sortierAufgabe.setText("Sortiere von klein nach groß");
            groesserKleinerZeichen1.setText("<");
            groesserKleinerZeichen2.setText("<");
            groesserKleinerZeichen3.setText("<");
        }
        int[] buttonValues = new int[4];
        int correctAnswerIndex = random.nextInt(4);
        for (int i = 0; i < 4; i++) {
                boolean doppeltesErgebnis;
                int randomErgebnis;
                do {
                    doppeltesErgebnis = false;
                    randomErgebnis = random.nextInt(100);
                    for (int j = 0; j < 4; j++) {
                        if (buttonValues[j] == randomErgebnis) {
                            doppeltesErgebnis = true;
                        }
                    }
                } while (doppeltesErgebnis);
                buttonValues[i] = randomErgebnis;
        }
        ergebnis1.setText(String.valueOf(buttonValues[0]));
        ergebnis2.setText(String.valueOf(buttonValues[1]));
        ergebnis3.setText(String.valueOf(buttonValues[2]));
        ergebnis4.setText(String.valueOf(buttonValues[3]));
    }
}