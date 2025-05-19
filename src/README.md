/src contains all java files

/Resources contains data

1. Filereader reads Resources row by row and creates objects:
   - Stop (represents a stop / station)
   - Edge (represents the path from one station to another)
   - Trip (represents a full trip from station a to e (including the edges between a-b, b-c, so forth)
2. AStarPathFinder containing the search algorithm can be used on the graph to locate the fastest path between to stations
   - Fastest, not shortest
   - prioritizes staying on the same Trip / line rather than changing often
   - 
