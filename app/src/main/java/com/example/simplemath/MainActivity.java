package com.example.simplemath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button singleplayer, multiplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        singleplayer = findViewById(R.id.singleplayer);
        singleplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStartseite();
            }
        });
        multiplayer = findViewById(R.id.multiplayer);
        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMultiplayer();
            }
        });
    }

    private void openMultiplayer() {
        Intent intent = new Intent(this, Multiplayer.class);
        startActivity(intent);
    }

    public void openStartseite() {

        SharedPreferences.Editor editor = getSharedPreferences("hochzaehlenHighscore", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

        editor = getSharedPreferences("zahlenEinfuegenHighscore", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

        editor = getSharedPreferences("groesserKleinerHighscore", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(this, Startseite.class);
        startActivity(intent);
    }

}