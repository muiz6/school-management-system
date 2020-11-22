package com.project.schoolsystem.ui.navigation;

import io.reactivex.annotations.NonNull;

import java.util.Map;

public interface Destination {
    void onArguments(@NonNull Map<String, Object> arguments);
}
