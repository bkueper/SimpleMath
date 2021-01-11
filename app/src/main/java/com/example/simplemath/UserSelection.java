package com.example.simplemath;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Map;

import static com.example.simplemath.R.color.purple_200;
import static com.example.simplemath.R.color.unserBlau;

public class UserSelection extends AppCompatActivity {
    LinearLayout linearLayoutUsers;
    Button setNewUserName, deleteUsername;
    EditText enterUsername;
    String editUsername;
    Button editButton;
    ArrayList<Button> buttonsInLinear = new ArrayList<>();
    private ImageButton imageButton;

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        imageButton = findViewById(R.id.addUserButton);
        linearLayoutUsers = findViewById(R.id.linearLayoutUsers);
        setNewUserName = findViewById(R.id.setNewUsername);
        setNewUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = enterUsername.getText().toString();
                if (isNewUsername(newUsername)) {
                    editButton.setText(newUsername);
                    replaceName(editUsername, newUsername);
                    enterUsername.setText("");
                    closeEditUsernameMenu(editButton, editUsername);
                } else {
                    enterUsername.setText("");
                    Toast.makeText(getApplicationContext(), "Username schon vergeben", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteUsername = findViewById(R.id.deleteUsername);
        deleteUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeName(editUsername);
                linearLayoutUsers.removeView(editButton);
                Toast.makeText(getApplicationContext(), editUsername + " entfernt", Toast.LENGTH_SHORT).show();
                closeEditUsernameMenu(editButton, editUsername);
            }
        });
        enterUsername = findViewById(R.id.enterUsername);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateUser();
            }
        });

        SharedPreferences prefs = getSharedPreferences("usernamePrefs", MODE_PRIVATE);
        Map<String, ?> allUsersMap = prefs.getAll();

        for (String key : allUsersMap.keySet()) {
            addUser((String) allUsersMap.get(key));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int newestId = data.getIntExtra("NEWUSERID", 0);
        if (newestId != 0) {
            SharedPreferences prefs = getSharedPreferences("usernamePrefs", MODE_PRIVATE);
            String newName = prefs.getString(String.valueOf(newestId), "");
            addUser(newName);
        }
    }

    public void openCreateUser() {
        Intent intent = new Intent(this, CreateUser.class);
        startActivityForResult(intent, 0);
    }

    public void openGameSelection() {
        Intent intent = new Intent(this, GameSelection.class);
        startActivity(intent);
    }

    public void addUser(String name) {
        int width = Math.round(convertDpToPixel(200, getApplicationContext()));
        int height = Math.round(convertDpToPixel(70, getApplicationContext()));
        Button tempButton = new MaterialButton(this);
        tempButton.setText(name);
        tempButton.setTextSize(25);
        tempButton.setBackgroundColor(getResources().getColor(unserBlau));
        tempButton.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        setCustomOnclick(tempButton, name);
        tempButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                for (Button button : buttonsInLinear) {
                    button.setBackgroundColor(getResources().getColor(unserBlau));
                }
                tempButton.setBackgroundColor(getResources().getColor(purple_200));

                tempButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeEditUsernameMenu(editButton, editUsername);
                    }
                });
                //tempButton.setOnClickListener(null);
                openEditUsernameMenu(tempButton, name);

                return true;
            }
        });
        linearLayoutUsers.addView(tempButton);
        buttonsInLinear.add(tempButton);
    }

    public void openEditUsernameMenu(Button button, String name) {
        setNewUserName.setVisibility(View.VISIBLE);
        deleteUsername.setVisibility(View.VISIBLE);
        enterUsername.setVisibility(View.VISIBLE);
        editUsername = name;
        editButton = button;
    }

    public void closeEditUsernameMenu(Button button, String name) {
        setNewUserName.setVisibility(View.INVISIBLE);
        deleteUsername.setVisibility(View.INVISIBLE);
        enterUsername.setVisibility(View.INVISIBLE);
        editUsername = null;
        editButton = null;
        setCustomOnclick(button, name);
        button.setBackgroundColor(getResources().getColor(unserBlau));
    }

    public void setCustomOnclick(Button button, String name) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor prefs = getSharedPreferences("currentUser", MODE_PRIVATE).edit();
                prefs.clear();
                prefs.putString("username", button.getText().toString());
                prefs.commit();
                openGameSelection();
            }
        });
    }

    public boolean isNewUsername(String username) {
        if (username.equals("")) {
            return false;
        }
        SharedPreferences prefs = getSharedPreferences("usernamePrefs", MODE_PRIVATE);
        Map<String, ?> allUsersMap = prefs.getAll();

        for (String key : allUsersMap.keySet()) {
            if (username.toLowerCase().equals(((String) allUsersMap.get(key)).toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public void removeName(String name) {
        SharedPreferences.Editor editor = getSharedPreferences("groesserKleinerHighscore", MODE_PRIVATE).edit();
        editor.remove(name);
        editor.commit();
        editor = getSharedPreferences("zahlenEinfuegenHighscore", MODE_PRIVATE).edit();
        editor.remove(name);
        editor.commit();
        editor = getSharedPreferences("hochzaehlenHighscore", MODE_PRIVATE).edit();
        editor.remove(name);
        editor.commit();
        SharedPreferences prefs = getSharedPreferences("usernamePrefs", MODE_PRIVATE);
        editor = getSharedPreferences("usernamePrefs", MODE_PRIVATE).edit();

        Map<String, ?> allUsersMap = prefs.getAll();
        for (String key : allUsersMap.keySet()) {
            if (prefs.getString(key, "").equals(name)) {
                editor.remove(key);
            }
        }
        editor.commit();
    }

    public void replaceName(String alt, String neu) {
        SharedPreferences.Editor editor = getSharedPreferences("groesserKleinerHighscore", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("groesserKleinerHighscore", MODE_PRIVATE);
        int score = prefs.getInt(alt, 0);
        editor.remove(alt);
        editor.putInt(neu, score);
        editor.commit();
        editor = getSharedPreferences("zahlenEinfuegenHighscore", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("zahlenEinfuegenHighscore", MODE_PRIVATE);
        score = prefs.getInt(alt, 0);
        editor.remove(alt);
        editor.putInt(neu, score);
        editor.commit();
        editor = getSharedPreferences("hochzaehlenHighscore", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("hochzaehlenHighscore", MODE_PRIVATE);
        score = prefs.getInt(alt, 0);
        editor.remove(alt);
        editor.putInt(neu, score);
        editor.commit();

        editor = getSharedPreferences("usernamePrefs", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("usernamePrefs", MODE_PRIVATE);

        Map<String, ?> allUsersMap = prefs.getAll();
        for (String key : allUsersMap.keySet()) {
            if (prefs.getString(key, "").equals(alt)) {
                editor.remove(key);
                editor.putString(key, neu);
            }
        }
        editor.commit();
    }

}





