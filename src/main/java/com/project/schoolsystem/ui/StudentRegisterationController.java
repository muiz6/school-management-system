/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.schoolsystem.ui;

import com.jfoenix.controls.JFXTextField;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.models.StudentModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

/**
 *
 * @author msufwan
 */
public class StudentRegisterationController {

    @FXML
    private JFXTextField fieldName;
    @FXML
    private JFXTextField fieldFatherName;
    @FXML
    private JFXTextField fieldmobileNumber;
    @FXML
    private JFXTextField fieldEmrgNumber;
    @FXML
    private JFXTextField fieldAddress;
    @FXML
    private DatePicker datePickerDob;
    @FXML
    private DatePicker datePickerRgt;


    @FXML
    private Button btnAddNewStudent;

    @FXML
    private Button btnRemoveStudent1;

    public void onInsertStudent(javafx.event.ActionEvent mouseEvent)
    {
        _insertStudent();
    }

    private void _insertStudent() {
        StudentModel model = new StudentModel();
        model.setName(fieldName.getText());
        model.setFather_name(fieldFatherName.getText());
        model.setMobile_no(fieldmobileNumber.getText());
        model.setEmergency_no(fieldEmrgNumber.getText());
        model.setAddress(fieldAddress.getText());
        model.setDob(datePickerDob.getValue().toString());
        model.setRegistration_date(datePickerRgt.getValue().toString());
        SqlServer server = SqlServer.getInstance();
        server.insertStudent(model, new SqlServer.OnCompletionCallback<String>() {
            @Override
            public void onResult(boolean success, String result) {

                // feesback from db here
            }
        });
    }

}
