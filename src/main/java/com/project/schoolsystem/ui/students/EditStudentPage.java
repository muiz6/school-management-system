package com.project.schoolsystem.ui.students;

import com.jfoenix.controls.*;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.models.StudentModel;
import com.project.schoolsystem.ui.alert.ConfirmationAlert;
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

public class EditStudentPage implements Initializable, Destination {
    private SqlServer _server = SqlServer.getInstance();
    private StudentModel _currentStudent;
    private SingleObserver<Boolean> _singleObserver =
            new SingleObserver<Boolean>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {}

                @Override
                public void onSuccess(@NonNull Boolean aBoolean) {
                    new Snackbar(root).enqueue("Student updated successfully.",
                            Snackbar.STATUS_SUCCESS);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    e.printStackTrace();
                    new Snackbar(root).enqueue("Something went wrong!",
                            Snackbar.STATUS_ERROR);
                }
            };
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
        fieldContact.setText(model.getPhoneNumber());
        fieldCnic.setText(model.getCnic());
        fieldAddress.setText(model.getAddress());
    }

    public void onUpdate(ActionEvent actionEvent) {
        final StudentModel model = _currentStudent;
        model.setName(fieldName.getText());
        model.setFatherName(fieldParentName.getText());
        model.setCnic(fieldCnic.getText());
        model.setPhoneNumber(fieldContact.getText());
        model.setEmergencyContact(fieldEmergencyContact.getText());
        model.setDob(Date.valueOf(datePickerDob.getValue()));
        model.setAddress(fieldAddress.getText());
        if (btnRadioMale.isSelected()) {
            model.setGender(StudentModel.GENDER_MALE);
        } else if (btnRadioFemale.isSelected()) {
            model.setGender(StudentModel.GENDER_FEMALE);
        }
        model.setActive(true);
        _server.patchStudent(model)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(_singleObserver);
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
                            final StudentModel model = _currentStudent;
                            model.setActive(false);
                            _server.patchStudent(model)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(JavaFxScheduler.platform())
                                    .subscribe(_singleObserver);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}
                });
    }
}
