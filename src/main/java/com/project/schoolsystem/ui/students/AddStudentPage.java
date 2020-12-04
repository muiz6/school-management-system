package com.project.schoolsystem.ui.students;

import com.jfoenix.controls.*;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.models.*;
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
    private List<ClassModel> _classes;
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
    @FXML
    private JFXComboBox<String> comboxClass;

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
        final String departmentCode = _getSelectedDepartmentCode();
        final String sessionCode = _getSelectedSessionCode();

        final StudentModel model = new StudentModel();
        model.setDepartmentCode(departmentCode);
        model.setSessionCode(sessionCode);
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

        _server.getStudentRollNoNew(departmentCode, sessionCode)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        final ClassRegisterEntryModel registerModel = new ClassRegisterEntryModel();
                        registerModel.setDepartmentCode(departmentCode);
                        registerModel.setSessionCode(sessionCode);
                        registerModel.setClassCode(_getSelectedClassCode());
                        registerModel.setStudentRollNumber(integer);
                        _server.postClassRegisterEntry(registerModel)
                                .subscribeOn(Schedulers.io())
                                .observeOn(JavaFxScheduler.platform())
                                .subscribe(new SingleObserver<Boolean>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {}

                                    @Override
                                    public void onSuccess(@NonNull Boolean aBoolean) {
                                        System.out.println("Student added to class successfully!");
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        e.printStackTrace();
                                    }
                                });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}
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
        _server.getClasses(departmentCode, sessionCode)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new SingleObserver<List<ClassModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull List<ClassModel> classModels) {
                        _classes = classModels;
                        comboxClass.getItems().clear();
                        for (final ClassModel model : classModels) {
                            comboxClass.getItems().add(model.getClassCode());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    private String _getSelectedDepartmentCode() {
        final int index = comboxDepartment.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            return _departments.get(index).getDepartmentCode();
        }
        return null;
    }

    private String _getSelectedSessionCode() {
        final int index = comboxSession.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            return _sessions.get(index).getSessionCode();
        }
        return null;
    }

    private String _getSelectedClassCode() {
        final int index = comboxClass.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            return _classes.get(index).getClassCode();
        }
        return null;
    }
}
