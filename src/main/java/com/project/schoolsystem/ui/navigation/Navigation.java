package com.project.schoolsystem.ui.navigation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Navigation {
    @SerializedName("main_title")
    private String title;
    @SerializedName("destinations")
    private List<Destination> destinations;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }
}
