package com.example.simplemath;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.String.*;

/**
 * Class which evaluates the latest game.
 */
public class Evaluation extends AppCompatActivity {
    private boolean anotherRound;

    /**
     * Finds out whether a new Highscore in the played game got broken or how many tasks were
     * correct. Updates Highscore if old highscore got broken. Tells the Activity that opened,
     * whether it needs to start another round or not.
     * @param savedInstanceState android related
     */
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = null;
        SharedPreferences.Editor editor = null;
        setContentView(R.layout.activity_evaluation);
        TextView feedbackText = findViewById(R.id.feedbackFreiesSpiel);
        Button continueButton = findViewById(R.id.continueButton);
        TextView highscoreView = findViewById(R.id.highscore);
        Intent intent = getIntent();
        int gameID = intent.getIntExtra("SPIELID", 0);
        switch (gameID) {
            case 0:
                prefs = getSharedPreferences("groesserKleinerHighscore", MODE_PRIVATE);
                editor = getSharedPreferences("groesserKleinerHighscore", MODE_PRIVATE).edit();
                break;
            case 1:
                prefs = getSharedPreferences("zahlenEinfuegenHighscore", MODE_PRIVATE);
                editor = getSharedPreferences("zahlenEinfuegenHighscore", MODE_PRIVATE).edit();
                break;
            case 2:
                prefs = getSharedPreferences("hochzaehlenHighscore", MODE_PRIVATE);
                editor = getSharedPreferences("hochzaehlenHighscore", MODE_PRIVATE).edit();
                break;
            default:

        }
        boolean highscoreMode = intent.getBooleanExtra("HIGHSCOREMODE", false);
        if (highscoreMode) {
            float scoreValue = intent.getIntExtra("SCOREWERT", 0);
            int minutes = intent.getIntExtra("MINUTES", 1);
            float scorePerMinute =(float) scoreValue / minutes;
            String username = getSharedPreferences("currentUser", MODE_PRIVATE).getString("username", "");
            float oldHighscore = prefs.getFloat(username, 0.0f);

            if (scorePerMinute > oldHighscore) {

                editor.putFloat(username, scorePerMinute);
                editor.apply();
                oldHighscore = scorePerMinute;
            }
            highscoreView.setVisibility(View.VISIBLE);
            highscoreView.setText(format("HIGHSCORE: %.2f", oldHighscore));
            feedbackText.setText(format("DEIN SCORE: %.2f", scorePerMinute));

            continueButton.setOnClickListener(v -> finish());
        } else {
            int rounds = intent.getIntExtra("DURCHLAEUFE", 0);
            int reachedPoints = intent.getIntExtra("PUNKTZAHL", 0);
            anotherRound = rounds > 0;
            highscoreView.setVisibility(View.GONE);
            feedbackText.setText(format("Du hast %d von 15 Aufgaben gelöst!", reachedPoints));
            continueButton.setOnClickListener(v -> {
                Intent result = new Intent();
                result.putExtra("WEITERERUNDE", anotherRound);
                setResult(1, result);
                finish();
            });
        }


    }
}