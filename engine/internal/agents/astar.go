package agents

import (
	"container/heap"
	"math"

	"github.com/pkz074/mazerunner/internal/maze"
)

func init() {
	Register(astarAgent{name: "astar_manhattan", h: manhattan})
	Register(astarAgent{name: "astar_euclidean", h: euclidean})
}

type heuristic func(a, b maze.Point) float64

type astarAgent struct {
	name string
	h    heuristic
}

func (a astarAgent) Name() string { return a.name }

func (a astarAgent) Solve(m *maze.Maze) Result {
	start := m.Start
	end := m.End

	pq := &priorityQueue{}
	heap.Init(pq)
	heap.Push(pq, &item{point: start, priority: 0})

	gCost := map[maze.Point]int{start: 0}
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
			newG := gCost[current.point] + m.Grid[neighbor.Row][neighbor.Col].Weight
			if existing, seen := gCost[neighbor]; !seen || newG < existing {
				gCost[neighbor] = newG
				parent[neighbor] = current.point
				f := newG + int(a.h(neighbor, end))
				heap.Push(pq, &item{point: neighbor, priority: f})
			}
		}
	}

	return Result{Found: false, Explored: explored, NodeCount: len(explored)}
}

func manhattan(a, b maze.Point) float64 {
	return math.Abs(float64(a.Row-b.Row)) + math.Abs(float64(a.Col-b.Col))
}

func euclidean(a, b maze.Point) float64 {
	dr := float64(a.Row - b.Row)
	dc := float64(a.Col - b.Col)
	return math.Sqrt(dr*dr + dc*dc)
}
