package com.example.simplemath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.String.format;

public class AuswertungZahlenEinfuegen extends AppCompatActivity {
    private int durchlaeufe, punktzahl, scoreWert, bisherigerHighscore;
    private TextView feedbackText;
    private Button weiterButton;
    private boolean highscoreMode, weitereRunde;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auswertung_zahlen_einfuegen);
        feedbackText = findViewById(R.id.feedbackFreiesSpiel);
        weiterButton = findViewById(R.id.weiterButton);
        Intent intent = getIntent();
        highscoreMode = intent.getBooleanExtra("HIGHSCOREMODE", false);
        if (highscoreMode) {
            scoreWert = intent.getIntExtra("SCOREWERT",0);
            username = intent.getStringExtra("USERNAME");
            SharedPreferences prefs = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
            bisherigerHighscore = prefs.getInt(username,0);
            if(scoreWert>bisherigerHighscore){
                SharedPreferences.Editor editor = getSharedPreferences("sharedPrefs", MODE_PRIVATE).edit();
                editor.putInt(username, scoreWert);
                editor.commit();
            }
            feedbackText.setText(format("Du hast einen Score von %d erreicht!", scoreWert));
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