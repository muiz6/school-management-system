package com.project.schoolsystem.ui.departments;

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

public class DepartmentTile implements Initializable {
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelDepartmentCode;
    @FXML
    private HBox root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @Nullable
    public static DepartmentTile inflate() {
        final FXMLLoader loader = new FXMLLoader(DepartmentTile.class.getResource(R.Fxml.DEPARTMENT_TILE));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.getController();
    }

    public Label getLabelTitle() {
        return labelTitle;
    }

    public Label getLabelDepartmentCode() {
        return labelDepartmentCode;
    }

    public HBox getRoot() {
        return root;
    }
}
