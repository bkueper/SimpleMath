package com.example.simplemath;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.String.*;

public class AuswertungZahlenEinfuegen extends AppCompatActivity {
    private int durchlaeufe, punktzahl, scoreWert;
    private TextView feedbackText;
    private Button weiterButton;
    private boolean highscoreMode, weitereRunde;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auswertung_zahlen_einfuegen);
        feedbackText = findViewById(R.id.feedbackFreiesSpiel);
        weiterButton = findViewById(R.id.weiterButton);
        Intent intent = getIntent();
        highscoreMode = intent.getBooleanExtra("HIGHSCOREMODE",false);
        if(highscoreMode){
            scoreWert = intent.getIntExtra("SCOREWERT",0);
            feedbackText.setText(format("du hast einen Score von %d erreicht!", scoreWert));
            weiterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else{
            durchlaeufe = intent.getIntExtra("DURCHLAEUFE", 0);
            punktzahl = intent.getIntExtra("PUNKTZAHL", 0);
            weitereRunde = durchlaeufe > 0;
            feedbackText.setText(format("Du hast %d von 15 Aufgaben gelöst!", punktzahl));
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