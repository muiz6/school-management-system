package com.project.schoolsystem.ui;

import com.project.schoolsystem.R;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Snackbar implements Initializable {
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_ERROR = 1;
    private final String[] styles = {
            "-fx-background-color: green;",
            "-fx-background-color: red;"
    };
    private int status;
    @FXML
    private HBox root;
    @FXML
    private VBox indicator;
    @FXML
    private Label labelMsg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indicator.setStyle(styles[status]);
    }

    @Nullable
    public static Snackbar inflate() {
        final FXMLLoader loader = new FXMLLoader(Snackbar.class.getResource(R.Fxml.SNACKBAR));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HBox getRoot() {
        return root;
    }

    public void setStatus(int status) {
       this.status = status;
       indicator.setStyle(styles[status]);
    }

    public void setMessage(String message) {
        labelMsg.setText(message);
    }
}
