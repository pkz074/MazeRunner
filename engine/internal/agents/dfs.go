package agents

import "github.com/pkz074/mazerunner/internal/maze"

func init() {
	Register(dfsAgent{})
}

type dfsAgent struct{}

func (d dfsAgent) Name() string { return "dfs" }

func (d dfsAgent) Solve(m *maze.Maze) Result {
	start := m.Start
	end := m.End

	visited := map[maze.Point]bool{}
	parent := map[maze.Point]maze.Point{}
	stack := []maze.Point{start}
	var explored []maze.Point

	for len(stack) > 0 {
		current := stack[len(stack)-1]
		stack = stack[:len(stack)-1]

		if visited[current] {
			continue
		}
		visited[current] = true
		explored = append(explored, current)

		if current == end {
			return Result{
				Found:     true,
				Path:      reconstructPath(parent, start, end),
				Explored:  explored,
				NodeCount: len(explored),
				PathCost:  pathCost(m, reconstructPath(parent, start, end)),
			}
		}

		for _, neighbor := range m.Neighbors(current) {
			if !visited[neighbor] {
				parent[neighbor] = current
				stack = append(stack, neighbor)
			}
		}
	}

	return Result{Found: false, Explored: explored, NodeCount: len(explored)}
}
