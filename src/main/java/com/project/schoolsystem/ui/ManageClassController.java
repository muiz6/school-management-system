package com.project.schoolsystem.ui;

import com.project.schoolsystem.R;
import com.project.schoolsystem.data.SqlServer;
import com.project.schoolsystem.data.model.ClassModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManageClassController implements Initializable {
    private final SqlServer db = SqlServer.getInstance();
    @FXML
    private ListView listView;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _getData();
    }

    public void onUpdate(ActionEvent evt) {
        _getData();
    }

    private void _getData() {
        listView.getItems().clear();
        db.getClasses(new SqlServer.OnCompletionCallback<List<ClassModel>>() {
            @Override
            public void onResult(boolean success, List<ClassModel> Result) {
                if (success) {
                    for (final ClassModel model : Result) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource(R.Fxml.TILE_CLASS));
                            Node node = loader.load();
                            ClassTileController controller = loader.getController();
                            controller.setClassId(model.getId());
                            controller.setClassName(model.getName());
                            controller.setStudentCount(model.getStudentCount());
                            controller.setTermId(model.getTermId());
                            listView.getItems().add(node);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
