package com.example.foodies.structures;

import java.util.List;

public class FoodiesUser {

    private String bio;
    private String email;
    private String gender;
    private String imageUrl;
    private String name;
    private List<String> preferences;

    public FoodiesUser(String bio, String email, String gender, String imageUrl, String name, List<String> preferences) {
        this.bio = bio;
        this.email = email;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.name = name;
        this.preferences = preferences;
    }

    public FoodiesUser() {}


    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
