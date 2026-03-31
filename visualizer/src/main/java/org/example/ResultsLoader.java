package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ResultsLoader {

    public static List<AgentResult> load() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            AgentResult[] results = mapper.readValue(
                    new File("../data/results.json"),
                    AgentResult[].class
            );
            return Arrays.asList(results);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}