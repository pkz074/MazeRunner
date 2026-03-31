package org.example;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class ResultsScreen {

        public Scene createScene(Stage stage, List<AgentResult> results) {

            TableView<AgentResult> table = new TableView<>();

            TableColumn<AgentResult, String> nameCol = new TableColumn<>("Agent");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<AgentResult, Boolean> foundCol = new TableColumn<>("Found");
            foundCol.setCellValueFactory(new PropertyValueFactory<>("found"));

            TableColumn<AgentResult, Integer> costCol = new TableColumn<>("Path Cost");
            costCol.setCellValueFactory(new PropertyValueFactory<>("path_cost"));

            TableColumn<AgentResult, Integer> nodesCol = new TableColumn<>("Nodes");
            nodesCol.setCellValueFactory(new PropertyValueFactory<>("nodes_expanded"));

            TableColumn<AgentResult, Long> timeCol = new TableColumn<>("Time (us)");
            timeCol.setCellValueFactory(new PropertyValueFactory<>("time_us"));

            table.getColumns().addAll(nameCol, foundCol, costCol, nodesCol, timeCol);

            table.setItems(FXCollections.observableArrayList(results));

            table.getSortOrder().add(timeCol);
            timeCol.setSortType(TableColumn.SortType.ASCENDING);

            return new Scene(table, 600, 400);


        }



    }
