package com.project.schoolsystem.ui.session;

import com.project.schoolsystem.R;
import io.reactivex.annotations.Nullable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SessionTile implements Initializable {
    @FXML
    private HBox root;
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelSessionCode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @Nullable
    public static SessionTile inflate() {
        final FXMLLoader loader = new FXMLLoader(SessionTile.class.getResource(R.Fxml.SESSION_TILE));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.getController();
    }

    public HBox getRoot() {
        return root;
    }

    public Label getLabelTitle() {
        return labelTitle;
    }

    public Label getLabelSessionCode() {
        return labelSessionCode;
    }
}
