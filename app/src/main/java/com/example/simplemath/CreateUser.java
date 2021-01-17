package com.example.simplemath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

/**
 * Class for the Activity, that creates a new User.
 */
public class CreateUser extends AppCompatActivity {
    private EditText username;

    /**
     * Finds all needed Views from the layout and sets OnClickListeners for the button to create the
     * user.
     *
     * @param savedInstanceState android related
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Button button = findViewById(R.id.createUserButton);
        username = findViewById(R.id.enterUsername);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Checks whether a user with the entered name already exist. If not a new user with a
             * unique ID gets created.
             * @param v View which got clicked.
             */
            @Override
            public void onClick(View v) {
                if (isNewUsername(username.getText().toString())) {
                    SharedPreferences.Editor editor = getSharedPreferences("usernamePrefs", MODE_PRIVATE).edit();
                    SharedPreferences.Editor editorIds = getSharedPreferences("usernameIds", MODE_PRIVATE).edit();
                    SharedPreferences ids = getSharedPreferences("usernameIds", MODE_PRIVATE);
                    if (ids.getInt("id", 0) == 0) {
                        editorIds.putInt("id", 1);
                        editorIds.apply();
                    }
                    int currentId = ids.getInt("id", 0);

                    editor.putString(String.valueOf(currentId), username.getText().toString());
                    editor.apply();


                    Intent result = new Intent();
                    result.putExtra("NEWUSERID", currentId);
                    setResult(0, result);

                    currentId += 1;
                    editorIds.putInt("id", currentId);
                    editorIds.commit();
                    finish();
                } else {
                    username.setText("");
                    Toast.makeText(getApplicationContext(), "Username schon vergeben", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Method which checks whether a username is already in the Shared Preferences file.
     *
     * @param username Username that gets checked.
     * @return True when when username is new. False when it got found in the Shared Preferences with all usernames.
     */
    public boolean isNewUsername(String username) {
        SharedPreferences prefs = getSharedPreferences("usernamePrefs", MODE_PRIVATE);
        Map<String, ?> allUsersMap = prefs.getAll();

        for (String key : allUsersMap.keySet()) {
            if (username.toLowerCase().equals(((String) allUsersMap.get(key)).toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}