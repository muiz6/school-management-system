package com.project.schoolsystem.ui;

import com.project.schoolsystem.R;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.project.schoolsystem.data.model.StudentsModelOld;
import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StudentsController implements Initializable {
    @FXML
    private TableView<StudentsModelOld> tbData;
    @FXML
    public TableColumn<StudentsModelOld, Integer> studentId;

    @FXML
    public TableColumn<StudentsModelOld, String> firstName;

    @FXML
    public TableColumn<StudentsModelOld, Integer> Class;
    
    @FXML
    public TableColumn<StudentsModelOld, Integer> Fee;
    
    @FXML
    private Button btnAddNewStudent;
    
    @FXML
    private Button btnRemoveStudent1;
    
    //my bad - the freaking mouse event
    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) {
        if (mouseEvent.getSource() == btnAddNewStudent) {
            loadStage(R.Fxml.STUDENT_FORM);
        }else if (mouseEvent.getSource() == btnRemoveStudent1){
            loadStage(R.Fxml.DELETE_AND_EDIT_SYSTEM);
        } 
        }

    @Override
    
    public void initialize(URL location, ResourceBundle resources) {
        studentId.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        Class.setCellValueFactory(new PropertyValueFactory<>("semester"));
        Fee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        tbData.setItems(studentsModels);
    }

    private final ObservableList<StudentsModelOld> studentsModels = FXCollections.observableArrayList(new StudentsModelOld(1,"One Grade", "4 Grade", 25, 4),
            new StudentsModelOld(2,"Two Grade", "Mors", 20, 3),
            new StudentsModelOld(3,"Three Grade", "Chepchieng", 35,6),
            new StudentsModelOld(4,"Four Grade", "Mors", 22, 0)
    );

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
