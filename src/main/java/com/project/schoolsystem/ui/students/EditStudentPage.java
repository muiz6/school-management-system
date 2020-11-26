package com.project.schoolsystem.ui.students;

import com.jfoenix.controls.*;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.models.StudentModel;
import com.project.schoolsystem.ui.navigation.Destination;
import io.reactivex.annotations.NonNull;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class EditStudentPage implements Initializable, Destination {
    private StudentModel _currentStudent;
    @FXML
    private VBox root;
    @FXML
    private JFXDatePicker datePickerReg;
    @FXML
    private JFXTextField fieldRollNo;
    @FXML
    private JFXTextField fieldName;
    @FXML
    private JFXTextField fieldParentName;
    @FXML
    private JFXDatePicker datePickerDob;
    @FXML
    private JFXRadioButton btnRadioMale;
    @FXML
    private JFXRadioButton btnRadioFemale;
    @FXML
    private JFXTextField fieldContact;
    @FXML
    private JFXTextField fieldEmergencyContact;
    @FXML
    private JFXTextField fieldCnic;
    @FXML
    private JFXTextArea fieldAddress;
    @FXML
    private JFXComboBox<String> comboxClass;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @Override
    public void onArguments(@NonNull Map<String, Object> arguments) {
        final StudentModel model = (StudentModel) arguments.get(R.NavArgs.STUDENT_MODEL);
        _currentStudent = model;
        datePickerReg.setValue(model.getRegistrationDate().toLocalDate());
        fieldRollNo.setText(model.getDepartmentCode()
                + "-" + model.getSessionCode()
                + "-" + model.getRollNo());
        fieldName.setText(model.getName());
        fieldParentName.setText(model.getFatherName());
        datePickerDob.setValue(model.getDob().toLocalDate());
        final String gender = model.getGender();
        if (StudentModel.GENDER_MALE.equals(gender)) {
            btnRadioMale.setSelected(true);
        } else if (StudentModel.GENDER_FEMALE.equals(gender)) {
            btnRadioFemale.setSelected(true);
        }
        fieldEmergencyContact.setText(model.getEmergencyContact());
        fieldCnic.setText(model.getCnic());
        fieldAddress.setText(model.getAddress());
    }

    public void onUpdate(ActionEvent actionEvent) {}

    public void onDeactivate(ActionEvent actionEvent) {}
}
