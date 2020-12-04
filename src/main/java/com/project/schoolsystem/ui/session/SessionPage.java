package com.project.schoolsystem.ui.session;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jfoenix.controls.JFXListView;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.models.SessionModel;
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

public class SessionPage implements Initializable {
    private final SqlServer _server = SqlServer.getInstance();
    private final TabAdapter _navAdapter;
    private List<SessionModel> _currentSessions;
    @FXML
    private TextField fieldSearch;
    @FXML
    private JFXListView sessionListV;
    @FXML
    private GridPane tabHeader;
    @FXML
    private AnchorPane tabContent;

    public SessionPage() {
        final Gson gson = new Gson();
        final JsonReader jsonReader = new JsonReader(new InputStreamReader(
                getClass().getResourceAsStream(R.Navigation.SESSION)));
        final Navigation navigation = gson.fromJson(jsonReader, Navigation.class);
        _navAdapter = new TabAdapter(navigation);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _navAdapter.setupHeader(tabHeader);
        _navAdapter.setUpBody(tabContent);

        _updateListView("");

        sessionListV.getSelectionModel()
                .selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable,
                            Number oldValue,
                            Number newValue) {
                        final Map<String, Object> args = new HashMap<>();
                        args.put(R.NavArgs.SESSION_MODEL, _currentSessions.get(newValue.intValue()));
                        _navAdapter.navigate(R.Id.DEST_EDIT_SESSION, args);
                        sessionListV.getSelectionModel().clearSelection();
                    }
                });

        fieldSearch.textProperty()
                .addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable,
                            String oldValue,
                            String newValue) {
                        if (newValue.length() >= 3 || newValue.length() == 0) {
                            _updateListView(newValue);
                        }
                    }
                });
    }

    private void _updateListView(String query) {
        if (query.isEmpty()) {
            _server.getSessions()
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(new _MySingleObserver());
        } else {
            _server.getSessions(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(new _MySingleObserver());
        }
    }

    public void onRefresh(ActionEvent actionEvent) {
        fieldSearch.textProperty().setValue("");
        _updateListView("");
    }

    private class _MySingleObserver implements SingleObserver<List<SessionModel>> {
        @Override
        public void onSubscribe(@NonNull Disposable d) {}

        @Override
        public void onSuccess(@NonNull List<SessionModel> sessionList) {
            _currentSessions = sessionList;
            sessionListV.getItems().clear();
            for (final SessionModel model : sessionList) {
                final SessionTile tile = SessionTile.inflate();
                tile.getLabelTitle().setText(model.getSessionTitle());
                tile.getLabelSessionCode().setText(model.getSessionCode());
                sessionListV.getItems().add(tile.getRoot());
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {}
    }
}
