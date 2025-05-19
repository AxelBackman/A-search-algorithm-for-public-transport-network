import java.util.*;
import java.util.Map.Entry;



public class Graph {
	private Map<Stop, List<Edge>> adjList;
	private Map<Integer, Stop> stopsById; 
	private Map<String, Stop> stopsByName; // map för snabbare sökningar på namn
	private Map<Stop, Stop> stops; // första avgången från en nod
	private List<String> stopNames;
	
	public Graph() { 
		this.adjList = new HashMap<>();
		this.stopsById = new HashMap<>();
		this.stopsByName = new HashMap<>();
		this.stops = new HashMap<>();
		this.stopNames = new ArrayList<>();
	}
	
	public List<String> getStopNames() { return stopNames; }
	public Stop getStationById(int id) { return stopsById.get(id); }
    public Collection<Stop> getStations() { return stopsById.values(); }
	public Stop getStationByName(String name) { return stopsByName.get(name); }
	
	public void addStation(Stop stop) { 
		stopsById.put(stop.getId(), stop); 
		stopsByName.put(stop.getName(), stop); 
		stops.put(stop, null);
		stopNames.add(stop.getName());
		Collections.sort(stopNames);
		
	}
	
	public void addStation(Stop stop, Edge edge) {
		adjList.computeIfAbsent(stop, k -> new ArrayList<>()).add(edge); 
	}
	
    public List<Edge> getEdges(Stop stop) {
        return adjList.getOrDefault(stop, new ArrayList<>());
    }
    
    public Map<Stop, List<Edge>> getAdjList(){
    	return adjList;
    	
    }
	

	public void printGraph() {
		int amountOfStations = 0;

		for (Entry<Integer, Stop> entry : stopsById.entrySet()) {
			System.out.println("Station: " + entry.getValue().getName());
			for (Edge edge : entry.getValue().getEdges()) {
				System.out.println(edge);
				amountOfStations++;
			}
		}
		System.out.println("Antalet kanter: " + amountOfStations);
	}
	
	
    
    
    public List<Edge> findShortestPath(Stop start, Stop goal, int currentTime, Map<Stop, List<Edge>> adjList){
    	return AStarPathfinder.findShortestPath(start, goal, currentTime, adjList);
    }
    
}
