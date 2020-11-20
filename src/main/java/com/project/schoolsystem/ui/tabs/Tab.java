package com.project.schoolsystem.ui.tabs;

import com.jfoenix.controls.JFXRippler;
import com.project.schoolsystem.R;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Tab implements Initializable {
    private static final String[] STYLE = {
            "-fx-background-color: #00000020;",
            "-fx-background-color: -color-secondary;"
    };
    @FXML
    private JFXRippler root;
    @FXML
    private Label labelTab;
    @FXML
    private VBox indicator;
    private boolean selected = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setStyle(selected);
    }

    @Nullable
    public static Tab inflate() {
        final FXMLLoader loader = new FXMLLoader(Tab.class.getResource(R.Fxml.TAB));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setText(String text) {
        labelTab.setText(text);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        setStyle(selected);
    }

    public JFXRippler getRoot() {
        return root;
    }

    private void setStyle(boolean selected) {
        if (selected) {
            indicator.setStyle(STYLE[1]);
        } else {
            indicator.setStyle(STYLE[0]);
        }
    }
}
