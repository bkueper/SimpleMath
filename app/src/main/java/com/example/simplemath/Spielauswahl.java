package com.example.simplemath;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class Spielauswahl extends AppCompatActivity implements View.OnClickListener{
    private int spielID;
    private Button highscore;
    private Button freiesSpiel;
    private SeekBar zeitLeiste;
    private TextView angezeigteZeit;
    private ImageButton hochzaehlen;
    private ImageButton zahlenEinfuegen;
    private ImageButton groesserKleiner;
    private Button hochzaehlenPlus;
    private Button hochzaehlenMinus;
    private Button zahlenEinfuegenPlus;
    private Button zahlenEinfuegenMinus;
    private Button groesserKleinerPlus;
    private Button groesserKleinerMinus;
    private TextView countForHochzaehlen;
    private TextView countForZahlenEinfuegen;
    private TextView countForGroesserKleiner;
    private Button startButton;
    private boolean highscoreMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spielauswahl);
        countForZahlenEinfuegen = findViewById(R.id.countForZahlenEinfuegen);
        countForGroesserKleiner = findViewById(R.id.countForGroesserKleiner);
        countForHochzaehlen = findViewById(R.id.countForHochzählen);
        highscore = findViewById(R.id.highscore);
        highscore.setOnClickListener(this);
        freiesSpiel = findViewById(R.id.freiesSpiel);
        freiesSpiel.setOnClickListener(this);
        zeitLeiste = findViewById(R.id.spieldauer);
        zeitLeiste.setMax(14);
        zeitLeiste.setProgress(2);
        zeitLeiste.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int totalProgress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                angezeigteZeit.setText(String.valueOf(progress+1) + " Min");
                totalProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                angezeigteZeit.setText(String.valueOf(totalProgress+1) + " Min");
            }
        });
        angezeigteZeit = findViewById(R.id.angezeigteZeit);
        angezeigteZeit.setText(String.valueOf(zeitLeiste.getProgress()+1)+ " Min");
        hochzaehlen = findViewById(R.id.hochzaehlen);
        hochzaehlen.setOnClickListener(this);
        zahlenEinfuegen = findViewById(R.id.zahlenEinfuegen);
        zahlenEinfuegen.setOnClickListener(this);
        groesserKleiner = findViewById(R.id.groesserKleiner);
        groesserKleiner.setOnClickListener(this);
        hochzaehlenPlus = findViewById(R.id.plusHochzaehlen);
        hochzaehlenPlus.setOnClickListener(this);
        hochzaehlenMinus = findViewById(R.id.minusHochzaehlen);
        hochzaehlenMinus.setOnClickListener(this);
        zahlenEinfuegenPlus = findViewById(R.id.plusZahlenEinfuegen);
        zahlenEinfuegenPlus.setOnClickListener(this);
        zahlenEinfuegenMinus = findViewById(R.id.minusZahlenEinfuegen);
        zahlenEinfuegenMinus.setOnClickListener(this);
        groesserKleinerPlus = findViewById(R.id.plusGroesserKleiner);
        groesserKleinerPlus.setOnClickListener(this);
        groesserKleinerMinus = findViewById(R.id.minusGroesserKleiner);
        groesserKleinerMinus.setOnClickListener(this);
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        checkAllCounts();
    }
    public void countPlus(TextView tw){
        int zahl = Integer.parseInt(tw.getText().toString());
        if(zahl<9){
            zahl++;
            tw.setText(String.valueOf(zahl));
        }
    }
    public void countMinus(TextView tw){
        int zahl = Integer.parseInt(tw.getText().toString());
        if(zahl>0){
            zahl--;
            tw.setText(String.valueOf(zahl));
        }
    }
    public void checkCount(TextView tw,ImageButton ib){
        if(Integer.parseInt(countForHochzaehlen.getText().toString())>0 || Integer.parseInt(countForZahlenEinfuegen.getText().toString())>0 || Integer.parseInt(countForGroesserKleiner.getText().toString())>0){
            startButton.setVisibility(View.VISIBLE);
        }else{
            startButton.setVisibility(View.GONE);
        }
        if(Integer.parseInt(tw.getText().toString())>0){
            ib.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button_pressed));
        }else{
            ib.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button));
        }
    }
    public void checkAllCounts(){
        checkCount(countForHochzaehlen,hochzaehlen);
        checkCount(countForZahlenEinfuegen,zahlenEinfuegen);
        checkCount(countForGroesserKleiner,groesserKleiner);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.highscore:
                highscoreMode = true;
                highscore.setBackgroundColor(Color.GREEN);
                freiesSpiel.setBackgroundColor(Color.parseColor("#2196F3"));
                zeitLeiste.setVisibility(View.VISIBLE);
                angezeigteZeit.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.GONE);
                hochzaehlenPlus.setVisibility(View.GONE);
                groesserKleinerPlus.setVisibility(View.GONE);
                zahlenEinfuegenPlus.setVisibility(View.GONE);
                hochzaehlenMinus.setVisibility(View.GONE);
                groesserKleinerMinus.setVisibility(View.GONE);
                zahlenEinfuegenMinus.setVisibility(View.GONE);
                countForGroesserKleiner.setVisibility(View.GONE);
                countForZahlenEinfuegen.setVisibility(View.GONE);
                countForHochzaehlen.setVisibility(View.GONE);
                hochzaehlen.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button));
                zahlenEinfuegen.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button));
                groesserKleiner.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button));
                break;
            case R.id.freiesSpiel:
                checkAllCounts();
                highscoreMode = false;
                highscore.setBackgroundColor(Color.parseColor("#2196F3"));
                freiesSpiel.setBackgroundColor(Color.GREEN);
                zeitLeiste.setVisibility(View.GONE);
                angezeigteZeit.setVisibility(View.GONE);
                hochzaehlenPlus.setVisibility(View.VISIBLE);
                groesserKleinerPlus.setVisibility(View.VISIBLE);
                zahlenEinfuegenPlus.setVisibility(View.VISIBLE);
                hochzaehlenMinus.setVisibility(View.VISIBLE);
                groesserKleinerMinus.setVisibility(View.VISIBLE);
                zahlenEinfuegenMinus.setVisibility(View.VISIBLE);
                countForGroesserKleiner.setVisibility(View.VISIBLE);
                countForZahlenEinfuegen.setVisibility(View.VISIBLE);
                countForHochzaehlen.setVisibility(View.VISIBLE);
                break;
            case R.id.hochzaehlen:
                if(highscoreMode) {
                    startButton.setVisibility(View.VISIBLE);
                    hochzaehlen.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button_pressed));
                    zahlenEinfuegen.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button));
                    groesserKleiner.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button));
                    spielID = 0;
                }
                break;
            case R.id.zahlenEinfuegen:
                if(highscoreMode) {
                    startButton.setVisibility(View.VISIBLE);
                    hochzaehlen.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button));
                    zahlenEinfuegen.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button_pressed));
                    groesserKleiner.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button));
                    spielID = 1;
                }
                break;
            case R.id.groesserKleiner:
                if(highscoreMode) {
                    startButton.setVisibility(View.VISIBLE);
                    hochzaehlen.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button));
                    zahlenEinfuegen.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button));
                    groesserKleiner.setBackground((Drawable) getResources().getDrawable(R.drawable.default_button_pressed));
                    spielID = 2;
                }
                break;
            case R.id.plusHochzaehlen:
                countPlus(countForHochzaehlen);
                checkCount(countForHochzaehlen,hochzaehlen);
                break;
            case R.id.minusHochzaehlen:
                countMinus(countForHochzaehlen);
                checkCount(countForHochzaehlen,hochzaehlen);
                break;
            case R.id.plusZahlenEinfuegen:
                countPlus(countForZahlenEinfuegen);
                checkCount(countForZahlenEinfuegen,zahlenEinfuegen);
                break;
            case R.id.minusZahlenEinfuegen:
                countMinus(countForZahlenEinfuegen);
                checkCount(countForZahlenEinfuegen,zahlenEinfuegen);
                break;
            case R.id.plusGroesserKleiner:
                countPlus(countForGroesserKleiner);
                checkCount(countForGroesserKleiner,groesserKleiner);
                break;
            case R.id.minusGroesserKleiner:
                countMinus(countForGroesserKleiner);
                checkCount(countForGroesserKleiner,groesserKleiner);
                break;
            case R.id.startButton:
                if(highscoreMode){
                    int minuten = zeitLeiste.getProgress() + 1;
                    starteHighscoreSpiel(minuten,spielID);
                }else{
                    int []durchlaeufe = new int[3];
                    durchlaeufe[0] = Integer.parseInt(countForHochzaehlen.getText().toString());
                    durchlaeufe[1] = Integer.parseInt(countForZahlenEinfuegen.getText().toString());
                    durchlaeufe[2] = Integer.parseInt(countForGroesserKleiner.getText().toString());
                    starteFreiesSpiel(durchlaeufe);
                }
            default:
                break;
        }
    }

    public void starteHighscoreSpiel(int minuten, int spielID){
        switch (spielID){
            case 0:
                //starteHochzaehlen für Minutenzahl
                break;
            case 1:
                Intent intent = new Intent(this, ZahlenEinfuegen.class);
                intent.putExtra("MINUTES", minuten);
                intent.putExtra("HIGHSCOREMODE", true);
                startActivity(intent);
                break;
            case 2:
                Intent intent2 = new Intent(this, GroesserKleiner.class);
                intent2.putExtra("MINUTES", minuten);
                intent2.putExtra("HIGHSCOREMODE", true);
                startActivity(intent2);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + spielID);
        }
    }
    public void starteFreiesSpiel(int[] anzahlDurchlaeufeProSpiel){
        for(int i = 0; i < anzahlDurchlaeufeProSpiel.length; i++){
                    switch (i){
                        case 0:
                            //starteHochzaehlenActivity
                            break;
                        case 1:
                            if(anzahlDurchlaeufeProSpiel[1] > 0){
                                Intent intent = new Intent(this,ZahlenEinfuegen.class);
                                intent.putExtra("HIGHSCOREMODE", false);
                                intent.putExtra("DURCHLAEUFE",anzahlDurchlaeufeProSpiel[1]);
                                startActivity(intent);
                            }
                            break;
                        case 2:
                            Intent intent = new Intent(this,GroesserKleiner.class);
                            intent.putExtra("HIGHSCOREMODE", false);
                            intent.putExtra("DURCHLAEUFE",anzahlDurchlaeufeProSpiel[2]);
                            startActivity(intent);
                            break;
                    }
                }

            }
        }

