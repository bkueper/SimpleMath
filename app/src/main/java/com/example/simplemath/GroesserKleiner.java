package com.example.simplemath;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GroesserKleiner extends AppCompatActivity implements View.OnTouchListener {
    private final Random random = new Random();
    private Button ergebnis1, ergebnis2, ergebnis3, ergebnis4,bestaetigen;
    private boolean highscoreMode;
    private boolean ersterDurchlauf = true;
    private int restlicheAufgaben, scoreWert, durchlaeufe, sortierArt, correctAnswers;
    private TextView sortierAufgabe, groesserKleinerZeichen1, groesserKleinerZeichen2, groesserKleinerZeichen3,score,aufgabenFortschritt,zeit;
    private View solution1,solution2,solution3,solution4;
    private float dX, dY;
    private Rect normalRect = new Rect();
    private int[] location = new int[2];
    private Button[] besetztePlaetze = new Button[4];
    private float[] startPositionen;
    private CountDownTimer cTimer;
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
        bestaetigen = findViewById(R.id.bestaetigen);
        bestaetigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ergebnisBewerten();
                if(restlicheAufgaben<=0){
                    openAuswertung();
                }else{
                    updateViews();
                }
            }
        });
        sortierAufgabe = findViewById(R.id.sortierAufgabe);
        groesserKleinerZeichen1 = findViewById(R.id.groesserKleinerZeichen1);
        groesserKleinerZeichen2 = findViewById(R.id.groesserKleinerZeichen2);
        groesserKleinerZeichen3 = findViewById(R.id.groesserKleinerZeichen3);
        aufgabenFortschritt = findViewById(R.id.aufgabenFortschritt);
        score = findViewById(R.id.score);
        zeit = findViewById(R.id.zeitUebrig);
        solution1 = findViewById(R.id.solutionSpace1);
        solution2 = findViewById(R.id.solutionSpace2);
        solution3 = findViewById(R.id.solutionSpace3);
        solution4 = findViewById(R.id.solutionSpace4);

        Intent intent = getIntent();
        highscoreMode = intent.getBooleanExtra("HIGHSCOREMODE", true);
        int minuten = intent.getIntExtra("MINUTES", 1);
        if (highscoreMode) {
            restlicheAufgaben = 1;
            startHighscoreGame(minuten);
        } else {
            durchlaeufe = intent.getIntExtra("DURCHLAEUFE", 1);
            startFreiesSpiel();
        }
    }

    @Override
    protected void onDestroy() {
            super.onDestroy();
            if(cTimer != null){
                cTimer.cancel();
            }
        }

    public void startHighscoreGame(int minuten){
        restlicheAufgaben = 1;
        updateViews();
        scoreWert = 0;
        aufgabenFortschritt.setVisibility(View.GONE);
        cTimer = new CountDownTimer(minuten*60000,1000) {
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
    public void startFreiesSpiel(){
        restlicheAufgaben =15;
        updateViews();
        score.setVisibility(View.GONE);
        zeit.setVisibility(View.GONE);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(ersterDurchlauf){
            startPositionen = new float[] {ergebnis1.getX(),ergebnis1.getY(),
                    ergebnis2.getX(),ergebnis2.getY(),ergebnis3.getX(),
                    ergebnis3.getY(),ergebnis4.getX(),ergebnis4.getY()};
        }
        ersterDurchlauf = false;
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
                int x = (int)event.getRawX();
                int y = (int)event.getRawY();
                if(isViewInBounds(solution1, x, y)){
                    besetztePlaetze[0] = (Button)v;
                    solution1.dispatchTouchEvent(event);
                    v.setX(solution1.getX()-20);
                    v.setY(solution1.getY()+60);
                }
                if(isViewInBounds(solution2, x, y)){
                    besetztePlaetze[1] = (Button)v;
                    solution2.dispatchTouchEvent(event);
                    v.setX(solution2.getX()-20);
                    v.setY(solution2.getY()+60);
                }
                if(isViewInBounds(solution3, x, y)){
                    besetztePlaetze[2] = (Button)v;
                    solution3.dispatchTouchEvent(event);
                    v.setX(solution3.getX()-20);
                    v.setY(solution3.getY()+60);
                }
                if(isViewInBounds(solution4, x, y)){
                    besetztePlaetze[3] = (Button)v;
                    solution4.dispatchTouchEvent(event);
                    v.setX(solution4.getX()-20);
                    v.setY(solution4.getY()+60);
                }
                break;
            default:
                return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean weitereRunde = false;
        if(requestCode == 1){
            assert data != null;
            weitereRunde = data.getBooleanExtra("WEITERERUNDE",false);
        }
        if(weitereRunde){
            startFreiesSpiel();
        }else{
            finish();
        }
    }

    public void ergebnisBewerten(){
        if(!highscoreMode){
            restlicheAufgaben--;
        }
        for(int i = 0; i < 4;i++){
            if(besetztePlaetze[i] == null){
                if(scoreWert > 0){
                    scoreWert--;
                }
                return;
            }
        }
        if(sortierArt == 0){
            for(int i = 0; i<3;i++){
                if(Integer.parseInt(besetztePlaetze[i].getText().toString()) <= Integer.parseInt(besetztePlaetze[i+1].getText().toString())){
                    if(scoreWert>0){
                        scoreWert--;
                    }
                    return;
                }
            }
        }else{
            for(int i = 0; i<3;i++){
                if(Integer.parseInt(besetztePlaetze[i].getText().toString()) >= Integer.parseInt(besetztePlaetze[i+1].getText().toString())){
                    if(scoreWert>0){
                        scoreWert--;
                    }
                    return;
                }
            }
        }
        if(highscoreMode){
           scoreWert++;
        }
        correctAnswers++;
    }

    public void updateViews() {
        if (!highscoreMode) {
            aufgabenFortschritt.setText(String.format("Aufgabe %d von %d", (16 - restlicheAufgaben), 15));
        } else {
            score.setText(String.format("SCORE: %d", scoreWert));
        }
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
        for (int i = 0; i < 4; i++) {
                boolean doppeltesErgebnis;
                int randomErgebnis;
                do {
                    doppeltesErgebnis = false;
                    randomErgebnis = random.nextInt(100);
                    for (int j = 0; j < 4; j++) {
                        if (buttonValues[j] == randomErgebnis) {
                            doppeltesErgebnis = true;
                            break;
                        }
                    }
                } while (doppeltesErgebnis);
                buttonValues[i] = randomErgebnis;
        }
        ergebnis1.setText(String.valueOf(buttonValues[0]));
        ergebnis2.setText(String.valueOf(buttonValues[1]));
        ergebnis3.setText(String.valueOf(buttonValues[2]));
        ergebnis4.setText(String.valueOf(buttonValues[3]));
        if(!ersterDurchlauf) {
            ergebnis1.setX(startPositionen[0]);
            ergebnis1.setY(startPositionen[1]);

            ergebnis2.setX(startPositionen[2]);
            ergebnis2.setY(startPositionen[3]);

            ergebnis3.setX(startPositionen[4]);
            ergebnis3.setY(startPositionen[5]);

            ergebnis4.setX(startPositionen[6]);
            ergebnis4.setY(startPositionen[7]);
        }
    }
    private boolean isViewInBounds(View view, int x, int y){
        view.getDrawingRect(normalRect);
        view.getLocationOnScreen(location);
        normalRect.offset(location[0], location[1]);
        return normalRect.contains(x, y);
    }

    public void openAuswertung(){
        Intent intent = new Intent(this, AuswertungZahlenEinfuegen.class);
        if(highscoreMode){
            intent.putExtra("SCOREWERT",scoreWert);
            intent.putExtra("HIGHSCOREMODE",true);
            startActivity(intent);
        }else{
            intent.putExtra("PUNKTZAHL", correctAnswers);
            durchlaeufe -= 1;
            intent.putExtra("DURCHLAEUFE",durchlaeufe);
            intent.putExtra("HIGHSCOREMODE",false);
            startActivityForResult(intent,1);
        }
    }
}