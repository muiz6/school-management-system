package com.project.schoolsystem.ui.students;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jfoenix.controls.JFXListView;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.models.StudentModel;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StudentPage implements Initializable, Destination {
    private final SqlServer _server = SqlServer.getInstance();
    private final TabAdapter _navAdapter;
    private List<StudentModel> _currentStudents;
    @FXML
    private JFXListView studentListV;
    @FXML
    private TextField fieldSearch;
    @FXML
    private GridPane tabHeader;
    @FXML
    private AnchorPane tabContent;

    public StudentPage() {
        final Gson gson = new Gson();
        final JsonReader jsonReader = new JsonReader(new InputStreamReader(
                getClass().getResourceAsStream(R.Navigation.MANAGE_STUDENTS)));
        final Navigation navigation = gson.fromJson(jsonReader, Navigation.class);
        _navAdapter = new TabAdapter(navigation);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _navAdapter.setupHeader(tabHeader);
        _navAdapter.setUpBody(tabContent);

        _updateStudentListView("");

        fieldSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue,
                    String newValue) {
                int length = newValue.length();
                if (length >= 3 || length == 0) {
                    _updateStudentListView(newValue);
                }
            }
        });

        studentListV.getSelectionModel()
                .selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue,
                            Number newValue) {
                        final StudentModel model = _currentStudents.get(newValue.intValue());
                        final Map<String, Object> args = new HashMap<>();
                        args.put(R.NavArgs.STUDENT_MODEL, model);
                        _navAdapter.navigate(R.Id.DEST_EDIT_STUDENT, args);
                        studentListV.getSelectionModel().clearSelection();
                    }
                });
    }

    @Override
    public void onArguments(@NonNull Map<String, Object> arguments) {}

    public void onRefresh(ActionEvent actionEvent) {
        fieldSearch.setText("");
        _updateStudentListView("");
    }

    private void _updateStudentListView(String query) {
        if (query.isEmpty()) {
            _server.getStudents()
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(new _MySingleObserver());
        } else {
            _server.getStudents(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(new _MySingleObserver());
        }
    }

    private class _MySingleObserver implements SingleObserver<List<StudentModel>> {
        @Override
        public void onSubscribe(@NonNull Disposable d) {}

        @Override
        public void onSuccess(@NonNull List<StudentModel> studentModels) {
            _currentStudents = studentModels;
            studentListV.getItems().clear();
            for (final StudentModel model : studentModels) {
                final PersonTile tile = PersonTile.inflate();
                tile.getLabelName().setText(model.getName());
                tile.getLabelUserName().setText(model.getDepartmentCode()
                        + "-" + model.getSessionCode()
                        + "-" + model.getRollNo());
                studentListV.getItems().add(tile.getRoot());
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            e.printStackTrace();
        }
    }
}
