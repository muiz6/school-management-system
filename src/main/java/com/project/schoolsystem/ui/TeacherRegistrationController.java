/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.schoolsystem.ui;

import com.jfoenix.controls.JFXTextField;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.model.TeacherModel;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author msufwan
 */
public class TeacherRegistrationController {
    

    @FXML
    private JFXTextField fieldName;
    @FXML
    private JFXTextField fieldCnic;
    @FXML
    private JFXTextField fieldmobileNumber;
    @FXML
    private JFXTextField fieldEmrgNumber;
    @FXML
    private JFXTextField fieldAddress;
    @FXML
    private JFXTextField fieldQualification;
    @FXML
    private DatePicker datePickerDob;
    @FXML
    private DatePicker datePickerRgt;
    
    @FXML
    private Button btnRegisterTeacher;
    
    @FXML
    private Button btnSearchTeacher;
    
    @FXML
    private Button btnRemoveTeacher;
    
    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) {
        if (mouseEvent.getSource() == btnSearchTeacher) {
            loadStage(R.Fxml.STUDENT_FORM);
        }else if (mouseEvent.getSource() == btnRemoveTeacher){
            loadStage(R.Fxml.DELETE_AND_EDIT_SYSTEM);
        } 
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
    
    public void onInsertTeacher(javafx.event.ActionEvent mouseEvent)
    {
        _insertTeacher();
    }

    private void _insertTeacher() {
        TeacherModel model = new TeacherModel();
        model.setName(fieldName.getText());
        model.setCnic(fieldCnic.getText());
        model.setMobile_no(fieldmobileNumber.getText());
        model.setEmergency_no(fieldEmrgNumber.getText());
        model.setQualification(fieldQualification.getText());
        model.setAddress(fieldAddress.getText());
        model.setDob(datePickerDob.getValue().toString());
        model.setRegistration_date(datePickerRgt.getValue().toString());
        SqlServer server = SqlServer.getInstance();
        server.insertTeacher(model, new SqlServer.OnCompletionCallback<String>() {
            @Override
            public void onResult(boolean success, String result) {
            
                // feesback from db here
            }
        });
    }
}
