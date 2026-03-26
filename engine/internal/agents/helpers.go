package agents

import "github.com/pkz074/mazerunner/internal/maze"

// reconstructPath walks the parent map backwards from end to start.
func reconstructPath(parent map[maze.Point]maze.Point, start, end maze.Point) []maze.Point {
	path := []maze.Point{}
	current := end
	for current != start {
		path = append([]maze.Point{current}, path...)
		current = parent[current]
	}
	return append([]maze.Point{start}, path...)
}

// pathCost sums the weights of all cells along a path.
func pathCost(m *maze.Maze, path []maze.Point) int {
	total := 0
	for _, p := range path {
		total += m.Grid[p.Row][p.Col].Weight
	}
	return total
}
