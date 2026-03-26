package maze

const (
	Wall  = 0
	Grass = 1
	Sand  = 2
	Water = 4
)

type Cell struct {
	Row, Col int
	Weight   int
}

func (c Cell) IsWall() bool {
	return c.Weight == Wall
}
