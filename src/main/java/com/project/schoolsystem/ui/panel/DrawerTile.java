package com.project.schoolsystem.ui.panel;

import com.project.schoolsystem.R;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import jdk.internal.jline.internal.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DrawerTile implements Initializable {
    @FXML
    private FontAwesomeIconView icon;
    @FXML
    private Label title;
    @FXML
    private HBox root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @Nullable
    public static DrawerTile inflate() {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DrawerTile.class.getResource(R.Fxml.TILE_DRAWER));
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

    public Label getTitle() {
        return title;
    }

    public FontAwesomeIconView getIcon() {
        return icon;
    }
}
