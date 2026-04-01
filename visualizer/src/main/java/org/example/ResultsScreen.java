package org.example;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResultsScreen {

    // Added a parameter for the previous scene or a callback
    public Scene createScene(Stage stage, List<AgentResult> results, Scene previousScene) {

        // 1. Create the TableView
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

        // 2. Create the Back Button
        Button backButton = new Button("Back to Simulation");
        backButton.setOnAction(e -> stage.setScene(previousScene));

        // 3. Layout: Use a VBox to stack the table and the button
        VBox layout = new VBox(10); // 10px spacing
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);
        
        // Add the table (expanded to fill space) and the button
        layout.getChildren().addAll(table, backButton);
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        return new Scene(layout, 600, 450);
    }
}