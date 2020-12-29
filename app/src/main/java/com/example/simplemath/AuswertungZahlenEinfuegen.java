package com.example.simplemath;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AuswertungZahlenEinfuegen extends AppCompatActivity {
    private int durchlaeufe;
    private int punktzahl;
    private TextView feedbackText;
    private Button weiterButton;
    private boolean highscoreMode;
    private int scoreWert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auswertung_zahlen_einfuegen);
        feedbackText = findViewById(R.id.feedbackFreiesSpiel);
        weiterButton = findViewById(R.id.weiterButton);
        boolean weitereRunde;
        Intent intent = getIntent();
        highscoreMode = intent.getBooleanExtra("HIGHSCOREMODE",false);
        if(highscoreMode){
            scoreWert = intent.getIntExtra("SCOREWERT",0);
            feedbackText.setText("Du hast einen Score von " + scoreWert + " erreicht!");
            weiterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else{
            durchlaeufe = intent.getIntExtra("DURCHLAEUFE", 0);
            if(durchlaeufe <= 0){
                weitereRunde = false;
            }else{
                weitereRunde = true;
            }
            punktzahl = intent.getIntExtra("PUNKTZAHL", 0);
            feedbackText.setText("Du hast " + punktzahl + " von 15 Aufgaben gelöst!");
            weiterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent result = new Intent();
                    result.putExtra("WEITERERUNDE",weitereRunde);
                    setResult(1,result);
                    finish();

                }
            });
        }


    }
}