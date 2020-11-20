package com.project.schoolsystem.ui.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;

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
        navigate(index, null);
    }

    public void navigate(int index, @Nullable Map<String, Object> arguments) {
        final DestinationModel destination = _navigation.getDestinations().get(index);
        if (_anchorPane != null) {
            try {
                final FXMLLoader loader = new FXMLLoader(getClass().getResource(destination.getFxmlPath()));
                final Node node = loader.load();
                if (arguments != null) {
                    // if Destination is no implemented this
                    // will result in a class cast exception as expected
                    final Destination dest = loader.getController();
                    dest.onArguments(arguments);
                }
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

    /**
     * to navigate to undefined destination
     *
     * @param dest
     */
    public void navigate(DestinationModel dest) {
        navigate(dest, null);
    }

    /**
     * to navigate to undefined destination
     *
     * @param dest
     */
    public void navigate(DestinationModel dest, Map<String, Object> arguments) {
        if (_anchorPane != null) {
            try {
                final FXMLLoader loader = new FXMLLoader(getClass().getResource(dest.getFxmlPath()));
                final Node node = loader.load();
                if (arguments != null) {
                    // if Destination is no implemented this
                    // will result in a class cast exception as expected
                    final Destination controller = loader.getController();
                    controller.onArguments(arguments);
                }
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
    }

    public int getSelectedIndex() {
        return _selectedIndex;
    }
}
