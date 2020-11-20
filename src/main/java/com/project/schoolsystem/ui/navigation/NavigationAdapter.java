package com.project.schoolsystem.ui.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import javax.annotation.Nonnull;
import java.io.IOException;

public abstract class NavigationAdapter {
    private final Navigation _navigation;
    private int _selectedIndex;
    private AnchorPane _anchorPane;

    public NavigationAdapter(@Nonnull Navigation navigation) {
        _navigation = navigation;
    }

    public void setUpBody(AnchorPane pane) {
        _anchorPane = pane;
        // navigate to whatever index is selected
        navigate(getSelectedIndex());
    }

    public void navigate(int index) {
        final Destination destination = _navigation.getDestinations().get(index);
        if (_anchorPane != null) {
            try {
                final Node node = FXMLLoader.load(getClass().getResource(destination.getFxmlPath()));
                _anchorPane.getChildren().clear();
                _anchorPane.getChildren().add(node);
                AnchorPane.setTopAnchor(node, 0d);
                AnchorPane.setBottomAnchor(node, 0d);
                AnchorPane.setLeftAnchor(node, 0d);
                AnchorPane.setRightAnchor(node, 0d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        _selectedIndex = index;
    }

    public int getSelectedIndex() {
        return _selectedIndex;
    }
}
