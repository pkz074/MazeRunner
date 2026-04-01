package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.Paths;

public class MazeLoader {

    public static Maze load() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String path = Paths.get("")
                .toAbsolutePath()
                .getParent()
                .resolve("data/maze.json")
                .toString();
            System.out.println("Loading maze from: " + path);
            return mapper.readValue(new File(path), Maze.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
