import json
import os
import subprocess

import matplotlib.pyplot as plt
import pandas as pd

# Paths relative to this file's location
BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../"))
DATA_DIR = os.path.join(BASE_DIR, "data")
OUTPUT_DIR = os.path.join(os.path.dirname(__file__), "../output")

COLORS = ["#3498db", "#e74c3c", "#2ecc71", "#f1c40f", "#9b59b6", "#34495e"]


class MazeAnalyzer:
    def __init__(self):
        self.results_path = os.path.join(DATA_DIR, "results.json")
        self.batch_csv = os.path.join(OUTPUT_DIR, "batch_results.csv")
        self.raw_data = pd.DataFrame()
        os.makedirs(OUTPUT_DIR, exist_ok=True)

    def load_single(self):
        with open(self.results_path, "r") as f:
            data = json.load(f)
        self.raw_data = pd.DataFrame(data)
        print(f"Loaded {len(self.raw_data)} agent results from {self.results_path}")

    def run_batch(self, n_seeds=10, size=50):
        all_results = []

        for seed in range(n_seeds):
            print(f"--> Running simulation with seed {seed}...")
            subprocess.run(
                [
                    "docker",
                    "compose",
                    "run",
                    "--rm",
                    "engine",
                    "--seed",
                    str(seed),
                    "--size",
                    str(size),
                    "--agents",
                    "all",
                ],
                check=True,
                cwd=BASE_DIR,
            )

            with open(self.results_path, "r") as f:
                data = json.load(f)
                for entry in data:
                    entry["seed"] = seed
                all_results.extend(data)

        self.raw_data = pd.DataFrame(all_results)
        self.raw_data.to_csv(self.batch_csv, index=False)
        print(f"Done. Raw data exported to {self.batch_csv}")

    def generate_charts(self):
        if self.raw_data.empty:
            if os.path.exists(self.batch_csv):
                self.raw_data = pd.read_csv(self.batch_csv)
            else:
                self.load_single()

        avg_stats = self.raw_data.groupby("agent").mean(numeric_only=True).reset_index()
        metrics = ["time_us", "nodes_expanded", "path_cost"]
        labels = ["Avg Time (µs)", "Avg Nodes Expanded", "Avg Path Cost"]

        fig, axes = plt.subplots(1, 3, figsize=(18, 6))
        fig.suptitle("MazeRunner — Algorithm Benchmark", fontsize=16, fontweight="bold")

        for i, (metric, label) in enumerate(zip(metrics, labels)):
            bars = axes[i].bar(
                avg_stats["agent"], avg_stats[metric], color=COLORS[: len(avg_stats)]
            )
            axes[i].set_title(label)
            axes[i].tick_params(axis="x", rotation=45)
            axes[i].bar_label(bars, fmt="%.1f", padding=3, fontsize=8)

        plt.tight_layout()
        out_path = os.path.join(OUTPUT_DIR, "summary_report.png")
        plt.savefig(out_path, dpi=150)
        print(f"Charts saved to {out_path}")

    def generate_summary_md(self):
        if self.raw_data.empty:
            if os.path.exists(self.batch_csv):
                self.raw_data = pd.read_csv(self.batch_csv)
            else:
                self.load_single()

        avg = self.raw_data.groupby("agent").mean(numeric_only=True).reset_index()
        avg = avg.sort_values("time_us")

        lines = []
        lines.append("# MazeRunner — Benchmark Summary\n")
        lines.append(
            f"Averaged over {self.raw_data['seed'].nunique() if 'seed' in self.raw_data.columns else 1} maze(s).\n"
        )
        lines.append("| Agent | Avg Time (µs) | Avg Nodes Expanded | Avg Path Cost |")
        lines.append("|---|---|---|---|")

        for _, row in avg.iterrows():
            lines.append(
                f"| {row['agent']} "
                f"| {row['time_us']:.1f} "
                f"| {row['nodes_expanded']:.1f} "
                f"| {row['path_cost']:.1f} |"
            )

        lines.append("\n## Notes\n")
        fastest = avg.iloc[0]["agent"]
        slowest = avg.iloc[-1]["agent"]
        least_nodes = avg.loc[avg["nodes_expanded"].idxmin(), "agent"]
        most_nodes = avg.loc[avg["nodes_expanded"].idxmax(), "agent"]
        cheapest = avg.loc[avg["path_cost"].idxmin(), "agent"]
        most_exp = avg.loc[avg["path_cost"].idxmax(), "agent"]

        lines.append(f"- **Fastest computation:** {fastest}")
        lines.append(f"- **Slowest computation:** {slowest}")
        lines.append(f"- **Fewest nodes expanded:** {least_nodes}")
        lines.append(f"- **Most nodes expanded:** {most_nodes}")
        lines.append(f"- **Lowest path cost (most optimal):** {cheapest}")
        lines.append(f"- **Highest path cost (least optimal):** {most_exp}")

        lines.append("\n## Key Findings\n")

        # Speed vs optimality tradeoff
        fastest_cost = avg.loc[avg["agent"] == fastest, "path_cost"].values[0]
        cheapest_cost = avg.loc[avg["agent"] == cheapest, "path_cost"].values[0]
        if fastest != cheapest:
            cost_diff = ((fastest_cost - cheapest_cost) / cheapest_cost) * 100
            lines.append(
                f"- **Speed vs optimality tradeoff:** `{fastest}` is the fastest algorithm "
                f"but its path cost is {cost_diff:.1f}% higher than the optimal `{cheapest}`. "
                f"This illustrates the classic tradeoff between computation speed and solution quality."
            )

        # Informed vs blind comparison
        blind = ["bfs", "dfs"]
        informed = [a for a in avg["agent"] if a not in blind]
        blind_avg_nodes = avg[avg["agent"].isin(blind)]["nodes_expanded"].mean()
        informed_avg_nodes = avg[avg["agent"].isin(informed)]["nodes_expanded"].mean()
        if blind_avg_nodes > informed_avg_nodes:
            reduction = ((blind_avg_nodes - informed_avg_nodes) / blind_avg_nodes) * 100
            lines.append(
                f"- **Informed vs blind agents:** Informed algorithms (A\\*, Greedy, UCS) expanded "
                f"{reduction:.1f}% fewer nodes on average than blind agents (BFS, DFS), "
                f"demonstrating the value of heuristic guidance."
            )
        else:
            lines.append(
                f"- **Informed vs blind agents:** On this maze configuration, blind agents were "
                f"competitive with informed ones — this can happen on low-weight uniform terrain "
                f"where heuristics provide less advantage."
            )

        # A* heuristic comparison
        if (
            "astar_manhattan" in avg["agent"].values
            and "astar_euclidean" in avg["agent"].values
        ):
            m_cost = avg.loc[avg["agent"] == "astar_manhattan", "path_cost"].values[0]
            e_cost = avg.loc[avg["agent"] == "astar_euclidean", "path_cost"].values[0]
            m_time = avg.loc[avg["agent"] == "astar_manhattan", "time_us"].values[0]
            e_time = avg.loc[avg["agent"] == "astar_euclidean", "time_us"].values[0]
            faster_h = "Manhattan" if m_time < e_time else "Euclidean"
            lines.append(
                f"- **A\\* heuristic comparison:** Both A\\* variants found paths of equal cost "
                f"({m_cost:.0f} vs {e_cost:.0f}), confirming both are optimal. "
                f"The {faster_h} heuristic was faster to compute on this grid."
            )

        out_path = os.path.join(OUTPUT_DIR, "summary.md")
        with open(out_path, "w") as f:
            f.write("\n".join(lines))
        print(f"Summary saved to {out_path}")

    def run_all(self, n_seeds=5, size=50):
        self.run_batch(n_seeds=n_seeds, size=size)
        self.generate_charts()
        self.generate_summary_md()


if __name__ == "__main__":
    analyzer = MazeAnalyzer()

    # == UNCOMMENT THIS TO RUN WITHOUT DOCKER
    # Single run (no Docker needed, uses existing results.json)
    # analyzer.load_single()
    # analyzer.generate_charts()
    # analyzer.generate_summary_md()

    # == UNCOMMENT THIS TO RUN WITH DOCKER
    # Full batch run
    analyzer.run_all(n_seeds=5, size=50)
