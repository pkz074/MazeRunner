.PHONY: run build engine-local analysis analysis-single clean test

build:
	docker compose build

run:
	docker compose run --rm engine

engine-local:
	cd engine && go run cmd/main.go --seed 42 --size 50 --agents all

analysis:
	cd analysis && pip install -r requirements.txt -q && python src/analyzer.py

analysis-single:
	cd analysis && pip install -r requirements.txt -q && python -c "from src.analyzer import MazeAnalyzer; a = MazeAnalyzer(); a.load_single(); a.generate_charts(); a.generate_summary_md()"

test:
	cd engine && go test ./...

clean:
	rm -f data/maze.json data/results.json
	rm -f analysis/output/*.png analysis/output/*.csv analysis/output/*.md
