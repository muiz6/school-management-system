package com.project.schoolsystem.ui.manageteachers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.project.schoolsystem.PreferenceModel;
import com.project.schoolsystem.PreferenceProvider;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.model.UserModel;
import com.project.schoolsystem.ui.navigation.Destination;
import com.project.schoolsystem.ui.snackbar.Snackbar;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Date;
import java.util.Map;
import java.util.ResourceBundle;

public class AddTeacherPage implements Initializable, Destination {
    private final SqlServer _server = SqlServer.getInstance();
    private final PreferenceProvider _prefProvider = PreferenceProvider.getInstance();
    @FXML
    private VBox root;
    @FXML
    private JFXTextArea fieldAddress;
    @FXML
    private JFXTextField fieldQualification;
    @FXML
    private JFXTextField fieldEmergencyContact;
    @FXML
    private JFXTextField fieldContact;
    @FXML
    private JFXTextField fieldCnic;
    @FXML
    private JFXRadioButton btnRadioFemale;
    @FXML
    private JFXRadioButton btnRadioMale;
    @FXML
    private JFXDatePicker datePickerDob;
    @FXML
    private JFXTextField fieldName;
    @FXML
    private JFXTextField fieldPassword;
    @FXML
    private JFXTextField fieldUserName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final PreferenceModel model = _prefProvider.load();
        fieldPassword.setText(model.getDefaultTeacherPwd());
    }

    @Override
    public void onArguments(@NonNull Map<String, Object> arguments) {}

    public void onAddTeacher(ActionEvent actionEvent) {
        final UserModel model = new UserModel();
        model.setRole(UserModel.ROLE_TEACHER);
        model.setUserName(fieldUserName.getText());
        model.setPassword(fieldPassword.getText());
        model.setDisplayName(fieldName.getText());
        model.setDob(Date.valueOf(datePickerDob.getValue()));
        if (btnRadioMale.isSelected()) {
            model.setGender(UserModel.GENDER_MALE);
        } else if (btnRadioFemale.isSelected()) {
            model.setGender(UserModel.GENDER_FEMALE);
        }
        model.setCnic(fieldCnic.getText());
        model.setPhoneNumber(fieldContact.getText());
        model.setEmergencyContact(fieldEmergencyContact.getText());
        model.setQualification(fieldQualification.getText());
        model.setAddress(fieldAddress.getText());
        _server.postUser(model)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull Boolean success) {
                        new Snackbar(root).enqueue("Teacher added successfully", Snackbar.STATUS_SUCCESS);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        new Snackbar(root).enqueue("Something went wrong!", Snackbar.STATUS_ERROR);
                    }
                });
    }
}
