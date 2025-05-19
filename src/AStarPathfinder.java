import java.util.*;

public class AStarPathfinder {
	
	private static final int MIN_TRANSFER_TIME = 3;
	
	/**
	 * Implements the A* search algorithm to find the shortest path in SL public transport network. 
	 * The algorithm uses Euclidian distance to determine the heuristic.
	 *
	 * @param start       The trip's starting station.
	 * @param goal        The trip's goal station.
	 * @param currentTime The current time in minutes past midnight.
	 * @param adjList     The adjacency list representing the transport network.
	 * @return A list of edges representing the shortest path from start to goal at a certain start time whilst adhering to a time schedule, or null if no path is found.
	 */
	
	public static List<Edge> findShortestPath(Stop start, Stop goal, int currentTime, Map<Stop, List<Edge>> adjList) {
	    Map<Stop, Integer> bestTime = new HashMap<>();
	    Map<Stop, Edge> cameFrom = new HashMap<>();
	    Map<Stop, Double> gScoreMap = new HashMap<>();
	    Map<Stop, Double> fScoreMap = new HashMap<>();
	    
	    PriorityQueue<Stop> openSet = new PriorityQueue<>(Comparator.comparingDouble(fScoreMap::get));
	    
	    bestTime.put(start, currentTime);
	    openSet.add(start);
	    gScoreMap.put(start, 0.0);
	    fScoreMap.put(start, calculateHeuristic(start, goal));

	    while (!openSet.isEmpty()) {
	        Stop currentStop = openSet.poll();

	        if (currentStop.equals(goal)) {
	            return reconstructPath(cameFrom, goal);
	        }
	        
	        int currentArrivalTime = bestTime.get(currentStop);

	        for (Edge edge : adjList.getOrDefault(currentStop, Collections.emptyList())) {
	            Stop neighbor = edge.getDestination();
	            Edge previousEdge = cameFrom.get(currentStop);
	            
	            if (edge.getHeadSign().equals("Walk")) {
	                if (previousEdge != null) {
	                    int previousEdgeArrivalTime = previousEdge.getDestinationArrivalTime();
	                    int walkDepartureTime = Math.max(currentArrivalTime, previousEdgeArrivalTime);
	                    edge.setDepartureTime(walkDepartureTime);
	                    edge.setArrivalTime(walkDepartureTime + edge.getWeight());
	                }
	            }
	            
	            boolean isSameLine = (previousEdge != null && previousEdge.getTripId().equals(edge.getTripId()));
	            
	            if (isSameLine || edge.getFromDepartureTime() >= currentArrivalTime + MIN_TRANSFER_TIME) { 
	                int newTime = edge.getDestinationArrivalTime();

	                if (bestTime.containsKey(neighbor) && newTime >= bestTime.get(neighbor)) {
	                    continue;
	                }

	                bestTime.put(neighbor, newTime);
	                cameFrom.put(neighbor, edge);
	                
	                double gScore = newTime - currentTime;
	                double fScore = gScore + calculateHeuristic(neighbor, goal);

	                gScoreMap.put(neighbor, gScore);
	                fScoreMap.put(neighbor, fScore);

	                openSet.add(neighbor);
	            }
	        }
	    }
	    return null;
	}


    /**
     * Reconstructs the shortest path from the cameFrom map.
     * 
     * @param cameFrom		has keys of stops and values of the edge taken there
     * @param goal			The trip's goal station.
     * @return A list of edges representing the shortest path from start to goal.
     */
    private static List<Edge> reconstructPath(Map<Stop, Edge> cameFrom, Stop goal) {
        List<Edge> path = new ArrayList<>();
        Stop current = goal;
        
        while (cameFrom.containsKey(current)) {
            Edge edge = cameFrom.get(current);
            path.add(edge);
            current = edge.getFrom(); 
        }
        
        Collections.reverse(path);
        return path;
    }
    
    /**
     * Calculates the Euclidean distance between two stops, used as the heuristic in A*.
     * 
     * @param 	from, stop1, its coordinates is from where we start calculating distance.
     * @param 	to, stop2, its coordinates is from where we end the calculation of distance.
     * @return the Euclidean distance.
     */
    
    
    private static double calculateHeuristic(Stop from, Stop to) {
        double dx = from.getX() - to.getX();
        double dy = from.getY() - to.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    

    


    
}
