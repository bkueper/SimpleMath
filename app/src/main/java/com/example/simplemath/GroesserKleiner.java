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

public class GroesserKleiner extends AppCompatActivity implements View.OnTouchListener {
    private final Random random = new Random();
    private Button result1, result2, result3, result4, confirm;
    private boolean highscoreMode;
    private boolean firstRound = true;
    private int remainingTasks, scoreValue, rounds, sortingType, correctAnswers, minutes;
    private TextView sortierAufgabe, groesserKleinerZeichen1, groesserKleinerZeichen2, groesserKleinerZeichen3, score, aufgabenFortschritt, zeit;
    private View solution1, solution2, solution3, solution4;
    private float dX, dY;
    private final Rect normalRect = new Rect();
    private final int[] location = new int[2];
    private Button[] besetztePlaetze = new Button[4];
    private float[] startPositionen;
    private CountDownTimer cTimer;

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
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ergebnisBewerten();
                if (remainingTasks <= 0) {
                    openAuswertung();
                } else {
                    updateViews();
                }
            }
        });
        sortierAufgabe = findViewById(R.id.sortierAufgabe);
        groesserKleinerZeichen1 = findViewById(R.id.groesserKleinerZeichen1);
        groesserKleinerZeichen2 = findViewById(R.id.groesserKleinerZeichen2);
        groesserKleinerZeichen3 = findViewById(R.id.groesserKleinerZeichen3);
        aufgabenFortschritt = findViewById(R.id.taskProgress);
        score = findViewById(R.id.score);
        zeit = findViewById(R.id.timeLeft);
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
        remainingTasks = 1;
        updateViews();
        scoreValue = 0;
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
        remainingTasks = 15;
        updateViews();
        score.setVisibility(View.GONE);
        zeit.setVisibility(View.GONE);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (firstRound) {
            startPositionen = new float[]{result1.getX(), result1.getY(),
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
                    besetztePlaetze[0] = (Button) v;
                    solution1.dispatchTouchEvent(event);
                    v.setX(solution1.getX() - 20);
                    v.setY(solution1.getY() + 60);
                }
                if (isViewInBounds(solution2, x, y)) {
                    besetztePlaetze[1] = (Button) v;
                    solution2.dispatchTouchEvent(event);
                    v.setX(solution2.getX() - 20);
                    v.setY(solution2.getY() + 60);
                }
                if (isViewInBounds(solution3, x, y)) {
                    besetztePlaetze[2] = (Button) v;
                    solution3.dispatchTouchEvent(event);
                    v.setX(solution3.getX() - 20);
                    v.setY(solution3.getY() + 60);
                }
                if (isViewInBounds(solution4, x, y)) {
                    besetztePlaetze[3] = (Button) v;
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

    public void ergebnisBewerten() {
        if (!highscoreMode) {
            remainingTasks--;
        }
        for (int i = 0; i < 4; i++) {
            if (besetztePlaetze[i] == null) {
                if (scoreValue > 0) {
                    scoreValue--;
                }
                return;
            }
        }
        if (sortingType == 0) {
            for (int i = 0; i < 3; i++) {
                if (Integer.parseInt(besetztePlaetze[i].getText().toString()) <= Integer.parseInt(besetztePlaetze[i + 1].getText().toString())) {
                    if (scoreValue > 0) {
                        scoreValue--;
                    }
                    return;
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                if (Integer.parseInt(besetztePlaetze[i].getText().toString()) >= Integer.parseInt(besetztePlaetze[i + 1].getText().toString())) {
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

    public void updateViews() {
        if (!highscoreMode) {
            aufgabenFortschritt.setText(format("Aufgabe %d von %d", (16 - remainingTasks), 15));
        } else {
            score.setText(format("SCORE: %d", scoreValue));
        }
        sortingType = random.nextInt(2);
        if (sortingType == 0) {
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
        result1.setText(valueOf(buttonValues[0]));
        result2.setText(valueOf(buttonValues[1]));
        result3.setText(valueOf(buttonValues[2]));
        result4.setText(valueOf(buttonValues[3]));
        besetztePlaetze = new Button[4];
        if (!firstRound) {
            result1.setX(startPositionen[0]);
            result1.setY(startPositionen[1]);

            result2.setX(startPositionen[2]);
            result2.setY(startPositionen[3]);

            result3.setX(startPositionen[4]);
            result3.setY(startPositionen[5]);

            result4.setX(startPositionen[6]);
            result4.setY(startPositionen[7]);
        }
    }

    private boolean isViewInBounds(View view, int x, int y) {
        view.getDrawingRect(normalRect);
        view.getLocationOnScreen(location);
        normalRect.offset(location[0], location[1]);
        return normalRect.contains(x, y);
    }

    public void openAuswertung() {
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