package com.project.schoolsystem.ui.navigation;

import com.google.gson.annotations.SerializedName;

public class Destination {
    @SerializedName("glyphName")
    private String glyphName;
    @SerializedName("title")
    private String title;
    @SerializedName("fxmlPath")
    private String fxmlPath;
    @SerializedName("id")
    private String id;

    public String getGlyphName() {
        return glyphName;
    }

    public void setGlyphName(String glyphName) {
        this.glyphName = glyphName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public void setFxmlPath(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
