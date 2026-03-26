package agents

import (
	"container/heap"

	"github.com/pkz074/mazerunner/internal/maze"
)

func init() {
	Register(ucsAgent{})
}

type ucsAgent struct{}

func (u ucsAgent) Name() string { return "ucs" }

func (u ucsAgent) Solve(m *maze.Maze) Result {
	start := m.Start
	end := m.End

	pq := &priorityQueue{}
	heap.Init(pq)
	heap.Push(pq, &item{point: start, priority: 0})

	cost := map[maze.Point]int{start: 0}
	parent := map[maze.Point]maze.Point{}
	var explored []maze.Point

	for pq.Len() > 0 {
		current := heap.Pop(pq).(*item)
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
			newCost := cost[current.point] + m.Grid[neighbor.Row][neighbor.Col].Weight
			if existing, seen := cost[neighbor]; !seen || newCost < existing {
				cost[neighbor] = newCost
				parent[neighbor] = current.point
				heap.Push(pq, &item{point: neighbor, priority: newCost})
			}
		}
	}

	return Result{Found: false, Explored: explored, NodeCount: len(explored)}
}
