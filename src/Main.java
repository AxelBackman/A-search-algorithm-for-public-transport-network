
import java.util.*;

public class Main {


    public static void main(String[] args) {
    	
    	
    	Graph graph = new Graph();
    	SpatialHashGrid spatialHashGrid = new SpatialHashGrid(graph);
    	FileReader fileReader = new FileReader(spatialHashGrid, graph);
    	
    	//ÄNDRA FILSÖKVÄG OM DU VILL LADDA IN DATA
    	fileReader.createGraphFromFile("resources/sl_stops.txt", "resources/sl_stop_times.txt", "resources/sl_trips.txt", graph);
    	
    	
    	// ÄNDRA NEDANSTÅENDE FÖR KÖRNINGAR. TIME = MINUTER EFTER MIDNATT, 600 = 10:00
    	
    	String from = "Abrahamsberg T-bana";
    	String to = "Gubbängen T-bana";
    	int time = 600;
    	
        List<Edge> bestPath = graph.findShortestPath(graph.getStationByName(from), graph.getStationByName(to), time, graph.getAdjList());
        
        List<Trip> shortestPath = processEdgesToPaths(bestPath);
        
        printPath(shortestPath); 

    }


    public static void printPath(List<Trip> fullPath) {
    	fullPath.sort(Comparator.comparingInt(Trip::getDepartureTime));
    	
    	int time = 0;
    	int departureTime = 0;
    	int arrivalTime = 0;

    	for (Trip trip : fullPath) {

            System.out.println(
            		"Åk "
            		+ trip.getAmountOfStops()
            		+ " Stationer [" 
            		+ trip.getFrom()
            		+ " ---> "
            		+ trip.getTo()
            		+ "] Avgång "
            		+ trip.getDepartureTimeString()
            		+ " | Linje: "
            		+ trip.getTripId()
            		+ " | Mot: "
            		+ trip.getHeadSign()
            		+ " | Tar  "
            		+ trip.getTime()
            		+ " min "
            		);
            
            if (departureTime == 0) { 
                departureTime = trip.getDepartureTime();
            }
    		arrivalTime = trip.getArrivalTime();
    	}
    	
    	time = arrivalTime - departureTime;
    	
    	System.out.println("Resan tar totalt " + time + " minuter");

    }


    // Lånemetod för att konvertera tid till minuter
    public static int timeFormat(String time) {
        int timeDifference = 0;
        String[] parts = time.split(":");

        int hours = Integer.parseInt(parts[0].trim());
        int minutes = Integer.parseInt(parts[1].trim());

        timeDifference += hours * 60;
        timeDifference += minutes;

        return timeDifference;
    }

    public static List<Trip> processEdgesToPaths(List<Edge> edges) {
		// edges.sort(Comparator.comparingInt(Edge::getToStopInOrder)); - om vi vill i framtiden sortera stoppen i rätt ordning för UI
		
		List<Trip> allTrips = new ArrayList<>();
	    Map<Long, List<Edge>> trips = new HashMap<>(); // k = triId, value = kanterna som tillhör trippen.
	    Map<Long, Integer> timeSpent = new HashMap<>(); // map för att logga vikten av varje edge
	    
	    
	    for (Edge edge : edges) {
	    	trips.computeIfAbsent(edge.getTripId(), k -> new ArrayList<>()).add(edge); // lägg till alla kanter som tillhör samma tripId
	    	timeSpent.put(edge.getTripId(), timeSpent.getOrDefault(edge.getTripId(), 0) + edge.getWeight()); //lägg till tripd id , += vikten
	    }
	    
	    for(Map.Entry<Long, List<Edge>> entry : trips.entrySet()) {
	    	long key = entry.getKey();
	    	List<Edge> tripEdges = entry.getValue();
	    	
	    	Trip trip = Trip.getTripById(key);
	    	
	    	Edge firstEdge = tripEdges.get(0);
	    	Edge lastEdge = tripEdges.get(tripEdges.size()-1);
	    	
	    	int amountOfStops = (lastEdge.getToStopInOrder() - firstEdge.getToStopInOrder()+1); // +1 för att vi saknar getFromStopInOrder så den räknar först från andra stationen.
	    	String departure = firstEdge.getDepartureTimeString();
	    	int time = timeSpent.get(key);
	    	long tripId = key;
	    	Stop from = firstEdge.getFrom();
	    	Stop to = lastEdge.getDestination();
	    	List<Edge> e = tripEdges;
	    	String headSign = trip.getHeadSign();
	    	int departureTime = firstEdge.getFromDepartureTime();
	    	int arrivalTime = lastEdge.getDestinationArrivalTime();
	    	
	    	Trip fullTrip = new Trip(
	    			amountOfStops,
	    			departure,
	    			time,
	    			tripId,
	    			from,
	    			to,
	    			e,
	    			headSign,
	    			departureTime,
	    			arrivalTime
	    			);
	    	
	    	allTrips.add(fullTrip);
	
	    }
	    return allTrips;
	}
}
