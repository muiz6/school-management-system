package com.project.schoolsystem.ui.session;

import com.jfoenix.controls.JFXDatePicker;
import com.project.schoolsystem.data.SqlServer;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class AddSessionPage implements Initializable {
    private final SqlServer _server = SqlServer.getInstance();
    @FXML
    private VBox root;
    @FXML
    private JFXDatePicker datePickerEnd;
    @FXML
    private JFXDatePicker datePickerStart;
    @FXML
    private TextField fieldSessionTitle;
    @FXML
    private TextField fieldSessionCode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void onAdd(ActionEvent actionEvent) {
        final SessionModel session = new SessionModel();
        session.setSessionCode(fieldSessionCode.getText());
        session.setSessionTitle(fieldSessionTitle.getText());
        session.setStartDate(Date.valueOf(datePickerStart.getValue()));
        session.setEndDate(Date.valueOf(datePickerEnd.getValue()));
        _server.postSession(session)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        new Snackbar(root).enqueue("Session added successfully.",
                                Snackbar.STATUS_SUCCESS);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        new Snackbar(root).enqueue("Something went wrong!",
                                Snackbar.STATUS_ERROR);
                    }
                });
    }
}
