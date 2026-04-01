.PHONY: run build engine-local analysis analysis-single demo clean test

build:
	docker compose build

run:
	docker compose run --rm engine

engine-local:
	cd engine && go run cmd/main.go --seed 42 --size 50 --agents all

analysis:
	pip install -r analysis/requirements.txt -q
	PYTHONPATH=analysis python analysis/src/analyze.py

analysis-single:
	pip install -r analysis/requirements.txt -q
	PYTHONPATH=analysis python -c "from src.analyze import MazeAnalyzer; a = MazeAnalyzer(); a.load_single(); a.generate_charts(); a.generate_summary_md()"

demo:
	docker compose run --rm engine --seed 42 --size 50 --agents all
	pip install -r analysis/requirements.txt -q
	PYTHONPATH=analysis python -c "from src.analyze import MazeAnalyzer; a = MazeAnalyzer(); a.load_single(); a.generate_charts(); a.generate_summary_md()"
	cd visualizer && mvn javafx:run -q

test:
	cd engine && go test ./...

clean:
	rm -rf data/maze.json data/results.json
	rm -rf analysis/output/*.png analysis/output/*.csv analysis/output/*.md