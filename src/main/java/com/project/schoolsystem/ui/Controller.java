package com.project.schoolsystem.ui;

import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.SqlServer.OnCompletionCallback;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button btnManageClass;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnStudents;

    @FXML
    private Button btn_Timetable;

    @FXML
    private Button btnDeleteEdit;

    @FXML
    private Button btnTeachers;

    @FXML
    private Button btnAttendance;

    //my bad - the freaking mouse event
    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) {
        final Object source = mouseEvent.getSource();
        if (mouseEvent.getSource() == btnDashboard) {
            loadStage(R.Fxml.DASHBOARD);
        } else if (mouseEvent.getSource() == btnStudents) {
            loadStage(R.Fxml.STUDENTS);
        } else if (mouseEvent.getSource() == btn_Timetable) {
            loadStage(R.Fxml.TIME_TABLE);
        }
        else if (mouseEvent.getSource() == btnTeachers) {
            loadStage(R.Fxml.TEACHER_FORM);
        }
        else if (mouseEvent.getSource() == btnDeleteEdit) {
            loadStage(R.Fxml.DELETE_AND_EDIT_SYSTEM);
        }
        else if (mouseEvent.getSource() == btnAttendance) {
            loadStage(R.Fxml.ATTENDANCE_MANAGEMENT_SYSTEM);
        } else if (source == btnManageClass) {
            loadStage(R.Fxml.MANAGE_CLASS);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SqlServer server = SqlServer.getInstance();
        server.connect(new OnCompletionCallback<String>() {
            @Override
            public void onResult(boolean success, String result) {
                if (success) {
                    final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sql Server");
                    alert.setContentText("Connected to Sql Server");
                    alert.show();
                } else {
                    final Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Sql Server");
                    alert.setContentText("Failed to Connect to Sql Server.");
                    alert.show();
                }
            }
        });
    }

    private void loadStage(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(R.Image.ICON));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
