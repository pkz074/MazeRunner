package agents

import "github.com/pkz074/mazerunner/internal/maze"

func init() {
	Register(bfsAgent{})
}

type bfsAgent struct{}

func (b bfsAgent) Name() string { return "bfs" }

func (b bfsAgent) Solve(m *maze.Maze) Result {
	start := m.Start
	end := m.End

	visited := map[maze.Point]bool{start: true}
	parent := map[maze.Point]maze.Point{}
	queue := []maze.Point{start}
	var explored []maze.Point

	for len(queue) > 0 {
		current := queue[0]
		queue = queue[1:]
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
				visited[neighbor] = true
				parent[neighbor] = current
				queue = append(queue, neighbor)
			}
		}
	}

	return Result{Found: false, Explored: explored, NodeCount: len(explored)}
}
