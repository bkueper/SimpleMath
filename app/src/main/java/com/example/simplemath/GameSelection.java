package com.example.simplemath;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.String.format;
import static java.lang.String.valueOf;
/**
 * This is the Javaclass for the activity "GameSelection".
 * @author Bjarne Küper and Sascha Rührup
 *
 */
public class GameSelection extends AppCompatActivity implements View.OnClickListener {
    private int gameID;
    private Button highscore, freeGame, startButton;
    private Button hochzaehlenPlus, hochzaehlenMinus, zahlenEinfuegenPlus, zahlenEinfuegenMinus, groesserKleinerPlus, groesserKleinerMinus;
    private SeekBar seekBar;
    private TextView time, helloUser;
    private ImageButton hochzaehlen, zahlenEinfuegen, groesserKleiner;
    private TextView countForHochzaehlen, countForZahlenEinfuegen, countForGroesserKleiner;
    private boolean highscoreMode = false;

    /**
     * The onCreate method sets the Content and finds the Views in the layout file.
     * The selected user is greeted. An onProgressChanged listener is set for the progressbar.
     * All buttons get an onCLick listener, implemented by this class.
     * Finally the state of the selected games is checked.
     * @param savedInstanceState android related
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);
        helloUser = findViewById(R.id.halloUser);
        helloUser.setText("Hallo " + getSharedPreferences("currentUser", MODE_PRIVATE).getString("username", "").toUpperCase());
        countForZahlenEinfuegen = findViewById(R.id.countForZahlenEinfuegen);
        countForGroesserKleiner = findViewById(R.id.countForGroesserKleiner);
        countForHochzaehlen = findViewById(R.id.countForHochzählen);
        highscore = findViewById(R.id.highscore);
        highscore.setOnClickListener(this);
        freeGame = findViewById(R.id.freeGame);
        freeGame.setOnClickListener(this);
        seekBar = findViewById(R.id.gameDuration);
        seekBar.setMax(14);
        seekBar.setProgress(2);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int totalProgress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                time.setText(format("%d Min", progress + 1));
                totalProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                time.setText(format("%d Min", totalProgress + 1));
            }
        });
        time = findViewById(R.id.angezeigteZeit);
        time.setText(format("%d Min", seekBar.getProgress() + 1));
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

    /**
     * Increases the count of the TextView by getting its text, increasing it by one,
     * if it is not already nine or higher and setting the views text with the new value.
     * @param tw TextView to increase the count for.
     */
    public void countPlus(TextView tw) {
        int count = Integer.parseInt(tw.getText().toString());
        if (count < 9) {
            count++;
            tw.setText(valueOf(count));
        }
    }
    /**
     * Decreases the count of the TextView by getting its text, decreasing it by one,
     * if it is not already zero or lower and setting the views text with the new value.
     * @param tw TextView to decrease the count for.
     */
    public void countMinus(TextView tw) {
        int count = Integer.parseInt(tw.getText().toString());
        if (count > 0) {
            count--;
            tw.setText(valueOf(count));
        }
    }

    /**
     * Checks the given TextView. Depending on its count the background of the given ImageButton is set.
     * @param tw TextView, containing a count.
     * @param ib ImageButton of the related game.
     */
    public void checkCount(TextView tw, ImageButton ib) {
        if (Integer.parseInt(tw.getText().toString()) > 0) {
            ib.setBackground(getResources().getDrawable(R.drawable.default_button_pressed));
        } else {
            ib.setBackground(getResources().getDrawable(R.drawable.default_button));
        }
    }

    /**
     * Checks the count of all games and sets the startButton to visible,
     * if any have a count larger than zero, otherwise the startButton is set to "GONE".
     * Also checks the counts of all games.
     */
    public void checkAllCounts() {
        if (Integer.parseInt(countForHochzaehlen.getText().toString()) > 0 || Integer.parseInt(countForZahlenEinfuegen.getText().toString()) > 0 || Integer.parseInt(countForGroesserKleiner.getText().toString()) > 0) {
            startButton.setVisibility(View.VISIBLE);
        } else {
            startButton.setVisibility(View.GONE);
        }
        checkCount(countForHochzaehlen, hochzaehlen);
        checkCount(countForZahlenEinfuegen, zahlenEinfuegen);
        checkCount(countForGroesserKleiner, groesserKleiner);

    }

    /**
     * Sets the different functions for the buttons, which update the visibility of Views and call functions.
     * @param v View that is clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.highscore:
                highscoreMode = true;
                highscore.setBackgroundColor(Color.GREEN);
                freeGame.setBackgroundColor(Color.parseColor("#2196F3"));
                seekBar.setVisibility(View.VISIBLE);
                time.setVisibility(View.VISIBLE);
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
                hochzaehlen.setBackground(getResources().getDrawable(R.drawable.default_button));
                zahlenEinfuegen.setBackground(getResources().getDrawable(R.drawable.default_button));
                groesserKleiner.setBackground(getResources().getDrawable(R.drawable.default_button));
                break;
            case R.id.freeGame:
                checkAllCounts();
                highscoreMode = false;
                highscore.setBackgroundColor(Color.parseColor("#2196F3"));
                freeGame.setBackgroundColor(Color.GREEN);
                seekBar.setVisibility(View.GONE);
                time.setVisibility(View.GONE);
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
                if (highscoreMode) {
                    startButton.setVisibility(View.VISIBLE);
                    hochzaehlen.setBackground(getResources().getDrawable(R.drawable.default_button_pressed));
                    zahlenEinfuegen.setBackground(getResources().getDrawable(R.drawable.default_button));
                    groesserKleiner.setBackground(getResources().getDrawable(R.drawable.default_button));
                    gameID = 2;
                }
                break;
            case R.id.zahlenEinfuegen:
                if (highscoreMode) {
                    startButton.setVisibility(View.VISIBLE);
                    hochzaehlen.setBackground(getResources().getDrawable(R.drawable.default_button));
                    zahlenEinfuegen.setBackground(getResources().getDrawable(R.drawable.default_button_pressed));
                    groesserKleiner.setBackground(getResources().getDrawable(R.drawable.default_button));
                    gameID = 1;
                }
                break;
            case R.id.groesserKleiner:
                if (highscoreMode) {
                    startButton.setVisibility(View.VISIBLE);
                    hochzaehlen.setBackground(getResources().getDrawable(R.drawable.default_button));
                    zahlenEinfuegen.setBackground(getResources().getDrawable(R.drawable.default_button));
                    groesserKleiner.setBackground(getResources().getDrawable(R.drawable.default_button_pressed));
                    gameID = 0;
                }
                break;
            case R.id.plusHochzaehlen:
                countPlus(countForHochzaehlen);
                checkCount(countForHochzaehlen, hochzaehlen);
                break;
            case R.id.minusHochzaehlen:
                countMinus(countForHochzaehlen);
                checkCount(countForHochzaehlen, hochzaehlen);
                break;
            case R.id.plusZahlenEinfuegen:
                countPlus(countForZahlenEinfuegen);
                checkCount(countForZahlenEinfuegen, zahlenEinfuegen);
                break;
            case R.id.minusZahlenEinfuegen:
                countMinus(countForZahlenEinfuegen);
                checkCount(countForZahlenEinfuegen, zahlenEinfuegen);
                break;
            case R.id.plusGroesserKleiner:
                countPlus(countForGroesserKleiner);
                checkCount(countForGroesserKleiner, groesserKleiner);
                break;
            case R.id.minusGroesserKleiner:
                countMinus(countForGroesserKleiner);
                checkCount(countForGroesserKleiner, groesserKleiner);
                break;
            case R.id.startButton:
                if (highscoreMode) {
                    int minutes = seekBar.getProgress() + 1;
                    startHighscoreGame(minutes, gameID);
                } else {
                    int[] rounds = new int[3];
                    rounds[0] = Integer.parseInt(countForHochzaehlen.getText().toString());
                    rounds[1] = Integer.parseInt(countForZahlenEinfuegen.getText().toString());
                    rounds[2] = Integer.parseInt(countForGroesserKleiner.getText().toString());
                    startFreeGame(rounds);
                }
            default:
                break;
        }
    }

    /**
     * Starts a new game in Highscore mode.
     * @param minutes time the game should last.
     * @param spielID game that gets started.
     */
    public void startHighscoreGame(int minutes, int spielID) {
        switch (spielID) {
            case 0:
                Intent intent = new Intent(this, GroesserKleiner.class);
                intent.putExtra("MINUTES", minutes);
                intent.putExtra("HIGHSCOREMODE", true);
                intent.putExtra("SPIELID", spielID);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(this, ZahlenEinfuegen.class);
                intent2.putExtra("MINUTES", minutes);
                intent2.putExtra("HIGHSCOREMODE", true);
                intent2.putExtra("SPIELID", spielID);
                startActivity(intent2);
                break;
            case 2:
                Intent intent3 = new Intent(this, Hochzaehlen.class);
                intent3.putExtra("MINUTES", minutes);
                intent3.putExtra("HIGHSCOREMODE", true);
                intent3.putExtra("SPIELID", spielID);
                startActivity(intent3);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + spielID);
        }
    }

    /**
     * Starts one or multiple games with one or many rounds each.
     * @param roundsPerGame int array, each index relates to a game, the value defines the number of rounds.
     */
    public void startFreeGame(int[] roundsPerGame) {
        for (int i = 0; i < roundsPerGame.length; i++) {
            switch (i) {
                case 0:
                    if(roundsPerGame[2] > 0) {
                        Intent intent = new Intent(this, GroesserKleiner.class);
                        intent.putExtra("HIGHSCOREMODE", false);
                        intent.putExtra("DURCHLAEUFE", roundsPerGame[2]);
                        startActivity(intent);
                    }
                    break;
                case 1:
                    if (roundsPerGame[1] > 0) {
                        Intent intent2 = new Intent(this, ZahlenEinfuegen.class);
                        intent2.putExtra("HIGHSCOREMODE", false);
                        intent2.putExtra("DURCHLAEUFE", roundsPerGame[1]);
                        startActivity(intent2);
                    }
                    break;
                case 2:
                    if(roundsPerGame[0] > 0) {
                        Intent intent3 = new Intent(this, Hochzaehlen.class);
                        intent3.putExtra("HIGHSCOREMODE", false);
                        intent3.putExtra("DURCHLAEUFE", roundsPerGame[0]);
                        startActivity(intent3);
                    }
                    break;
            }
        }

    }
}

