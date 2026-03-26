package maze

import "math/rand"

func generate(size int, seed int64) [][]Cell {
	rng := rand.New(rand.NewSource(seed))

	grid := make([][]Cell, size)

	for r := 0; r < size; r++ {
		grid[r] = make([]Cell, size)
		for c := 0; c < size; c++ {
			grid[r][c] = Cell{Row: r, Col: c, Weight: randomWeight(rng)}
		}
	}

	grid[0][0].Weight = Grass
	grid[size-1][size-1].Weight = Grass

	return grid
}

func randomWeight(rng *rand.Rand) int {
	n := rng.Intn(10)
	switch {
	case n < 2:
		return Wall
	case n < 5:
		return Grass
	case n < 8:
		return Sand
	default:
		return Water
	}
}
