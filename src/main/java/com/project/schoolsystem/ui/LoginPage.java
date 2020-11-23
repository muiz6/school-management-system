package com.project.schoolsystem.ui;

import com.jfoenix.controls.JFXButton;
import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.models.UserModel;
import com.project.schoolsystem.ui.snackbar.Snackbar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPage implements Initializable {
    final SqlServer server = SqlServer.getInstance();
    @FXML
    private GridPane root;
    @FXML
    private TextField fieldUserName;
    @FXML
    private PasswordField fieldPwd;
    @FXML
    private JFXButton btnSignIn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server.connect(new SqlServer.OnCompletionCallback<String>() {
            @Override
            public void onResult(boolean success, String result) {
                if (success) {
                    new Snackbar(root).enqueue("Connected to Database", Snackbar.STATUS_SUCCESS);
                } else {
                    new Snackbar(root).enqueue("Could not connect to Database!", Snackbar.STATUS_ERROR);
                }
            }
        });
    }

    @Nullable
    public static LoginPage inflate() {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource(R.Fxml.LOGIN_PAGE));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onSignIn(ActionEvent actionEvent) {
        signIn();
    }

    public GridPane getRoot() {
        return root;
    }

    private void signIn() {
        final String userName = fieldUserName.getText();
        final String password = fieldPwd.getText();
        final UserModel model = server.getSignedUser(userName, password);
        if (model != null) {
            try {
                final String role = model.getRole();
                final Parent newRoot;
                if (UserModel.ROLE_TEACHER.equals(role)) {
                    newRoot = FXMLLoader.load(getClass().getResource(R.Fxml.TEACHER_PAGE));
                } else {
                    newRoot = FXMLLoader.load(getClass().getResource(R.Fxml.ADMIN_PAGE));
                }
                final Scene scene = root.getScene();
                scene.setRoot(newRoot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new Snackbar(root).enqueue("Sign In Failed!", Snackbar.STATUS_ERROR);
        }
    }
}
