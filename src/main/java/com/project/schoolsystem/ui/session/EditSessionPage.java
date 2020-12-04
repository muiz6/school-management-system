package com.project.schoolsystem.ui.session;

import com.jfoenix.controls.JFXDatePicker;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.models.SessionModel;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Date;
import java.util.Map;
import java.util.ResourceBundle;

public class EditSessionPage implements Initializable, Destination {
    private final SqlServer _server = SqlServer.getInstance();
    @FXML
    private VBox root;
    @FXML
    private JFXDatePicker datePickerEnd;
    @FXML
    private TextField fieldTitle;
    @FXML
    private JFXDatePicker datePickerStart;
    @FXML
    private TextField fieldSessionCode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @Override
    public void onArguments(@NonNull Map<String, Object> arguments) {
        final SessionModel model = (SessionModel) arguments.get(R.NavArgs.SESSION_MODEL);
        fieldSessionCode.setText(model.getSessionCode());
        fieldTitle.setText(model.getSessionTitle());
        datePickerStart.setValue(model.getStartDate().toLocalDate());
        datePickerEnd.setValue(model.getEndDate().toLocalDate());
    }

    public void onUpdate(ActionEvent actionEvent) {
        final SessionModel model = new SessionModel();
        model.setSessionCode(fieldSessionCode.getText());
        model.setSessionTitle(fieldTitle.getText());
        model.setStartDate(Date.valueOf(datePickerStart.getValue()));
        model.setEndDate(Date.valueOf(datePickerEnd.getValue()));
        _server.patchSession(model)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        new Snackbar(root).enqueue("Session updated successfuly!",
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
}
