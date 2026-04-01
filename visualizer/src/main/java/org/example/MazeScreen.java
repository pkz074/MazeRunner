package org.example;

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MazeScreen {

    public Scene createScene(Stage stage, Maze maze, List<AgentResult> results) {

        Canvas canvas = new Canvas(600, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Initial draw of the static maze walls/floors
        drawMaze(gc, maze);

        AnimationTimer timer = new AnimationTimer() {
            int frame = 0;

            @Override
            public void handle(long now) {
                for (int i = 0; i < results.size(); i++) {
                    AgentResult agent = results.get(i);
                    int cellSize = 600 / maze.size;

                    // Draw exploration progress
                    if (frame < agent.explored.size()) {
                        Position p = agent.explored.get(frame);
                        Color color = Color.hsb(i * 60, 1.0, 1.0);
                        gc.setFill(color);
                        gc.fillRect(p.col * cellSize, p.row * cellSize, cellSize, cellSize);
                    }

                    // Once exploration finishes, draw the final path
                    if (frame >= agent.explored.size()) {
                        Color color = Color.hsb(i * 60, 1.0, 1.0).brighter();
                        gc.setFill(color);
                        for (Position p : agent.path) {
                            gc.fillRect(p.col * cellSize, p.row * cellSize, cellSize, cellSize);
                        }
                    }
                }
                frame++;
            }
        };

        timer.start();

        // Results Button Setup
        Button resultsButton = new Button("Show Results");
        resultsButton.setMinHeight(40);
        resultsButton.setStyle("-fx-font-size: 14px;");

        resultsButton.setOnAction(e -> {
            // Stop the timer when leaving the screen to save resources
            timer.stop();
            
            ResultsScreen rs = new ResultsScreen();
            // We pass 'stage.getScene()' so the ResultsScreen knows how to come back here
            Scene resultsScene = rs.createScene(stage, results, stage.getScene());
            stage.setScene(resultsScene);
        });

        // Legend setup to identify different agents
        VBox legendBox = new VBox(5);
        for (int i = 0; i < results.size(); i++) {
            AgentResult agent = results.get(i);
            Label label = new Label("■ " + agent.name);
            label.setTextFill(Color.hsb(i * 60, 1.0, 1.0));
            legendBox.getChildren().add(label);
        }

        // Layout Assembly
        VBox root = new VBox(10, canvas, legendBox, resultsButton);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 10; -fx-background-color: #f4f4f4;");

        return new Scene(root, 600, 650);
    }

    private void drawMaze(GraphicsContext gc, Maze maze) {
        int size = maze.size;
        int cellSize = 600 / size;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = maze.grid[row][col];

                switch (value) {
                    case 0:
                        gc.setFill(Color.BLACK);  // Wall
                        break;
                    case 1:
                        gc.setFill(Color.WHITE);  // Grass
                        break;
                    case 2:
                        gc.setFill(Color.YELLOW); // Sand
                        break;
                    case 4:
                        gc.setFill(Color.BLUE);   // Water
                        break;
                    default:
                        gc.setFill(Color.GRAY);
                }
                gc.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
            }
        }
    }
}