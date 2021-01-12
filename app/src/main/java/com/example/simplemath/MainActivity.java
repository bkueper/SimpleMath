package com.example.simplemath;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class for the MainActivity. Has two Buttons which decide,
 * whether a multiplayer round gets started or the game selection
 * for the singleplayer.
 * @author Bjarne Küper and Sascha Rührup
 */
public class MainActivity extends AppCompatActivity {
    private Button singleplayer, multiplayer;
    /**
     * Creates the 2 Buttons and overrides onClick methods.
     * @param savedInstanceState android related
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        singleplayer = findViewById(R.id.singleplayer);
        singleplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameSelection();
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
    /**
     * Opens the Multiplayer Activity.
     */
    private void openMultiplayer() {
        Intent intent = new Intent(this, Multiplayer.class);
        startActivity(intent);
    }
    /**
     * Opens the singleplayer game selection Activity.
     */
    public void openGameSelection() {
        Intent intent = new Intent(this, UserSelection.class);
        startActivity(intent);
    }

}