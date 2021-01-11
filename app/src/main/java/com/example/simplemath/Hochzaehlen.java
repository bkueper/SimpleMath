package com.example.simplemath;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static java.lang.String.format;

public class Hochzaehlen extends AppCompatActivity implements View.OnClickListener {
    private final Random random = new Random();
    private GridLayout gridLayout;
    public ArrayList<Button> allButtons = new ArrayList<>();
    private int currentPosition, minPosition,spielId,restlicheAufgaben,durchlaeufe,minuten,correctAnswers,ersteZahl,zweiteZahl;
    private int scoreWert = 0;
    private TextView score, zeit,aufgabenText,aufgabenFortschritt;
    private boolean highscoreMode;
    private CountDownTimer cTimer;
    private Button bestaetigen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hochzaehlen);
        score = findViewById(R.id.score);
        zeit = findViewById(R.id.zeitUebrig);
        aufgabenText = findViewById(R.id.aufgabe);
        aufgabenFortschritt = findViewById(R.id.aufgabenFortschritt);
        bestaetigen = findViewById(R.id.bestaetigen);
        bestaetigen.setOnClickListener(this);
        gridLayout = findViewById(R.id.gridLayout);
        for(int i = 0; i<25; i++){
            addButton(i);
        }
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
    public void startHighscoreGame(int minuten) {
        restlicheAufgaben = 1;
        updateViews();
        scoreWert = 0;
        aufgabenFortschritt.setVisibility(View.GONE);
        cTimer = new CountDownTimer(minuten * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                zeit.setText("Zeit: " + new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
            }

            @Override
            public void onFinish() {
                openAuswertung();
                finish();
            }
        };
        cTimer.start();
    }
    public void startFreiesSpiel() {
        restlicheAufgaben = 15;
        updateViews();
        score.setVisibility(View.GONE);
        zeit.setVisibility(View.GONE);
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
    public void validate(){
        if(currentPosition + 1 == ersteZahl + zweiteZahl){
            if(highscoreMode){
                scoreWert++;
            }else{
                correctAnswers++;
                restlicheAufgaben--;
            }
            correctAnswer();
        }else{
            if(highscoreMode){
                if(scoreWert > 0) {
                    scoreWert--;
                }
            }else{
                restlicheAufgaben--;
            }
            wrongAnswer();
        }
    }
    public void correctAnswer() {
        Handler handler = new Handler();
        bestaetigen.setVisibility(View.GONE);
        for(Button button:allButtons){
            button.setBackgroundResource(R.drawable.round_button_bestaetigt);
        }
        handler.postDelayed(new Runnable() {
            public void run() {
                bestaetigen.setVisibility(View.VISIBLE);
                updateViews();
            }
        }, 1000);

    }

    public void wrongAnswer() {
        Handler handler = new Handler();
        bestaetigen.setVisibility(View.GONE);
        for(Button button:allButtons) {
            button.setBackgroundResource(R.drawable.round_button_falsch);
        }
        handler.postDelayed(new Runnable() {
            public void run() {
                bestaetigen.setVisibility(View.VISIBLE);
                updateViews();
            }
        }, 1000);
    }
    public void updateViews(){
        if(!highscoreMode){
            aufgabenFortschritt.setText(format("Aufgabe %d von %d", (16 - restlicheAufgaben), 15));
        }else{
            score.setText(format("SCORE: %d",scoreWert));
        }
        aufgabenText.setText(aufgabeErzeugen());
        for(int i = 0; i < ersteZahl; i++){
            allButtons.get(i).setBackgroundResource(R.drawable.round_button_bestaetigt);
        }
        for(int i = ersteZahl; i < allButtons.size(); i++){
            allButtons.get(i).setBackgroundResource(R.drawable.round_button);
        }
        currentPosition = ersteZahl - 1;
    }
    public String aufgabeErzeugen(){
        ersteZahl = random.nextInt(21);
        zweiteZahl = 1 + random.nextInt(25 - ersteZahl) ;
        String aufgabeAlsString = format("%d + %d", ersteZahl,zweiteZahl);
        return aufgabeAlsString;
    }
    public void openAuswertung() {
        Intent intent = new Intent(this, AuswertungZahlenEinfuegen.class);
        if (highscoreMode) {
            intent.putExtra("SCOREWERT", scoreWert);
            intent.putExtra("MINUTES", minuten);
            intent.putExtra("HIGHSCOREMODE", true);
            startActivity(intent);
        } else {
            intent.putExtra("PUNKTZAHL", correctAnswers);
            durchlaeufe -= 1;
            intent.putExtra("DURCHLAEUFE", durchlaeufe);
            intent.putExtra("HIGHSCOREMODE", false);
            startActivityForResult(intent, 1);
        }
    }
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
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bestaetigen) {
            validate();
            if(!highscoreMode) {
                if (restlicheAufgaben == 0) {
                    openAuswertung();
                }
            }
        } else {
            int position = v.getId();
            if (position < currentPosition) {
                if (position > ersteZahl - 2) {
                    for (int i = position + 1; i <= currentPosition; i++) {
                        allButtons.get(i).setBackgroundResource(R.drawable.round_button);
                    }
                }
            } else if (position > currentPosition) {
                if (position > ersteZahl - 1) {
                    for (int i = ersteZahl; i <= position; i++) {
                        allButtons.get(i).setBackgroundResource(R.drawable.round_button_gefaerbt);
                    }
                }
            }
            currentPosition = position;
        }
    }
}