package com.example.simplemath;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Startseite extends AppCompatActivity {
    private ImageButton imageButton;
    private Button user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startseite);
        imageButton = findViewById(R.id.addUserButton);
        user = findViewById(R.id.user1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserErstellen();
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSpielauswahl();
            }
        });

    }

    public void openUserErstellen() {
        Intent intent = new Intent(this, UserErstellen.class);
        startActivity(intent);
    }

    public void openSpielauswahl() {
        Intent intent = new Intent(this, Spielauswahl.class);
        startActivity(intent);
    }
}