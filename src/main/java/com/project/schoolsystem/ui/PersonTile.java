package com.project.schoolsystem.ui;

import com.project.schoolsystem.R;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PersonTile implements Initializable {
    @FXML
    private HBox root;
    @FXML
    private Label labelName;
    @FXML
    private Label labelUserName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Nullable
    public static PersonTile inflate() {
        final FXMLLoader loader = new FXMLLoader(PersonTile.class.getResource(R.Fxml.PERSON_TILE));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public HBox getRoot() {
        return root;
    }

    @Nullable
    public Label getLabelName() {
        return labelName;
    }

    @Nullable
    public Label getLabelUserName() {
        return labelUserName;
    }
}
