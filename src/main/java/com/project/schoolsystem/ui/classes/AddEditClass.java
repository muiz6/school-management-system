package com.project.schoolsystem.ui.classes;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.models.ClassModel;
import com.project.schoolsystem.data.models.DepartmentModel;
import com.project.schoolsystem.data.models.SessionModel;
import com.project.schoolsystem.ui.snackbar.Snackbar;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddEditClass implements Initializable {
    private final SqlServer _server = SqlServer.getInstance();
    private List<SessionModel> _sessions;
    private List<DepartmentModel> _departments;
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
                        }
                        comboxSessionAdd.getSelectionModel().selectFirst();
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
                        }
                        comboxDepartmentAdd.getSelectionModel().selectFirst();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {}
                });
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
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        new Snackbar(root).enqueue("Something went wrong!",
                                Snackbar.STATUS_ERROR);
                    }
                });
    }

    public void onDeactivate(ActionEvent actionEvent) {}
}
