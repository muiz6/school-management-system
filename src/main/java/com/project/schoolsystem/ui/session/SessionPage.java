package com.project.schoolsystem.ui.session;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jfoenix.controls.JFXListView;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.model.SessionModel;
import com.project.schoolsystem.ui.navigation.Navigation;
import com.project.schoolsystem.ui.navigation.TabAdapter;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SessionPage implements Initializable {
    private final SqlServer _server = SqlServer.getInstance();
    @FXML
    private TextField fieldSearch;
    @FXML
    private JFXListView sessionListV;
    @FXML
    private GridPane tabHeader;
    @FXML
    private AnchorPane tabContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final Gson gson = new Gson();
        final JsonReader jsonReader = new JsonReader(new InputStreamReader(
                getClass().getResourceAsStream(R.Navigation.SESSION)));
        final Navigation navigation = gson.fromJson(jsonReader, Navigation.class);
        final TabAdapter adapter = new TabAdapter(navigation);
        adapter.setupHeader(tabHeader);
        adapter.setUpBody(tabContent);

        _updateListView();
    }

    private void _updateListView() {
        _server.getSessions()
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new _MySingleObserver());
    }

    public void onRefresh(ActionEvent actionEvent) {
        fieldSearch.textProperty().setValue("");
        _updateListView();
    }

    private class _MySingleObserver implements SingleObserver<List<SessionModel>> {
        @Override
        public void onSubscribe(@NonNull Disposable d) {}

        @Override
        public void onSuccess(@NonNull List<SessionModel> sessionList) {
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
