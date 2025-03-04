package com.example.eventtrackingapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventDisplayActivity extends AppCompatActivity implements EventAdapter.OnEventActionListener {

    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private List<Event> events;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);

        dbHelper = new DatabaseHelper(this);

        // Initialize RecyclerView and Buttons
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        Button addEventButton = findViewById(R.id.addEventButton);
        Button sendNotificationsButton = findViewById(R.id.sendNotificationsButton);

        // Initialize events list and adapter
        events = new ArrayList<>();
        eventAdapter = new EventAdapter(events, this);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventRecyclerView.setAdapter(eventAdapter);

        // Load events from database
        loadEventsFromDatabase();

        // Add event button
        addEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EventDetailActivity.class);
            startActivity(intent);
        });

        // Send notifications button
        sendNotificationsButton.setOnClickListener(v -> {
            if (SmsHelper.checkSmsPermission(this)) {
                sendNotifications();
            } else {
                SmsHelper.requestSmsPermission(this);
            }
        });
    }

    private void loadEventsFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_EVENTS, null, null, null, null, null, null);

        events.clear(); // Clear the list to avoid duplication

        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_ID);
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_NAME);
            int timeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_TIME);
            int locationIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_LOCATION);
            int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT_DESCRIPTION);

            if (idIndex != -1 && nameIndex != -1 && timeIndex != -1 && locationIndex != -1 && descriptionIndex != -1) {
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String time = cursor.getString(timeIndex);
                String location = cursor.getString(locationIndex);
                String description = cursor.getString(descriptionIndex);

                events.add(new Event(id, name, time, location, description));
            } else {
                Toast.makeText(this, "Error loading events from database", Toast.LENGTH_SHORT).show();
            }
        }

        cursor.close();
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditEvent(int position) {
        Event event = events.get(position);
        Intent intent = new Intent(this, EventDetailActivity.class);

        intent.putExtra("eventId", event.getId());
        intent.putExtra("eventName", event.getName());
        intent.putExtra("eventLocation", event.getLocation());
        intent.putExtra("eventTime", event.getTime());
        intent.putExtra("eventDescription", event.getDescription());

        startActivity(intent);
    }

    @Override
    public void onDeleteEvent(int position) {
        // Validate the position
        if (position >= 0 && position < events.size()) {
            Event event = events.get(position);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rowsDeleted = db.delete(DatabaseHelper.TABLE_EVENTS,
                    DatabaseHelper.COLUMN_EVENT_ID + "=?",
                    new String[]{String.valueOf(event.getId())});

            if (rowsDeleted > 0) {
                events.remove(position);
                eventAdapter.notifyItemRemoved(position);
                Toast.makeText(this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete event", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid event position", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendNotifications() {
        for (Event event : events) {
            String message = "Reminder: " + event.getName() + " at " + event.getTime();
            String phoneNumber = "1234567890"; // Replace with actual user phone number
            SmsHelper.sendSms(this, phoneNumber, message);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SmsHelper.SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendNotifications();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEventsFromDatabase(); // Reload events when returning to this activity
    }
}
