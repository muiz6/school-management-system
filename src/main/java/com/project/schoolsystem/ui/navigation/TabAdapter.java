package com.project.schoolsystem.ui.navigation;

import com.project.schoolsystem.ui.tabs.Tab;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends NavigationAdapter {
    private final Navigation _navigation;
    private final List<Tab> _tabs = new ArrayList<>();

    public TabAdapter(@Nonnull Navigation navigation) {
        super(navigation);
        _navigation = navigation;
    }

    public void setupHeader(GridPane gridPane) {
        gridPane.getChildren().clear();
        final List<DestinationModel> destinations = _navigation.getDestinations();
        for (int i = 0; i < destinations.size(); i++) {
            final DestinationModel dest = destinations.get(i);
            final Tab tab = Tab.inflate();
            tab.setText(dest.getTitle());
            gridPane.add(tab.getRoot(), i, 0);
            _tabs.add(tab);
            tab.getRoot().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    _tabs.get(getSelectedIndex()).setSelected(false);
                    tab.setSelected(true);
                    navigate(_tabs.indexOf(tab));
                }
            });
            // select first tab
            tab.setSelected(i == 0);
        }
    }
}
