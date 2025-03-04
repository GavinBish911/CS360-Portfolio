package com.example.eventtrackingapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        EditText usernameField = findViewById(R.id.usernameField);
        EditText passwordField = findViewById(R.id.passwordField);
        Button loginButton = findViewById(R.id.loginButton);
        Button createAccountButton = findViewById(R.id.createAccountButton);

        // Handle login
        loginButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (authenticateUser(username, password)) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, EventDisplayActivity.class));
            } else {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle account creation
        createAccountButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (createUserAccount(username, password)) {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Username Already Exists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USERNAME, DatabaseHelper.COLUMN_PASSWORD},
                DatabaseHelper.COLUMN_USERNAME + "=? AND " + DatabaseHelper.COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        boolean isValid = cursor.moveToFirst();
        cursor.close();
        return isValid;
    }

    private boolean createUserAccount(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        try {
            db.insertOrThrow(DatabaseHelper.TABLE_USERS, null, values);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
