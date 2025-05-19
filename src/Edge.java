import java.util.*;

public class Edge {
	
	/* Denna klassen Representerar åkvägen mellan stationer
	 * En busslinje med 6 stopp har därmed 6 kanter där varje anges som type buss, och time anges från sl_stop_times.txt
	 * 
	 * Framtida implementeringar: datum för att hålla koll på olika dagar
	 * skapa kanter mellan stationer inom ett visst område då man kan gå. 
	 */
	
	private Long tripId;
	private String headSign;
	private Stop from;
	private Stop destination;
	private int weight;
	private int destinationArrivalTime; // tid när man anläner på Noden to
	private int fromDepartureTime; // tid när man åker från noden from
	private String departureTimeString;
	private static List<Edge> allEdges = new ArrayList<>();
	private int fromStopInOrder; // vilket nr av stopp på linjen from noden är
	private int toStopInOrder; // vilket nr av stopp på linjen to noden är
	
	
	
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
		this.fromStopInOrder = fromStopInOrder; //överflödiga? kan bara vara i Trip
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
		this.fromStopInOrder = fromStopInOrder; //överflödiga? kan bara vara i Trip
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
	public String toString() { return "Från " + from.getName() + " Till " + destination.getName() + " kl: " +  departureTimeString + ", " + " Tar " + weight + " minuter " + tripId; }
}
