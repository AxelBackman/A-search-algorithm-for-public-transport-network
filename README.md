# A* search algorithm for public transport network

Resources contains 1 days data from SL, Stockholm, Sweden's public transport network.

Src contains code to read the files, create Stop objects that represent nodes and edges between the stops.

AStarPathfinder finds the fastest path between 2 stops, and prioritizes staying on the same line rather than changing.

The program uses a makeshift SpatialHashGrid. This enables the creation of "walking" edges between stops that are close. The algorithm can therefor consider walking between 2 stops if another stop has a better departure.
