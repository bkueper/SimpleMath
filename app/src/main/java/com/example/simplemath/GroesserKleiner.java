package com.example.simplemath;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class GroesserKleiner extends AppCompatActivity implements View.OnTouchListener {
    private Button ergebnis1,ergebnis2,ergebnis3,ergebnis4,ergebnis5;
    float dX, dY;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groesser_kleiner);
        ergebnis1 = findViewById(R.id.groesserKleinerErgebnis1);
        ergebnis1.setOnTouchListener(this);
        ergebnis2 = findViewById(R.id.groesserKleinerErgebnis2);
        ergebnis2.setOnTouchListener(this);
        ergebnis3 = findViewById(R.id.groesserKleinerErgebnis3);
        ergebnis3.setOnTouchListener(this);
        ergebnis4 = findViewById(R.id.groesserKleinerErgebnis4);
        ergebnis4.setOnTouchListener(this);
        ergebnis5 = findViewById(R.id.groesserKleinerErgebnis5);
        ergebnis5.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                dX = v.getX() - event.getRawX();
                dY = v.getY() - event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:

                v.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
                break;
            default:
                return false;
        }
        return true;
    }
}