package com.project.schoolsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        final Parent root = FXMLLoader.load(getClass().getResource(R.Fxml.LOGIN_PAGE));
        _loadPreferences(primaryStage);
        primaryStage.getIcons().add(new Image(R.Image.ICON));
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void _loadPreferences(Stage primaryStage) {
        final PreferenceProvider pref = PreferenceProvider.getInstance();
        final PreferenceModel model = pref.load();
        primaryStage.setTitle(model.getOrganizationTitle());
    }
}
