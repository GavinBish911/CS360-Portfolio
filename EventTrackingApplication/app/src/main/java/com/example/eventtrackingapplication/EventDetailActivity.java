package com.example.eventtrackingapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EventDetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText eventNameField, eventLocationField, eventTimeField, eventDescriptionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        dbHelper = new DatabaseHelper(this);

        // Initialize UI components
        eventNameField = findViewById(R.id.eventNameField);
        eventLocationField = findViewById(R.id.eventLocationField);
        eventTimeField = findViewById(R.id.eventTimeField);
        eventDescriptionField = findViewById(R.id.eventDescriptionField);
        Button saveButton = findViewById(R.id.saveButton);

        // Retrieve event ID from the Intent
        int eventId = getIntent().getIntExtra("eventId", -1); // Default -1 indicates a new event

        // Load event details if editing an existing event
        if (eventId != -1) {
            loadEventDetails(eventId);
        }

        // Set Save button click listener
        saveButton.setOnClickListener(v -> saveEvent(eventId));
    }

    // Method to load event details from the database
    private void loadEventDetails(int eventId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_EVENTS,
                new String[]{
                        DatabaseHelper.COLUMN_EVENT_NAME,
                        DatabaseHelper.COLUMN_EVENT_LOCATION,
                        DatabaseHelper.COLUMN_EVENT_TIME,
                        DatabaseHelper.COLUMN_EVENT_DESCRIPTION
                },
                DatabaseHelper.COLUMN_EVENT_ID + "=?",
                new String[]{String.valueOf(eventId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_NAME);
            int locationIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_LOCATION);
            int timeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_TIME);
            int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_DESCRIPTION);

            if (nameIndex != -1) {
                eventNameField.setText(cursor.getString(nameIndex));
            }
            if (locationIndex != -1) {
                eventLocationField.setText(cursor.getString(locationIndex));
            }
            if (timeIndex != -1) {
                eventTimeField.setText(cursor.getString(timeIndex));
            }
            if (descriptionIndex != -1) {
                eventDescriptionField.setText(cursor.getString(descriptionIndex));
            }
        }
        cursor.close();
    }

    // Method to save or update an event
    private void saveEvent(int eventId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Collect data from fields
        String eventName = eventNameField.getText().toString().trim();
        String eventLocation = eventLocationField.getText().toString().trim();
        String eventTime = eventTimeField.getText().toString().trim();
        String eventDescription = eventDescriptionField.getText().toString().trim();

        // Validate required fields
        if (eventName.isEmpty() || eventTime.isEmpty()) {
            Toast.makeText(this, "Event name and time are required", Toast.LENGTH_SHORT).show();
            return;
        }

        values.put(DatabaseHelper.COLUMN_EVENT_NAME, eventName);
        values.put(DatabaseHelper.COLUMN_EVENT_LOCATION, eventLocation);
        values.put(DatabaseHelper.COLUMN_EVENT_TIME, eventTime);
        values.put(DatabaseHelper.COLUMN_EVENT_DESCRIPTION, eventDescription);

        if (eventId == -1) {
            // Insert new event
            long newRowId = db.insert(DatabaseHelper.TABLE_EVENTS, null, values);
            if (newRowId != -1) {
                Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add event", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update existing event
            int rowsUpdated = db.update(DatabaseHelper.TABLE_EVENTS, values,
                    DatabaseHelper.COLUMN_EVENT_ID + "=?",
                    new String[]{String.valueOf(eventId)});
            if (rowsUpdated > 0) {
                Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update event", Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}
