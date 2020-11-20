package com.project.schoolsystem;

import javax.annotation.Nullable;

public class PreferenceModel {
    private String organizationTitle = "Organization";

    public PreferenceModel() {
    }

    public PreferenceModel(String organizationTitle) {
        this.organizationTitle = organizationTitle;
    }

    @Nullable
    public String getOrganizationTitle() {
        return organizationTitle;
    }

    public void setOrganizationTitle(@Nullable String organizationTitle) {
        this.organizationTitle = organizationTitle;
    }
}
