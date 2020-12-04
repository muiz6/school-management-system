package com.project.schoolsystem.ui.snackbar;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.layout.Pane;

public class Snackbar extends JFXSnackbar {
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_ERROR = 1;
    private final SnackbarContent _content;

    {
        _content = SnackbarContent.inflate();
    }

    public Snackbar(Pane snackbarContainer) {
        super(snackbarContainer);
    }

    public void enqueue(String message, int status) {
        _content.setStatus(status);
        _content.setMessage(message);
        enqueue(new JFXSnackbar.SnackbarEvent(_content.getRoot()));
    }
}
