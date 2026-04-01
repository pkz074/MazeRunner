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

        runButton.setOnAction(e -> {
            try {
                String seed = seedField.getText();
                String size = sizeField.getText();

                //Run the engine
                ProcessBuilder pb = new ProcessBuilder(
                    "docker",
                    "compose",
                    "run",
                    "--rm",
                    "engine",
                    "--seed",
                    seed,
                    "--size",
                    size,
                    "--agents",
                    "all"
                );
                pb.directory(new java.io.File("../"));
                Process process = pb.start();
                int exitCode = process.waitFor();
                System.out.println("Engine exit code: " + exitCode);
                if (exitCode != 0) {
                    System.out.println("Engine failed!");
                    return;
                }
                // Load JSON
                Maze maze = MazeLoader.load();
                java.util.List<AgentResult> results = ResultsLoader.load();

                System.out.println("Loaded maze size: " + maze.size);

                // Switch to Maze Screen
                MazeScreen mazeScreen = new MazeScreen();
                Scene mazeScene = mazeScreen.createScene(stage, maze, results);

                stage.setScene(mazeScene);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

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
