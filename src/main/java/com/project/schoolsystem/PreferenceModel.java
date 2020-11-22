package com.project.schoolsystem;

import io.reactivex.annotations.Nullable;

public class PreferenceModel {
    private String organizationTitle = "Organization";
    private String defaultTeacherPwd = "12345";

    public PreferenceModel() {}

    public PreferenceModel(@Nullable String organizationTitle, @Nullable String defaultTeacherPwd) {
        this.organizationTitle = organizationTitle;
        this.defaultTeacherPwd = defaultTeacherPwd;
    }

    @Nullable
    public String getOrganizationTitle() {
        return organizationTitle;
    }

    public void setOrganizationTitle(@Nullable String organizationTitle) {
        this.organizationTitle = organizationTitle;
    }

    @Nullable
    public String getDefaultTeacherPwd() {
        return defaultTeacherPwd;
    }

    public void setDefaultTeacherPwd(@Nullable String defaultTeacherPwd) {
        this.defaultTeacherPwd = defaultTeacherPwd;
    }
}
