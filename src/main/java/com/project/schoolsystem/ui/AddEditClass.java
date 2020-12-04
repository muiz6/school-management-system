package com.project.schoolsystem.ui;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.models.ClassModel;
import com.project.schoolsystem.data.models.DepartmentModel;
import com.project.schoolsystem.data.models.SessionModel;
import com.project.schoolsystem.ui.alert.ConfirmationAlert;
import com.project.schoolsystem.ui.snackbar.Snackbar;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddEditClass implements Initializable {
    private final SqlServer _server = SqlServer.getInstance();
    private final ChangeListener<Number> _editChangeListener =
            new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable,
                        Number oldValue,
                        Number newValue) {
                    _updateClassComboBox();
                }
            };
    private List<SessionModel> _sessions;
    private List<DepartmentModel> _departments;
    private List<ClassModel> _currentClasses;
    @FXML
    private JFXComboBox<String> comboxDepartmentEdit;
    @FXML
    private JFXComboBox<String> comboxSessionEdit;
    @FXML
    private JFXComboBox<String> comboxClassEdit;
    @FXML
    private GridPane root;
    @FXML
    private JFXComboBox<String> comboxSessionAdd;
    @FXML
    private JFXTextField fieldClassCode;
    @FXML
    private JFXComboBox<String> comboxDepartmentAdd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _updateComboBoxes();
        comboxDepartmentEdit.getSelectionModel()
                .selectedIndexProperty()
                .addListener(_editChangeListener);
        comboxSessionEdit.getSelectionModel()
                .selectedIndexProperty()
                .addListener(_editChangeListener);
    }

    public void onAdd(ActionEvent actionEvent) {
        final ClassModel model = new ClassModel();
        model.setClassCode(fieldClassCode.getText().toUpperCase());
        final String departmentCode = _departments.get(comboxDepartmentAdd.getSelectionModel()
                .getSelectedIndex()).getDepartmentCode();
        model.setDepartmentCode(departmentCode);
        final String sessionCode = _sessions.get(comboxSessionAdd.getSelectionModel()
                .getSelectedIndex()).getSessionCode();
        model.setSessionCode(sessionCode);
        _server.postClass(model)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        new Snackbar(root).enqueue("Class Added Successfully.",
                                Snackbar.STATUS_SUCCESS);
                        _updateClassComboBox();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        new Snackbar(root).enqueue("Something went wrong!",
                                Snackbar.STATUS_ERROR);
                    }
                });
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
                            final int index = comboxClassEdit.getSelectionModel()
                                    .getSelectedIndex();
                            if (index >= 0) {
                                final ClassModel model = _currentClasses.get(index);
                                model.setActive(false);
                                _server.patchClass(model)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(JavaFxScheduler.platform())
                                        .subscribe(new SingleObserver<Boolean>() {
                                            @Override
                                            public void onSubscribe(@NonNull Disposable d) {}

                                            @Override
                                            public void onSuccess(@NonNull Boolean aBoolean) {
                                                new Snackbar(root).enqueue("Class deactivated successfully",
                                                        Snackbar.STATUS_SUCCESS);
                                                _updateClassComboBox();
                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {
                                                new Snackbar(root).enqueue("Something went wrong!",
                                                        Snackbar.STATUS_ERROR);
                                                e.printStackTrace();
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}
                });
    }

    private void _updateComboBoxes() {
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
                            comboxSessionAdd.getItems().add(model.getSessionTitle());
                            comboxSessionEdit.getItems().add(model.getSessionTitle());
                        }
                        comboxSessionAdd.getSelectionModel().selectFirst();
                        comboxSessionEdit.getSelectionModel().selectFirst();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}
                });
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
                            comboxDepartmentAdd.getItems().add(model.getTitle());
                            comboxDepartmentEdit.getItems().add(model.getTitle());
                        }
                        comboxDepartmentAdd.getSelectionModel().selectFirst();
                        comboxDepartmentEdit.getSelectionModel().selectFirst();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}
                });
    }

    private void _updateClassComboBox() {
        final int indexDep = comboxDepartmentEdit.getSelectionModel().getSelectedIndex();
        final int indexSsn = comboxSessionEdit.getSelectionModel().getSelectedIndex();
        if (indexDep >= 0 && indexSsn >= 0) {
            _server.getClasses(_departments.get(indexDep).getDepartmentCode(),
                    _sessions.get(indexSsn).getSessionCode())
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(new SingleObserver<List<ClassModel>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {}

                        @Override
                        public void onSuccess(@NonNull List<ClassModel> classModels) {
                            _currentClasses = classModels;
                            comboxClassEdit.getItems().clear();
                            for (final ClassModel model : classModels) {
                                comboxClassEdit.getItems().add(model.getClassCode());
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}
