package com.project.schoolsystem.ui.panel;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.project.schoolsystem.PreferenceModel;
import com.project.schoolsystem.PreferenceProvider;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.model.UserModel;
import com.project.schoolsystem.ui.LoginPage;
import com.project.schoolsystem.ui.navigation.DestinationModel;
import com.project.schoolsystem.ui.navigation.DrawerAdapter;
import com.project.schoolsystem.ui.navigation.Navigation;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.reactivex.functions.Consumer;
import javafx.event.ActionEvent;
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

public class TeacherPanelPage implements Initializable {
    private final SqlServer _server = SqlServer.getInstance();
    private final DrawerAdapter _drawerAdapter;
    @FXML
    private Label labelOrganizationTitle;
    @FXML
    private GridPane root;
    @FXML
    private Label labelUserName;
    @FXML
    private JFXListView drawer;
    @FXML
    private FontAwesomeIconView iconCog;
    @FXML
    private AnchorPane mainView;

    public TeacherPanelPage() {
        final Gson gson = new Gson();
        final JsonReader jsonReader = new JsonReader(new InputStreamReader(
                getClass().getResourceAsStream(R.Navigation.ADMIN)));
        final Navigation navigation = gson.fromJson(jsonReader, Navigation.class);
        _drawerAdapter = new DrawerAdapter(navigation);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _drawerAdapter.setupWithListView(drawer);
        _drawerAdapter.setUpBody(mainView);

        _initPrefs();
        _server.observeLastSignIn()
                .subscribe(new Consumer<UserModel>() {
                    @Override
                    public void accept(UserModel userModel) throws Exception {
                        labelUserName.setText(userModel.getDisplayName());
                    }
                });
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
                _server.setLastSignIn(null);
                popup.hide();
            }
        });
        popup.setPopupContent(dialog.getRoot());
        popup.show(drawer, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT, 10, -10);
    }

    public void onSettingClicked(ActionEvent actionEvent) {
        _drawerAdapter.navigate(new DestinationModel(
                null,
                "Settings",
                R.Fxml.TEACHER_SETTINGS,
                null));
    }

    private void _initPrefs() {
        PreferenceProvider.getInstance().observePreference().subscribe(
                new Consumer<PreferenceModel>() {
                    @Override
                    public void accept(PreferenceModel model) throws Exception {
                        final String title = model.getOrganizationTitle();
                        labelOrganizationTitle.setText(title);
                    }
                });
    }
}
