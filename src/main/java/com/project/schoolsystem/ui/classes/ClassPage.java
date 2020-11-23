package com.project.schoolsystem.ui.classes;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.project.schoolsystem.R;
import com.project.schoolsystem.ui.navigation.Navigation;
import com.project.schoolsystem.ui.navigation.TabAdapter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class ClassPage implements Initializable {
    private final TabAdapter _navAdapter;
    @FXML
    private AnchorPane tabBody;
    @FXML
    private GridPane tabHeader;

    public ClassPage() {
        final Gson gson = new Gson();
        final JsonReader jsonReader = new JsonReader(new InputStreamReader(
                getClass().getResourceAsStream(R.Navigation.MANAGE_CLASSES)));
        final Navigation navigation = gson.fromJson(jsonReader, Navigation.class);
        _navAdapter = new TabAdapter(navigation);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _navAdapter.setupHeader(tabHeader);
        _navAdapter.setUpBody(tabBody);
    }
}
