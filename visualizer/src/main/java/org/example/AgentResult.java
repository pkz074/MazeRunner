package org.example;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AgentResult {
    @JsonProperty("agent")
    public String name;
    public boolean found;
    public java.util.List<Position> path;
    public java.util.List<Position> explored;
    public long time_us;
    public int nodes_expanded;
    public double path_cost;

    public String getName() {
        return name;
    }

    public boolean getFound() {
        return found;
    }

    public double getPath_cost() {
        return path_cost;
    }

    public int getNodes_expanded() {
        return nodes_expanded;
    }

    public long getTime_us() {
        return time_us;
    }
}
