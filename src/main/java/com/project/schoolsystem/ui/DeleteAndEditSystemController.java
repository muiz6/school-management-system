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
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

/**
 * @author msufwan
 */
public class DeleteAndEditSystemController {

    @FXML
    private JFXTextField fieldId;

    @FXML
    private JFXTextField fieldName;
    @FXML
    private JFXTextField fieldFatherName;
    @FXML
    private JFXTextField fieldMobileNumber;
    @FXML
    private JFXTextField fieldEmrgNumber;
    @FXML
    private JFXTextField fieldAddress;
    @FXML
    private DatePicker datePickerDob;
    @FXML
    private DatePicker datePickerRgtDate;

    public void onSearchStudent(javafx.event.ActionEvent mouseEvent){
        _searchStudent();
    }

    private void _searchStudent(){
        int id = Integer.parseInt(fieldId.getText());
//        StudentModel model = new StudentSearchModel();
//        model.setId(fieldId.getText());
//        model.setName(fieldName.getText());
//        model.setFather_name(fieldFatherName.getText());
//        model.setMobile_no(fieldMobileNumber.getText());
//        model.setEmergency_no(fieldEmrgNumber.getText());
//        model.setAddress(fieldAddress.getText());
//        model.setDob(datePickerDob.getValue().toString());
//        model.setRegistration_date(datePickerRgtDate.getValue().toString());
        SqlServer server = SqlServer.getInstance();
        server.searchStudent(id, new SqlServer.OnCompletionCallback<StudentModel>() {
            @Override
            public void onResult(boolean success, StudentModel model) {
                // id model null data not found
                fieldName.setText(model.getName());
                fieldFatherName.setText(model.getFather_name());
                fieldMobileNumber.setText(model.getMobile_no());
                fieldEmrgNumber.setText(model.getEmergency_no());
                fieldAddress.setText(model.getAddress());
                datePickerDob.setValue(LocalDate.parse(model.getDob()));
                datePickerRgtDate.setValue(LocalDate.parse(model.getRegistration_date()));
                // feesback from db here
            }
        });
    }
}
