package com.example.simplemath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class Hochzaehlen extends AppCompatActivity implements View.OnClickListener {
    private GridLayout gridLayout;
    public ArrayList<Button> allButtons = new ArrayList<>();
    private int currentPosition, minPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hochzaehlen);
        gridLayout = findViewById(R.id.gridLayout);
        for(int i = 0; i<25; i++){
            addButton(i);
        }
    }
    public void addButton(int i) {
            Button button = new Button(this);
            int width = Math.round(convertDpToPixel(50, getApplicationContext()));
            int height = Math.round(convertDpToPixel(50, getApplicationContext()));
            button.setBackgroundResource(R.drawable.round_button);
            button.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            button.setText(String.valueOf(i));
            button.setOnClickListener(this);
            button.setId(i);
            allButtons.add(button);
            gridLayout.addView(button);
    }
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    public void onClick(View v) {
        int position = v.getId();
        if(position<currentPosition){
            for(int i = position+1; i <= currentPosition;i++){
                allButtons.get(i).setBackgroundResource(R.drawable.round_button);
            }
        }else if(position>currentPosition){
            for(int i = 0;i <= position;i++){
                allButtons.get(i).setBackgroundResource(R.drawable.round_button_gefaerbt);
            }
        }
        currentPosition = position;
    }
}