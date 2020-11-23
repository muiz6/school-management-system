package com.project.schoolsystem.ui.students;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.ui.navigation.Destination;
import com.project.schoolsystem.ui.navigation.Navigation;
import com.project.schoolsystem.ui.navigation.TabAdapter;
import io.reactivex.annotations.NonNull;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class StudentPage implements Initializable, Destination {
    private final SqlServer _server = SqlServer.getInstance();
    private final TabAdapter _navAdapter;
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
    }

    @Override
    public void onArguments(@NonNull Map<String, Object> arguments) {}

    public void onRefresh(ActionEvent actionEvent) {}
}
