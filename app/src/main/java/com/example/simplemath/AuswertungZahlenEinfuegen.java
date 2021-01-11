package com.example.simplemath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.String.*;
import static java.lang.String.format;

public class AuswertungZahlenEinfuegen extends AppCompatActivity {
    private int durchlaeufe, punktzahl, spielId, minuten;
    private float scoreWert, bisherigerHighscore;
    private TextView feedbackText, highscoreView;
    private Button weiterButton;
    private boolean highscoreMode, weitereRunde;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = null;
        SharedPreferences.Editor editor = null;
        setContentView(R.layout.activity_auswertung_zahlen_einfuegen);
        feedbackText = findViewById(R.id.feedbackFreiesSpiel);
        weiterButton = findViewById(R.id.weiterButton);
        highscoreView = findViewById(R.id.highscore);
        Intent intent = getIntent();
        spielId = intent.getIntExtra("SPIELID", 0);
        switch (spielId) {
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
        highscoreMode = intent.getBooleanExtra("HIGHSCOREMODE", false);
        if (highscoreMode) {
            scoreWert = intent.getIntExtra("SCOREWERT", 0);
            minuten = intent.getIntExtra("MINUTES", 1);
            float scoreProMinute =(float) scoreWert/minuten;
            username = getSharedPreferences("currentUser", MODE_PRIVATE).getString("username", "");
            bisherigerHighscore = prefs.getFloat(username, 0.0f);

            if (scoreProMinute > bisherigerHighscore) {

                editor.putFloat(username, scoreProMinute);
                editor.apply();
                bisherigerHighscore = scoreProMinute;
            }
            highscoreView.setVisibility(View.VISIBLE);
            highscoreView.setText(format("HIGHSCORE: %.2f",bisherigerHighscore));
            feedbackText.setText(format("DEIN SCORE: %.2f", scoreProMinute));

            weiterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            durchlaeufe = intent.getIntExtra("DURCHLAEUFE", 0);
            punktzahl = intent.getIntExtra("PUNKTZAHL", 0);
            weitereRunde = durchlaeufe > 0;
            highscoreView.setVisibility(View.GONE);
            feedbackText.setText(format("Du hast %d von 15 Aufgaben gelöst!", punktzahl));
            weiterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent result = new Intent();
                    result.putExtra("WEITERERUNDE", weitereRunde);
                    setResult(1, result);
                    finish();

                }
            });
        }


    }
}