package com.project.schoolsystem.ui.settings;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.project.schoolsystem.PreferenceProvider;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.model.UserModel;
import com.project.schoolsystem.ui.navigation.Destination;
import com.project.schoolsystem.ui.snackbar.Snackbar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javax.annotation.Nullable;
import java.net.URL;
import java.sql.Date;
import java.util.Map;
import java.util.ResourceBundle;

public class TeacherSettingPage implements Initializable, Destination {
    private final SqlServer _server = SqlServer.getInstance();
    private final PreferenceProvider _prefProvider = PreferenceProvider.getInstance();
    @FXML
    private JFXRadioButton btnRadioFemale;
    @FXML
    private JFXRadioButton btnRadioMale;
    @FXML
    private PasswordField fieldOldPwd;
    @FXML
    private TextField fieldNewPwd;
    @FXML
    private TextField fieldConfirmPwd;
    @FXML
    private TextField fieldUserName;
    @FXML
    private JFXDatePicker datePickerReg;
    @FXML
    private JFXTextField fieldName;
    @FXML
    private JFXDatePicker datePickerDob;
    @FXML
    private JFXTextField fieldContact;
    @FXML
    private JFXTextField fieldEmergencyContact;
    @FXML
    private JFXTextField fieldQualification;
    @FXML
    private TextArea fieldAddress;
    @FXML
    private GridPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _initFormContent();
    }

    @Override
    public void onArguments(@Nullable Map<String, Object> arguments) {
    }

    public void onSavePassword(ActionEvent actionEvent) {
        final String newPwd = fieldNewPwd.getText();
        final String cfmPwd = fieldConfirmPwd.getText();
        final Snackbar snackbar = Snackbar.inflate();
        if (newPwd.equals(cfmPwd)) {
            if (newPwd.length() >= 5) {
                final UserModel model = _server.getLastSignIn();
                final String oldPwd = model.getPassword();
                if (oldPwd.equals(fieldOldPwd.getText())) {
                    model.setPassword(newPwd);
                    _server.patchSignedUser(model);
                    snackbar.setStatus(Snackbar.STATUS_SUCCESS);
                    snackbar.setMessage("Password changed successfully.");
                } else {
                    snackbar.setStatus(Snackbar.STATUS_ERROR);
                    snackbar.setMessage("Password Incorrect!");
                }
            } else {
                snackbar.setStatus(Snackbar.STATUS_ERROR);
                snackbar.setMessage("Password must be at least 5 characters long!");
            }
        } else {
            snackbar.setStatus(Snackbar.STATUS_ERROR);
            snackbar.setMessage("Passwords do not match!");
        }
        final JFXSnackbar jfxSnackbar = new JFXSnackbar(root);
        jfxSnackbar.enqueue(new JFXSnackbar.SnackbarEvent(snackbar.getRoot()));
    }

    public void onSaveInfo(ActionEvent actionEvent) {
        final UserModel model = _server.getLastSignIn();
        if (model != null) {
            model.setDisplayName(fieldName.getText());
            model.setDob(Date.valueOf(datePickerDob.getValue()));
            model.setPhoneNumber(fieldContact.getText());
            model.setEmergencyContact(fieldEmergencyContact.getText());
            model.setQualification(fieldQualification.getText());
            model.setAddress(fieldAddress.getText());
            if (btnRadioMale.isSelected()) {
                model.setGender(UserModel.GENDER_MALE);
            } else if (btnRadioFemale.isSelected()) {
                model.setGender(UserModel.GENDER_FEMALE);
            }
            if (_server.patchSignedUser(model)) {
                final Snackbar snackbar = Snackbar.inflate();
                snackbar.setStatus(Snackbar.STATUS_SUCCESS);
                snackbar.setMessage("Changes saved successfully.");
                final JFXSnackbar jfxSnackbar = new JFXSnackbar(root);
                jfxSnackbar.enqueue(new JFXSnackbar.SnackbarEvent(snackbar.getRoot()));
            } else {
                final Snackbar snackbar = Snackbar.inflate();
                snackbar.setStatus(Snackbar.STATUS_ERROR);
                snackbar.setMessage("Something went wrong!");
                final JFXSnackbar jfxSnackbar = new JFXSnackbar(root);
                jfxSnackbar.enqueue(new JFXSnackbar.SnackbarEvent(snackbar.getRoot()));
            }
        }
    }

    private void _initFormContent() {
        final UserModel model = _server.getLastSignIn();
        if (model != null) {
            datePickerReg.setValue(model.getRegistrationDate().toLocalDate());
            fieldName.setText(model.getDisplayName());
            fieldContact.setText(model.getPhoneNumber());
            fieldQualification.setText(model.getQualification());
            fieldAddress.setText(model.getAddress());
            fieldEmergencyContact.setText(model.getEmergencyContact());
            datePickerDob.setValue(model.getDob().toLocalDate());
            fieldUserName.setText(model.getUserName());
            final String gender = model.getGender();
            if (UserModel.GENDER_MALE.equals(gender)) {
                btnRadioMale.setSelected(true);
            } else if (UserModel.GENDER_FEMALE.equals(gender)) {
                btnRadioFemale.setSelected(true);
            }
        }
    }
}
