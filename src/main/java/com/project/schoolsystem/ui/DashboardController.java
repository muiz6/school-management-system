package com.project.schoolsystem.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.project.schoolsystem.data.model.StudentsModelOld;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private TableView<StudentsModelOld> tbData;
    @FXML
    public TableColumn<StudentsModelOld, Integer> studentId;

    @FXML
    public TableColumn<StudentsModelOld, String> firstName;

    @FXML
    public TableColumn<StudentsModelOld, String> semester;

    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadChart();
        loadStudents();
    }

    private void loadChart()
    {

        PieChart.Data slice1 = new PieChart.Data("Classes", 213);
        PieChart.Data slice2 = new PieChart.Data("Attendance"  , 67);
        PieChart.Data slice3 = new PieChart.Data("Teachers" , 36);

        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);
        pieChart.getData().add(slice3);

    }


    private final ObservableList<StudentsModelOld> studentsModels = FXCollections.observableArrayList(new StudentsModelOld(1,"Amna", "4s", 4, 4500),
            new StudentsModelOld(2,"Muiz", "4s", 4, 4500),
            new StudentsModelOld(3,"Sufwan", "4s", 4, 4500),
            new StudentsModelOld(4,"Dawood", "3s", 3, 4500)
    );

    private void loadStudents()
    {
        studentId.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        semester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        tbData.setItems(studentsModels);
    }

}
