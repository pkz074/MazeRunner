package agents

import (
	"container/heap"

	"github.com/pkz074/mazerunner/internal/maze"
)

func init() {
	Register(greedyAgent{})
}

type greedyAgent struct{}

func (g greedyAgent) Name() string { return "greedy" }

func (g greedyAgent) Solve(m *maze.Maze) Result {
	start := m.Start
	end := m.End

	pq := &priorityQueue{}
	heap.Init(pq)
	heap.Push(pq, &item{point: start, priority: 0})

	visited := map[maze.Point]bool{}
	parent := map[maze.Point]maze.Point{}
	var explored []maze.Point

	for pq.Len() > 0 {
		current := heap.Pop(pq).(*item)

		if visited[current.point] {
			continue
		}
		visited[current.point] = true
		explored = append(explored, current.point)

		if current.point == end {
			path := reconstructPath(parent, start, end)
			return Result{
				Found:     true,
				Path:      path,
				Explored:  explored,
				NodeCount: len(explored),
				PathCost:  pathCost(m, path),
			}
		}

		for _, neighbor := range m.Neighbors(current.point) {
			if !visited[neighbor] {
				parent[neighbor] = current.point
				h := manhattan(neighbor, end)
				heap.Push(pq, &item{point: neighbor, priority: int(h)})
			}
		}
	}

	return Result{Found: false, Explored: explored, NodeCount: len(explored)}
}
