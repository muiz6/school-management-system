package com.project.schoolsystem.ui.navigation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Navigation {
    @SerializedName("main_title")
    private String title;
    @SerializedName("destinations")
    private List<DestinationModel> destinations;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DestinationModel> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<DestinationModel> destinations) {
        this.destinations = destinations;
    }
}
