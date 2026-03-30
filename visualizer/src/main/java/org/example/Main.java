package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        TextField seedField = new TextField();
        seedField.setPromptText("Enter seed");

        TextField sizeField = new TextField();
        sizeField.setPromptText("Enter size");

        Button runButton = new Button("Run");

        VBox layout = new VBox(10, seedField, sizeField, runButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 800, 600);

        stage.setTitle("Maze Runner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
