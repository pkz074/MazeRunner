package main

import (
	"flag"
	"fmt"
	"strings"

	"github.com/pkz074/mazerunner/internal/agents"
	_ "github.com/pkz074/mazerunner/internal/agents"
	"github.com/pkz074/mazerunner/internal/maze"
	"github.com/pkz074/mazerunner/internal/output"
)

func main() {
	seed := flag.Int64("seed", 50, "Maze generation seed")
	size := flag.Int("size", 20, "Grid size (NxN)")
	agentList := flag.String("agents", "all", "Comma-separated agents or 'all'")
	outDir := flag.String("out", "../data", "Output directory for JSON files")
	flag.Parse()

	m := maze.New(*size, *seed)

	selected := resolveAgents(*agentList)
	results := agents.RunAll(&m, selected)

	if err := output.Write(*outDir, &m, results); err != nil {
		fmt.Println("error writing output:", err)
		return
	}

	fmt.Printf("done — %dx%d maze, seed %d, %d agents ran\n", *size, *size, *seed, len(results))
}

func resolveAgents(input string) []string {
	if input == "all" {
		return agents.All()
	}
	return strings.Split(input, ",")
}
