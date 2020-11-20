package com.project.schoolsystem.ui.navigation;

import javax.annotation.Nullable;
import java.util.Map;

public interface Destination {
    void onArguments(@Nullable Map<String, Object> arguments);
}
