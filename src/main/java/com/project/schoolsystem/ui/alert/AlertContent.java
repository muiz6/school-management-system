package com.project.schoolsystem.ui.alert;

import com.jfoenix.controls.JFXAlert;
import com.project.schoolsystem.R;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AlertContent implements Initializable {

    private JFXAlert<Integer> _alert;
    @FXML
    private VBox root;
    @FXML
    private Label labelMsg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @Nullable
    public static AlertContent inflate() {
        FXMLLoader loader = new FXMLLoader(AlertContent.class.getResource(R.Fxml.ALERT));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setAlert(@NonNull JFXAlert<Integer> alert) {
        _alert = alert;
    }

    public void onNo(ActionEvent actionEvent) {
        _alert.setResult(ConfirmationAlert.RESULT_NO);
        _alert.close();
    }

    public void onYes(ActionEvent actionEvent) {
        _alert.setResult(ConfirmationAlert.RESULT_YES);
        _alert.close();
    }

    public VBox getRoot() {
        return root;
    }

    public void setMessage(String message) {
        labelMsg.setText(message);
    }
}
