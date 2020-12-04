package com.project.schoolsystem.ui.panel;

import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignOutDialog implements Initializable {
    private SignOutCallback callback;
    @FXML
    private HBox root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Nullable
    public static SignOutDialog inflate() {
        final FXMLLoader loader = new FXMLLoader(SignOutDialog.class.getResource(R.Fxml.DIALOG_SIGN_OUT));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HBox getRoot() {
        return root;
    }

    public void onDialogClicked(MouseEvent mouseEvent) {
        callback.onSignOut();
    }

    public void setSignOutCallback(SignOutCallback callback) {
        this.callback = callback;
    }

    public interface SignOutCallback {
        void onSignOut();
    }
}
