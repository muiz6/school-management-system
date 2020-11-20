package com.project.schoolsystem.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClassTileController implements Initializable {
    @FXML
    private Label labelClassName;
    @FXML
    private Label labelClassId;
    @FXML
    private Label labelStudents;
    @FXML
    private Label labelTermId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void setClassName(String className) {
        labelClassName.setText(className);
    }

    public void setClassId(int id) {
        labelClassId.setText(String.valueOf(id));
    }

    public void setStudentCount(int count) {
        labelStudents.setText(String.valueOf(count));
    }

    public void setTermId(int id) {
        labelTermId.setText(String.valueOf(id));
    }
}
