import pandas as pd
import matplotlib.pyplot as plt
import subprocess
import json
import os

class MazeAnalyzer:
    def __init__(self, results_path="data/results.json"):
        self.results_path = results_path
        self.raw_data = []

    def run_batch(self, n_seeds=10, size=50):
        """Runs the engine across multiple seeds and collects data."""
        all_results = []
        
        for seed in range(n_seeds):
            print(f"--> Running simulation with seed {seed}...")
            subprocess.run([
                "docker", "compose", "run", "--rm", "engine", 
                "--seed", str(seed), "--size", str(size), "--agents", "all"
            ], check=True)
            
            with open(self.results_path, 'r') as f:
                data = json.load(f)
                for entry in data:
                    entry['seed'] = seed
                all_results.extend(data)
        
        self.raw_data = pd.DataFrame(all_results)
        self.raw_data.to_csv("data/batch_results.csv", index=False)
        print("Done. Raw data exported to data/batch_results.csv")

    def generate_report(self):
        """Generates comparison bar charts and saves a summary PNG."""
        if self.raw_data.empty:
            self.raw_data = pd.read_csv("data/batch_results.csv")

        # Aggregate metrics
        avg_stats = self.raw_data.groupby('agent').mean(numeric_only=True).reset_index()
        metrics = ['time_us', 'nodes_expanded', 'path_cost']
        fig, axes = plt.subplots(1, 3, figsize=(18, 6))

        for i, metric in enumerate(metrics):
            axes[i].bar(avg_stats['agent'], avg_stats[metric], color=['#3498db', '#e74c3c', '#2ecc71', '#f1c40f', '#9b59b6', '#34495e'])
            axes[i].set_title(f'Average {metric.replace("_", " ").title()}')
            axes[i].tick_params(axis='x', rotation=45)

        plt.tight_layout()
        plt.savefig("summary_report.png")
        print("Report saved as summary_report.png")

if __name__ == "__main__":
    analyzer = MazeAnalyzer()
    analyzer.run_batch(n_seeds=5) # Adjust N as needed
    analyzer.generate_report()
