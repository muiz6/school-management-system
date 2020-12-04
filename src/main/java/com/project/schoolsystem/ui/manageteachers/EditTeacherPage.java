package com.project.schoolsystem.ui.manageteachers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.models.UserModel;
import com.project.schoolsystem.ui.alert.ConfirmationAlert;
import com.project.schoolsystem.ui.navigation.Destination;
import com.project.schoolsystem.ui.snackbar.Snackbar;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Date;
import java.util.Map;
import java.util.ResourceBundle;

public class EditTeacherPage implements Destination, Initializable {
    private final SqlServer _server = SqlServer.getInstance();
    @FXML
    private VBox root;
    @FXML
    private JFXTextField fieldCnic;
    @FXML
    private JFXTextField fieldUserName;
    @FXML
    private JFXDatePicker datePickerReg;
    @FXML
    private JFXTextField fieldName;
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
    private JFXTextField fieldQualification;
    @FXML
    private TextArea fieldAddress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @Override
    public void onArguments(@NonNull Map<String, Object> arguments) {
        final UserModel teacher = (UserModel) arguments.get(R.NavArgs.USER_MODEL);
        fieldUserName.setText(teacher.getUserName());
        datePickerReg.setValue(teacher.getRegistrationDate().toLocalDate());
        fieldName.setText(teacher.getDisplayName());
        datePickerDob.setValue(teacher.getDob().toLocalDate());
        final String gender = teacher.getGender();
        if (UserModel.GENDER_MALE.equals(gender)) {
            btnRadioMale.setSelected(true);
        } else if (UserModel.GENDER_FEMALE.equals(gender)) {
            btnRadioFemale.setSelected(true);
        }
        fieldContact.setText(teacher.getPhoneNumber());
        fieldEmergencyContact.setText(teacher.getEmergencyContact());
        fieldCnic.setText(teacher.getCnic());
        fieldQualification.setText(teacher.getQualification());
        fieldAddress.setText(teacher.getAddress());
    }

    public void onSave(ActionEvent actionEvent) {
        final UserModel model = new UserModel();
        model.setActive(true);
        _saveUser(model);
    }

    public void onDeactivate(ActionEvent actionEvent) {
        final ConfirmationAlert alert = new ConfirmationAlert(root.getScene().getWindow());
        alert.setMessage("Are you sure?");
        alert.showAsModal()
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull Integer result) {
                        if (result == ConfirmationAlert.RESULT_YES) {
                            System.out.println("deactivated!");
                            final UserModel model = new UserModel();
                            model.setActive(false);
                            _saveUser(model);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}
                });
    }

    private void _saveUser(UserModel model) {
        model.setUserName(fieldUserName.getText());
        model.setDisplayName(fieldName.getText());
        model.setDob(Date.valueOf(datePickerDob.getValue()));
        if (btnRadioMale.isSelected()) {
            model.setGender(UserModel.GENDER_MALE);
        } else if (btnRadioFemale.isSelected()) {
            model.setGender(UserModel.GENDER_FEMALE);
        }
        model.setPhoneNumber(fieldContact.getText());
        model.setEmergencyContact(fieldEmergencyContact.getText());
        model.setCnic(fieldCnic.getText());
        model.setQualification(fieldQualification.getText());
        model.setAddress(fieldAddress.getText());
        if (_server.patchUser(model)) {
            new Snackbar(root).enqueue("Teacher updated successfully", Snackbar.STATUS_SUCCESS);
        } else {
            new Snackbar(root).enqueue("Something when wrong!", Snackbar.STATUS_ERROR);
        }
    }
}
