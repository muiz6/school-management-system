package com.project.schoolsystem.ui.session;

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

public class SessionPage implements Initializable {
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
    }
}
