package com.project.schoolsystem.ui.navigation;

import com.project.schoolsystem.ui.panel.DrawerTile;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import javax.annotation.Nonnull;

public class DrawerAdapter extends NavigationAdapter {
    private final Navigation _navigation;
    private ListView _listView;

    public DrawerAdapter(@Nonnull Navigation navigation) {
        super(navigation);
        _navigation = navigation;
    }

    @Override
    public void navigate(DestinationModel dest) {
        super.navigate(dest);
        // clear selection in List view as this method is used to navigate
        // to an undefined destination
        if (_listView != null) {
            _listView.getSelectionModel().clearSelection();
        }
    }

    public void setupWithListView(ListView listView) {
        _listView = listView;
        final ObservableList list = listView.getItems();
        list.clear();
        for (final DestinationModel destination : _navigation.getDestinations()) {
            final DrawerTile tile = DrawerTile.inflate();
            tile.getIcon().setGlyphName(destination.getGlyphName());
            tile.getTitle().setText(destination.getTitle());
            list.add(tile.getRoot());
        }
        listView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                navigate(newValue.intValue());
            }
        });
        // select acc to selected destination
        listView.getSelectionModel().select(getSelectedIndex());
    }
}
