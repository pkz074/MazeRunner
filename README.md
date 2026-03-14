# MazeRunner

AI pathfinding benchmark — multiple search algorithms compete on procedurally generated weighted mazes.

![Go](https://img.shields.io/badge/engine-Go-00ADD8?style=flat-square)
![Python](https://img.shields.io/badge/visualizer-Python-3776AB?style=flat-square)
![Course](https://img.shields.io/badge/AI_Course-Project-7C3AED?style=flat-square)

## What it does

Generate a maze from a seed, select which algorithm agents to run, and watch them all search simultaneously. When finished, compare their performance across time, nodes expanded, and path cost — then export the results for analysis.

Each agent is a different color. Explored cells light up as the algorithm expands them. A leaderboard updates as each agent reaches the goal.

## Agents

| Agent | Type | Heuristic |
|---|---|---|
| A\* (Manhattan) | Informed | Manhattan distance |
| A\* (Euclidean) | Informed | Euclidean distance |
| Greedy Best-First | Informed | Manhattan distance |
| UCS / Dijkstra | Optimal | None |
| BFS | Blind | None |
| DFS | Blind | None |


## on development
