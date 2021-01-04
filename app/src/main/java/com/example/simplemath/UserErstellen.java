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

public class UserErstellen extends AppCompatActivity {
    private Button button;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_erstellen);
        button = findViewById(R.id.userErstellenButton);
        username = findViewById(R.id.enterUsername);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewUsername(username.getText().toString())) {
                    SharedPreferences.Editor editor = getSharedPreferences("usernamePrefs", MODE_PRIVATE).edit();
                    SharedPreferences.Editor editorIds = getSharedPreferences("usernameIds", MODE_PRIVATE).edit();
                    SharedPreferences ids = getSharedPreferences("usernameIds", MODE_PRIVATE);
                    if (ids.getInt("id", 0) == 0) {
                        editorIds.putInt("id", 1);
                        editorIds.commit();
                    }
                    int currentId = ids.getInt("id", 0);

                    editor.putString(String.valueOf(currentId), username.getText().toString());
                    editor.commit();


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