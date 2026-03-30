package org.example;
import java.util.List;
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
}
