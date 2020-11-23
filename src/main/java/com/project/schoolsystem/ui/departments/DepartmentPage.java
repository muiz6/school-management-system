package com.project.schoolsystem.ui.departments;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.model.DepartmentModel;
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
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentPage implements Initializable {
    private final SqlServer _server = SqlServer.getInstance();
    @FXML
    private JFXListView departmentListV;
    @FXML
    private VBox root;
    @FXML
    private JFXTextField fieldDepTitleEdit;
    @FXML
    private JFXTextField fieldDepCodeEdit;
    @FXML
    private JFXTextField fieldDepTitleAdd;
    @FXML
    private JFXTextField fieldDepCodeAdd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _updateListView();
    }

    public void onRefresh(ActionEvent actionEvent) {
        _updateListView();
    }

    public void onAdd(ActionEvent actionEvent) {
        final DepartmentModel model = new DepartmentModel();
        model.setDepartmentCode(fieldDepCodeAdd.getText());
        model.setTitle(fieldDepTitleAdd.getText());
        _server.postDepartment(model)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onSuccess(@NonNull Boolean success) {
                        new Snackbar(root).enqueue("Department Added Successfully.",
                                Snackbar.STATUS_SUCCESS);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        new Snackbar(root).enqueue("Something went wrong!",
                                Snackbar.STATUS_ERROR);
                    }
                });
    }

    public void onUpdate(ActionEvent actionEvent) {}

    public void onDeactivate(ActionEvent actionEvent) {}

    private void _updateListView() {
        _server.getDepartments()
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new _MySingleObserver());
    }

    private class _MySingleObserver implements SingleObserver<List<DepartmentModel>> {
        @Override
        public void onSubscribe(@NonNull Disposable d) {}

        @Override
        public void onSuccess(@NonNull List<DepartmentModel> departmentModels) {
            departmentListV.getItems().clear();
            for (final DepartmentModel model : departmentModels) {
                final DepartmentTile tile = DepartmentTile.inflate();
                tile.getLabelTitle().setText(model.getTitle());
                tile.getLabelDepartmentCode().setText(model.getDepartmentCode());
                departmentListV.getItems().add(tile.getRoot());
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {}
    }
}
