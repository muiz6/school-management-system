package com.project.schoolsystem.ui.panel;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.model.UserModel;
import com.project.schoolsystem.ui.LoginPage;
import com.project.schoolsystem.ui.navigation.Navigation;
import com.project.schoolsystem.ui.navigation.DrawerAdapter;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPanelPage implements Initializable {
    private final SqlServer server = SqlServer.getInstance();
    @FXML
    private GridPane root;
    @FXML
    private Label labelUserName;
    @FXML
    private AnchorPane mainView;
    @FXML
    private JFXListView drawer;
    @FXML
    private FontAwesomeIconView iconCog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final Gson gson = new Gson();
        final JsonReader jsonReader = new JsonReader(new InputStreamReader(
                getClass().getResourceAsStream(R.Navigation.ADMIN)));
        final Navigation navigation = gson.fromJson(jsonReader, Navigation.class);
        final DrawerAdapter adapter = new DrawerAdapter(navigation);
        adapter.setupWithListView(drawer);
        adapter.setUpBody(mainView);

        final UserModel user = server.getLastSignIn();
        labelUserName.setText(user.getDisplayName());
    }

    public void onUsernameClicked(MouseEvent mouseEvent) {
        final JFXPopup popup = new JFXPopup();
        final SignOutDialog dialog = SignOutDialog.inflate();
        dialog.setSignOutCallback(new SignOutDialog.SignOutCallback() {
            @Override
            public void onSignOut() {
                final Scene scene = root.getScene();
                scene.setRoot(LoginPage.inflate().getRoot());
                // clear session info
                server.setLastSignIn(null);
                popup.hide();
            }
        });
        popup.setPopupContent(dialog.getRoot());
        popup.show(drawer, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT, 10, -10);
    }
}
