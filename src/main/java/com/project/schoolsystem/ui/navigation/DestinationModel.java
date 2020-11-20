package com.project.schoolsystem.ui.navigation;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class DestinationModel {
    @SerializedName("glyphName")
    private String glyphName;
    @SerializedName("title")
    private String title;
    @SerializedName("fxmlPath")
    private String fxmlPath;
    @SerializedName("id")
    private String id;

    public DestinationModel() {
    }

    public DestinationModel(@Nullable String id,
            @Nullable String title,
            @Nullable String fxmlPath,
            @Nullable String glyphName) {
        this.id = id;
        this.title = title;
        this.fxmlPath = fxmlPath;
        this.glyphName = glyphName;
    }

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
