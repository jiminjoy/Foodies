package com.example.foodies.structures;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class Event {

    private Timestamp date;
    private String description;
    private List<String> going;
    private String imageUrl;
    private String name;
    private DocumentReference organizer;
    private String location;

    public Event() {

    }

    public Event(Timestamp date, String description, List<String> going, String imageUrl, String location, String name, DocumentReference organizer) {
        this.date = date;
        this.description = description;
        this.going = going;
        this.imageUrl = imageUrl;
        this.location = location;
        this.name = name;
        this.organizer = organizer;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getGoing() {
        return going;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public DocumentReference getOrganizer() {
        return organizer;
    }

    public String getLocation() {
        return location;
    }
}
