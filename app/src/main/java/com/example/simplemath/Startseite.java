package com.example.simplemath;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import static com.example.simplemath.R.color.unserBlau;

public class Startseite extends AppCompatActivity {
    private ImageButton imageButton;
    private Button user;
    LinearLayout linearLayoutUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startseite);
        imageButton = findViewById(R.id.addUserButton);
        linearLayoutUsers = findViewById(R.id.linearLayoutUsers);
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
        //Intent intent = new Intent(this, UserErstellen.class);
        //startActivity(intent);
        addUser("penis");
    }

    public void openSpielauswahl() {
        Intent intent = new Intent(this, Spielauswahl.class);
        startActivity(intent);
    }

    public void addUser(String name){
        Button tempButton = new MaterialButton(this);
        tempButton.setText(name);
        tempButton.setTextSize(25);
        tempButton.setBackgroundColor(getResources().getColor(unserBlau));
        linearLayoutUsers.addView(tempButton);
    }
}





