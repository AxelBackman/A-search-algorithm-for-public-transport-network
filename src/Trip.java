import java.util.*;

public class Trip {
	
	private int time;
	private long tripId;
	private Stop from;
	private Stop to;
	private List<Edge> edges;
	private Map<Integer, Edge> edgeMap;
	private String departureTime;
	private int amountOfStops;
	private String headSign;
	private static Map<Long, Trip> allTrips = new HashMap<>();
	private HashSet<Stop> stops;
	private int firstDeparture;
	private int lastArrival;
	
	public Trip(
			int amountOfStops,
			String departureTime,
			int time,
			long tripId, 
			Stop from, 
			Stop to, 
			List<Edge> edges,
			String headSign, 
			int firstDeparture, 
			int lastArrival
			) {
		
		this.amountOfStops = amountOfStops;
		this.departureTime = departureTime;
		this.time = time;
		this.tripId = tripId;
		this.from = from;
		this.to = to;
		this.edges = edges;
		this.headSign = headSign;
		this.stops = fillStops();
		this.firstDeparture = firstDeparture;
		this.lastArrival = lastArrival;
		this.edgeMap = fillStopsMap();
		
		allTrips.put(tripId, this);
		
	}
	
	public int getTime()  { return time; }
	public long getTripId() { return tripId; }
	public String getFrom() { return from.getName(); }
	public String getTo() { return to.getName(); }
	public List<Edge> getEdges() { return edges; }
	public void setFrom(Stop newFrom) { this.from = newFrom; }
	public void setTo(Stop newTo) { this.to = newTo; }
	public void addEdge(Edge newEdge) { this.edges.add(newEdge); }
	public String getDepartureTimeString() { return departureTime; }
	public int getAmountOfStops() { return amountOfStops; }
	public void setHeadSign(String headSign) { this.headSign = headSign; }
	public String getHeadSign() { return headSign; }
	public static Trip getTripById(long tripId) { return allTrips.get(tripId); }
	public static Map<Long, Trip> getAllTrips( ){ return allTrips; }
	public int getDepartureTime()  { return firstDeparture; }
	public int getArrivalTime() { return lastArrival; }
	public HashSet<Stop> getStops() { return stops; }
	public Map<Integer, Edge> getEdgeMap() { return edgeMap; }
	
	
	
	public Map<Integer, Edge> fillStopsMap() { 
		Map<Integer, Edge> newMap = new HashMap<>();
		for (Edge edge : this.edges) {
			newMap.put(edge.getfromStopInOrder(), edge);
		}
		return newMap;
	}
	
	private HashSet<Stop> fillStops() {
        HashSet<Stop> stops = new HashSet<>();

        if (edges.isEmpty()) {
        	System.out.println("No stops");
            return stops; 
        }

        for (Edge edge : this.getEdges()) {
            stops.add(edge.getFrom());
            stops.add(edge.getDestination());
        }
        return stops;
    }
	
}
