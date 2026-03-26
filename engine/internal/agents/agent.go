package agents

import (
	"fmt"
	"time"

	"github.com/pkz074/mazerunner/internal/maze"
)

// Result holds everything recorded for one agent run.
type Result struct {
	Agent     string       `json:"agent"`
	Path      []maze.Point `json:"path"`
	Explored  []maze.Point `json:"explored"`
	TimeUS    int64        `json:"time_us"`
	NodeCount int          `json:"nodes_expanded"`
	PathCost  int          `json:"path_cost"`
	Found     bool         `json:"found"`
}

// Agent is the interface every algorithm must implement.
// To add a new algorithm: implement this interface and call Register().
type Agent interface {
	Name() string
	Solve(m *maze.Maze) Result
}

var registry = map[string]Agent{}

// Register adds an agent to the registry.
// Call this in each algorithm file's init() function.
func Register(a Agent) {
	registry[a.Name()] = a
}

// All returns every registered agent name.
func All() []string {
	names := make([]string, 0, len(registry))
	for name := range registry {
		names = append(names, name)
	}
	return names
}

// Run executes a single agent and returns its result.
func Run(m *maze.Maze, name string) (Result, error) {
	a, ok := registry[name]
	if !ok {
		return Result{}, fmt.Errorf("unknown agent: %s", name)
	}
	start := time.Now()
	r := a.Solve(m)
	r.Agent = name
	r.TimeUS = time.Since(start).Microseconds()
	return r, nil
}

// RunAll executes all selected agents and returns their results.
func RunAll(m *maze.Maze, selected []string) []Result {
	var results []Result
	for _, name := range selected {
		r, err := Run(m, name)
		if err != nil {
			fmt.Println("warning:", err)
			continue
		}
		results = append(results, r)
	}
	return results
}
