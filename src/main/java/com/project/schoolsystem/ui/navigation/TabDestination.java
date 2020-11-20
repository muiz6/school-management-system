package com.project.schoolsystem.ui.navigation;

import javax.annotation.Nonnull;
import java.util.Map;

public interface TabDestination {
    void setArguments(@Nonnull Map<String, Object> arguments);
}
