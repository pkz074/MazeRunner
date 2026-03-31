package org.example;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MazeScreen {

    public Scene createScene(Stage stage, Maze maze, List<AgentResult> results) {

        Canvas canvas = new Canvas(600, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawMaze(gc, maze);

        AnimationTimer timer = new AnimationTimer() {

            int frame = 0;

            @Override
            public void handle(long now) {

                for (int i = 0; i < results.size(); i++) {

                    AgentResult agent = results.get(i);

                    int cellSize = 600 / maze.size;

                    if (frame < agent.explored.size()) {

                        Position p = agent.explored.get(frame);

                        Color color = Color.hsb(i * 60, 1.0, 1.0);
                        gc.setFill(color);

                        gc.fillRect(
                                p.col * cellSize,
                                p.row * cellSize,
                                cellSize,
                                cellSize
                        );
                    }

                    if (frame >= agent.explored.size()) {

                        Color color = Color.hsb(i * 60, 1.0, 1.0).brighter();
                        gc.setFill(color);

                        for (Position p : agent.path) {

                            gc.fillRect(
                                    p.col * cellSize,
                                    p.row * cellSize,
                                    cellSize,
                                    cellSize
                            );
                        }
                    }
                }

                frame++;
            }

        };

        timer.start();

        Button resultsButton = new Button("Show Results");
        resultsButton.setMinHeight(40);
        resultsButton.setStyle("-fx-font-size: 14px;");

        resultsButton.setOnAction(e -> {
            ResultsScreen rs = new ResultsScreen();
            Scene resultScene = rs.createScene(stage, results);

            stage.setScene(resultScene);
        });


        VBox legendBox = new VBox();
        for (int i = 0; i < results.size(); i++) {
            AgentResult agent = results.get(i);

            Label label = new Label(agent.name);
            label.setTextFill(Color.hsb(i * 60, 1.0, 1.0));

            legendBox.getChildren().add(label);
        }

        VBox root = new VBox(10, canvas, legendBox, resultsButton);
        root.setStyle("-fx-padding: 10;");
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
                        gc.setFill(javafx.scene.paint.Color.BLACK); //wall
                        break;
                    case 1:
                        gc.setFill(javafx.scene.paint.Color.WHITE); //grass
                        break;
                    case 2:
                        gc.setFill(javafx.scene.paint.Color.YELLOW); //sand
                        break;
                    case 4:
                        gc.setFill(javafx.scene.paint.Color.BLUE); //water
                        break;
                    default:
                        gc.setFill(javafx.scene.paint.Color.GRAY);
                }

                gc.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
            }
        }
    }
}
