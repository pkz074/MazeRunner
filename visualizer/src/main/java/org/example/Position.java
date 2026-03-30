package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {
    @JsonProperty("Row")
    public int row;

    @JsonProperty("Col")
    public int col;
}
