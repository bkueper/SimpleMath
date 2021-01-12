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

/**
 * UserSelection allows the User to select a select a profile, edit it or delete it.
 * This class generates all clickable user buttons and allows to edit or delete them.
 * @author Bjarne Küper and Sascha Rührup
 */
public class UserSelection extends AppCompatActivity {
    private LinearLayout linearLayoutUsers;
    private Button setNewUserName, deleteUsername;
    private EditText enterUsername;
    private String editUsername;
    private Button editButton;
    private ArrayList<Button> buttonsInLinear = new ArrayList<>();
    private ImageButton imageButton;

    /**
     * Converts a number of dp to a number of pixels.
     * @param dp number of dp to convert
     * @param context required to get access to the devices display dimensions
     * @return dp converted to pixels depending on the devices display dimensions
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * The onCreate method sets the Content and finds the Views in the layout file.
     * It also sets onCLick listeners for editing and deleting a user as well as for creating a new one.
     * Finally it creates a new user button for every user in shared preferences.
     * @param savedInstanceState android related
     */
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
                    closeEditUsernameMenu(editButton);
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
                closeEditUsernameMenu(editButton);
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

    /**
     * Receives an id related to the newest user created in "CreateUser" and adds it to the user selection.
     * @param requestCode start code
     * @param resultCode received code
     * @param data containing information as intent.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            int newestId = data.getIntExtra("NEWUSERID", 0);
            if (newestId != 0) {
                SharedPreferences prefs = getSharedPreferences("usernamePrefs", MODE_PRIVATE);
                String newName = prefs.getString(String.valueOf(newestId), "");
                addUser(newName);
            }
        }
    }

    /**
     * starts the Activity CreateUser.
     */
    public void openCreateUser() {
        Intent intent = new Intent(this, CreateUser.class);
        startActivityForResult(intent, 0);
    }

    /**
     * starts the Activity GameSelection.
     */
    public void openGameSelection() {
        Intent intent = new Intent(this, GameSelection.class);
        startActivity(intent);
    }

    /**
     * Receives a String and creates a button. Sets onLongClick to open edit and deletion context.
     * Calls another method to set an onClick that starts the game selection.
     * Finally adds the button to the layout and a separate list.
     * @param name of user to add to the user selection.
     */
    public void addUser(String name) {
        int width = Math.round(convertDpToPixel(200, getApplicationContext()));
        int height = Math.round(convertDpToPixel(70, getApplicationContext()));
        Button tempButton = new MaterialButton(this);
        tempButton.setText(name);
        tempButton.setTextSize(25);
        tempButton.setBackgroundColor(getResources().getColor(unserBlau));
        tempButton.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        setCustomOnclick(tempButton);
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
                        closeEditUsernameMenu(editButton);
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

    /**
     * makes the edit and delete buttons visible and saves the user it is called from.
     * @param button clicked button to edit.
     * @param name name of the user, action is started on.
     */
    public void openEditUsernameMenu(Button button, String name) {
        setNewUserName.setVisibility(View.VISIBLE);
        deleteUsername.setVisibility(View.VISIBLE);
        enterUsername.setVisibility(View.VISIBLE);
        editUsername = name;
        editButton = button;
    }

    /**
     * disables the edit and delete context and saves the given name on the given button.
     * Finally resets the buttons color to default.
     * Links a given name to the button.
     * @param button current button
     */
    public void closeEditUsernameMenu(Button button) {
        setNewUserName.setVisibility(View.INVISIBLE);
        deleteUsername.setVisibility(View.INVISIBLE);
        enterUsername.setVisibility(View.INVISIBLE);
        editUsername = null;
        editButton = null;
        setCustomOnclick(button);
        button.setBackgroundColor(getResources().getColor(unserBlau));
    }

    /**
     * Sets onClick for the given button, which saves the username as selected user in shared preferences
     * and starts the game selection.
     * @param button current button
     */
    public void setCustomOnclick(Button button) {
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

    /**
     * Checks if the given username is already present.
     * @param username username to check
     * @return true if username does not already exist, false otherwise
     */
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

    /**
     * Removes a given name from all Highscore lists as well as from the list of users in shared prefs.
     * @param name Name to remove,
     */
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

    /**
     * Edits the shared preferences for the different Highscore lists and the list of users and changes the name.
     * @param currentUsername name to replace.
     * @param newUsername new name set.
     */
    public void replaceName(String currentUsername, String newUsername) {
        SharedPreferences.Editor editor = getSharedPreferences("groesserKleinerHighscore", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("groesserKleinerHighscore", MODE_PRIVATE);
        int score = prefs.getInt(currentUsername, 0);
        editor.remove(currentUsername);
        editor.putInt(newUsername, score);
        editor.commit();
        editor = getSharedPreferences("zahlenEinfuegenHighscore", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("zahlenEinfuegenHighscore", MODE_PRIVATE);
        score = prefs.getInt(currentUsername, 0);
        editor.remove(currentUsername);
        editor.putInt(newUsername, score);
        editor.commit();
        editor = getSharedPreferences("hochzaehlenHighscore", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("hochzaehlenHighscore", MODE_PRIVATE);
        score = prefs.getInt(currentUsername, 0);
        editor.remove(currentUsername);
        editor.putInt(newUsername, score);
        editor.commit();

        editor = getSharedPreferences("usernamePrefs", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("usernamePrefs", MODE_PRIVATE);

        Map<String, ?> allUsersMap = prefs.getAll();
        for (String key : allUsersMap.keySet()) {
            if (prefs.getString(key, "").equals(currentUsername)) {
                editor.remove(key);
                editor.putString(key, newUsername);
            }
        }
        editor.commit();
    }

}





