package com.project.schoolsystem.ui.navigation;

import com.project.schoolsystem.ui.tabs.Tab;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TabAdapter extends NavigationAdapter {
    private final Navigation _navigation;
    private final List<Tab> _tabs = new ArrayList<>();
    private int _selectedIndex = 0;

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

    @Override
    public void navigate(@NonNull String destinationId, @Nullable Map<String, Object> arguments) {
        _tabs.get(getSelectedIndex()).setSelected(false);
        super.navigate(destinationId, arguments);
        _tabs.get(getSelectedIndex()).setSelected(true);
    }
}
