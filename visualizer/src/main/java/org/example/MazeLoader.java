package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

public class MazeLoader {

    public static Maze load() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File("data/maze.json"), Maze.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
