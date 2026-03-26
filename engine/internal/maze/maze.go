package maze

type Maze struct {
	Size  int
	Seed  int64
	Grid  [][]Cell
	Start Point
	End   Point
}

type Point struct {
	Row, Col int
}

func New(size int, seed int64) Maze {
	m := &Maze{
		Size:  size,
		Seed:  seed,
		Start: Point{0, 0},
		End:   Point{size - 1, size - 1},
	}
	m.Grid = generate(size, seed)
	return *m
}

func (m *Maze) InBounds(row int, col int) bool {
	return row >= 0 && col >= 0 && row < m.Size && col < m.Size
}

func (m *Maze) Neighbors(p Point) []Point {
	dirs := []Point{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}
	var result []Point
	for _, d := range dirs {
		r, c := p.Row+d.Row, p.Col+d.Col
		if m.InBounds(r, c) && !m.Grid[r][c].IsWall() {
			result = append(result, Point{r, c})
		}
	}
	return result
}
