.PHONY: run build clean

build:
	docker compose build

run:
	docker compose run --rm engine --seed 42 --size 50 --agents all

run-local:
	cd engine && go run cmd/main.go --seed 42 --size 50 --agents all

test:
	cd engine && go test ./...

clean:
	rm -f data/maze.json data/results.json
