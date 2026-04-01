package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ResultsLoader {

    public static List<AgentResult> load() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String path = Paths.get("")
                .toAbsolutePath()
                .getParent()
                .resolve("data/results.json")
                .toString();
            System.out.println("Loading results from: " + path);
            AgentResult[] results = mapper.readValue(
                new File(path),
                AgentResult[].class
            );
            return Arrays.asList(results);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
