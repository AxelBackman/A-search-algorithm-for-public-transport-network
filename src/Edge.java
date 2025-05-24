import java.util.*;

public class Edge {
	
	private Long tripId;
	private String headSign;
	private Stop from;
	private Stop destination;
	private int weight;
	private int destinationArrivalTime; 
	private int fromDepartureTime;
	private String departureTimeString;
	private static List<Edge> allEdges = new ArrayList<>();
	private int fromStopInOrder; 
	private int toStopInOrder; 
	
	public Edge(
			Long tripId, 
			Stop from, Stop destination, 
			int destinationArrivalTime, 
			int fromDepartureTime, 
			String departureTimeString, 
			String headSign, 
			int fromStopInOrder, 
			int toStopInOrder
			){

		
		this.tripId = tripId;
		this.from = from;
		this.destination = destination;
		this.destinationArrivalTime = destinationArrivalTime;
		this.fromDepartureTime = fromDepartureTime;
		this.weight = destinationArrivalTime - fromDepartureTime;
		this.departureTimeString = departureTimeString;
		this.headSign = headSign;
		allEdges.add(this);
		this.fromStopInOrder = fromStopInOrder;
		this.toStopInOrder = toStopInOrder;
	}
	
	public Edge(
			Long tripId, 
			Stop from, Stop destination, 
			int destinationArrivalTime, 
			int fromDepartureTime, 
			String departureTimeString, 
			String headSign, 
			int fromStopInOrder, 
			int toStopInOrder,
			int weight
			){
		
		this.tripId = tripId;
		this.from = from;
		this.destination = destination;
		this.destinationArrivalTime = destinationArrivalTime;
		this.destinationArrivalTime = fromDepartureTime;
		this.weight = weight;
		this.departureTimeString = departureTimeString;
		this.headSign = headSign;
		allEdges.add(this);
		this.fromStopInOrder = fromStopInOrder; 
		this.toStopInOrder = toStopInOrder;
		
	}
	
	public Long getTripId() { return tripId; }
	public Stop getFrom() { return from; }
	public Stop getDestination() { return destination; }
	public int getWeight() { return weight; }
	public int getDestinationArrivalTime() { return destinationArrivalTime; }
	public int getFromDepartureTime() { return fromDepartureTime; }
	public String getHeadSign() { return headSign; }
	public void setHeadSign(String newHeadSign) { this.headSign = newHeadSign; }
	public void setDepartureTime(int time) { fromDepartureTime = time; }
	public void setArrivalTime(int time) { destinationArrivalTime = time; }
	public String getDepartureTimeString() { return departureTimeString; }
	public int getfromStopInOrder() { return fromStopInOrder; }
	public int getToStopInOrder() { return toStopInOrder; }
	public Trip getTrip(long tripId) { return Trip.getTripById(tripId); }
	
	
	public List<Edge> getEdgeById(long findTripId) {
		List<Edge> edgesFromId = new ArrayList<>();
		
		for (Edge edge : allEdges) {
			if (edge.getTripId().equals(findTripId)) {
				edgesFromId.add(edge);
			}
		}
		return edgesFromId;
	}
	
	@Override
	public String toString() { return "From " + from.getName() + " To " + destination.getName() + " Time: " +  departureTimeString + ", " + " Takes " + weight + " minutes " + tripId; }
}
