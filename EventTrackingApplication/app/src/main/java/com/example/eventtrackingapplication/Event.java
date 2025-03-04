package com.example.eventtrackingapplication;

public class Event {
    private int id;
    private String name;
    private String time;
    private String location;
    private String description;

    public Event(int id, String name, String time, String location, String description) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.location = location;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}

