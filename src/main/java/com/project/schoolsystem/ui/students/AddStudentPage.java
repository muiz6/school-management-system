package com.project.schoolsystem.ui.students;

import com.jfoenix.controls.*;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.models.DepartmentModel;
import com.project.schoolsystem.data.models.SessionModel;
import com.project.schoolsystem.data.models.StudentModel;
import com.project.schoolsystem.ui.snackbar.Snackbar;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AddStudentPage implements Initializable {
    private final SqlServer _server = SqlServer.getInstance();
    private final ChangeListener<Number> _cBoxChangeListener =
            (observable, oldValue, newValue) -> _refreshRollNo();
    private List<SessionModel> _sessions;
    private List<DepartmentModel> _departments;
    @FXML
    private VBox root;
    @FXML
    private JFXComboBox<String> comboxSession;
    @FXML
    private JFXComboBox<String> comboxDepartment;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboxDepartment.getSelectionModel()
                .selectedIndexProperty()
                .addListener(_cBoxChangeListener);
        comboxSession.getSelectionModel()
                .selectedIndexProperty()
                .addListener(_cBoxChangeListener);

        _server.getDepartments()
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new SingleObserver<List<DepartmentModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull List<DepartmentModel> departmentModels) {
                        _departments = departmentModels;
                        for (final DepartmentModel model : departmentModels) {
                            comboxDepartment.getItems().add(model.getTitle());
                        }
                        comboxDepartment.getSelectionModel().selectFirst();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}
                });
        _server.getSessions()
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new SingleObserver<List<SessionModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull List<SessionModel> sessionModels) {
                        _sessions = sessionModels;
                        for (final SessionModel model : sessionModels) {
                            comboxSession.getItems().add(model.getSessionTitle());
                        }
                        comboxSession.getSelectionModel().selectFirst();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}
                });
    }

    public void onAdd(ActionEvent actionEvent) {
        final StudentModel model = new StudentModel();
        model.setDepartmentCode(_getSelectedDepartmentCode());
        model.setSessionCode(_getSelectedSessionCode());
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
        _server.postStudent(model)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        new Snackbar(root).enqueue("Student added successfully.",
                                Snackbar.STATUS_SUCCESS);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        new Snackbar(root).enqueue("Something went wrong!",
                                Snackbar.STATUS_ERROR);
                    }
                });
    }

    private void _refreshRollNo() {
        final String departmentCode = _getSelectedDepartmentCode();
        final String sessionCode = _getSelectedSessionCode();
        _server.getStudentRollNoNew(departmentCode, sessionCode)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        fieldRollNo.setText(departmentCode
                                + '-' + sessionCode
                                + '-' + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}
                });
    }

    private String _getSelectedDepartmentCode() {
        return _departments.get(comboxDepartment.getSelectionModel()
                .getSelectedIndex()).getDepartmentCode();
    }

    private String _getSelectedSessionCode() {
        return _sessions.get(comboxSession.getSelectionModel()
                .getSelectedIndex()).getSessionCode();
    }
}
