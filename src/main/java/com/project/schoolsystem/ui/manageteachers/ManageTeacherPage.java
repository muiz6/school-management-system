package com.project.schoolsystem.ui.manageteachers;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jfoenix.controls.JFXListView;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.model.UserModel;
import com.project.schoolsystem.ui.PersonTile;
import com.project.schoolsystem.ui.navigation.Destination;
import com.project.schoolsystem.ui.navigation.Navigation;
import com.project.schoolsystem.ui.navigation.TabAdapter;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ManageTeacherPage implements Initializable, Destination {
    private final SqlServer _server = SqlServer.getInstance();
    private final TabAdapter _navAdapter;
    @FXML
    private TextField fieldSearch;
    @FXML
    private JFXListView teacherListV;
    @FXML
    private GridPane tabHeader;
    @FXML
    private AnchorPane tabContent;

    public ManageTeacherPage() {
        final Gson gson = new Gson();
        final JsonReader jsonReader = new JsonReader(new InputStreamReader(
                getClass().getResourceAsStream(R.Navigation.MANAGE_TEACHERS)));
        final Navigation navigation = gson.fromJson(jsonReader, Navigation.class);
        _navAdapter = new TabAdapter(navigation);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _navAdapter.setupHeader(tabHeader);
        _navAdapter.setUpBody(tabContent);
        _initTeacherList();
    }

    @Override
    public void onArguments(@Nullable Map<String, Object> arguments) {
    }

    private void _initTeacherList() {
        // teacherListV.getSelectionModel()
        //         .selectedIndexProperty()
        //         .addListener(new ChangeListener<Number>() {
        //             @Override
        //             public void changed(ObservableValue<? extends Number> observable,
        //                     Number oldValue,
        //                     Number newValue) {
        //                 _navAdapter.navigate();
        //             }
        //         });

        _updateTeacherListViewAll();

        fieldSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue,
                    String newValue) {
                if (newValue.length() >= 3) {
                    _updateTeacherListViewBySearch(newValue);
                } else if (newValue.isEmpty()) {
                    _updateTeacherListViewAll();
                }
            }
        });
    }

    private void _updateTeacherListViewAll() {
        _server.getTeachers()
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new _MySingleObserver());
    }

    private void _updateTeacherListViewBySearch(@Nonnull String query) {
        _server.getTeacherBySearch(query)
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new _MySingleObserver());
    }

    private class _MySingleObserver implements SingleObserver<List<UserModel>> {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
        }

        @Override
        public void onSuccess(@NonNull List<UserModel> teachers) {
            teacherListV.getItems().clear();
            for (final UserModel teacher : teachers) {
                final PersonTile tile = PersonTile.inflate();
                tile.getLabelName().setText(teacher.getDisplayName());
                tile.getLabelUserName().setText(teacher.getUserName());
                teacherListV.getItems().add(tile.getRoot());
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
        }
    }
}
