package output

import (
	"encoding/json"
	"fmt"
	"os"
	"path/filepath"

	"github.com/pkz074/mazerunner/internal/agents"
	"github.com/pkz074/mazerunner/internal/maze"
)

type mazeOutput struct {
	Seed  int64      `json:"seed"`
	Size  int        `json:"size"`
	Start maze.Point `json:"start"`
	End   maze.Point `json:"end"`
	Grid  [][]int    `json:"grid"`
}

func Write(dir string, m *maze.Maze, results []agents.Result) error {
	if err := writeMaze(dir, m); err != nil {
		return fmt.Errorf("maze: %w", err)
	}
	if err := writeResults(dir, results); err != nil {
		return fmt.Errorf("results: %w", err)
	}
	return nil
}

func writeMaze(dir string, m *maze.Maze) error {
	grid := make([][]int, m.Size)
	for r := 0; r < m.Size; r++ {
		grid[r] = make([]int, m.Size)
		for c := 0; c < m.Size; c++ {
			grid[r][c] = m.Grid[r][c].Weight
		}
	}
	out := mazeOutput{
		Seed:  m.Seed,
		Size:  m.Size,
		Start: m.Start,
		End:   m.End,
		Grid:  grid,
	}
	return writeJSON(filepath.Join(dir, "maze.json"), out)
}

func writeResults(dir string, results []agents.Result) error {
	return writeJSON(filepath.Join(dir, "results.json"), results)
}

func writeJSON(path string, v any) error {
	f, err := os.Create(path)
	if err != nil {
		return err
	}
	defer f.Close()
	enc := json.NewEncoder(f)
	enc.SetIndent("", "  ")
	return enc.Encode(v)
}
