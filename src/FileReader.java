import java.io.*;
import java.util.*;

public class FileReader {
	
	private Map<Long, String> headSigns;
	private SpatialHashGrid spatialHashGrid;
	private Graph graph;
	
	public Graph createGraphFromFile(String stopsFile, String routesFile, String tripsFile, Graph graph) {
		
		List<Stop> allStations = createStations(stopsFile, graph);
		createHeadSigns(tripsFile, graph);
		createRoutes(routesFile, graph, spatialHashGrid, allStations);

		return graph;
	}
	
	public FileReader(SpatialHashGrid spatialHashGrid, Graph graph) {
		this.spatialHashGrid = spatialHashGrid;
		this.headSigns = new HashMap<>();
		this.graph = graph;
	}
	
	public List<Stop> createStations(String fileName, Graph graph) {
		try {
			File file = new File(fileName);
			
			if (!file.exists()) {
                throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
            }
			
        	BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(file));
        	bufferedReader.readLine();
            
            String line;
            List<Stop> allStations = new ArrayList<>();
            
            while ((line = bufferedReader.readLine()) != null) {
            	String [] parts = line.split(",");

            	int id = Integer.parseInt(parts[0].trim());
            	String name = parts[1].trim();
            	double latitude = Double.parseDouble(parts[2].trim());
            	double longitude = Double.parseDouble(parts[3].trim());
            		
            	Stop stop = new Stop(id, name, latitude, longitude);
            	
            	graph.addStation(stop);
            	allStations.add(stop);
            	
            	spatialHashGrid.addStop(stop); 
            	
            }
            bufferedReader.close();
            return allStations;
            
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An IO-error occured: " + e.getMessage());
            e.printStackTrace();
        }
		return null;
	}
	     
	public void createHeadSigns(String fileName, Graph graph) {
		try {
			File file = new File(fileName);
		
			if (!file.exists()) {
				throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
			}
			
			BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(file));
			bufferedReader.readLine();
        
			String line = "";
        
        
			while ((line = bufferedReader.readLine()) != null) {
				String [] parts = line.split(",");
				
                if (parts.length != 5) {
                    System.out.println("Inccorect line: " + line);
                    continue;
                }
				Long tripId = Long.parseLong(parts[2].trim());
				String headSign = parts[3].trim();
				
				headSigns.put(tripId, headSign);
				
			}
			bufferedReader.close();
        	
		} catch (IOException e) { 
			System.out.println("Error reading headSigns");
		}
	}
	
	
	
	public void createRoutes (String fileName, Graph graph, SpatialHashGrid spatialGrid, List<Stop> allStations) {
    	try {
			File file = new File(fileName);
			
			if (!file.exists()) {
                throw new FileNotFoundException("Filen hittades inte: " + file.getAbsolutePath());
			}
			BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(file));
            bufferedReader.readLine(); 
            
            String previousStop = "";
            String currentStop = "";
            List<Edge> trip = new ArrayList<>();

            while ((currentStop = bufferedReader.readLine()) != null) {
            	String [] currentTripParts = currentStop.split(",");
            	int isLastStation = Integer.parseInt(currentTripParts[5].trim()); 
            	int isFirstStation = Integer.parseInt(currentTripParts[6].trim()); 
            	
            	if (isFirstStation == 1) { 
            		previousStop = currentStop;
            		
            	}
            	
            	if (isLastStation == 1) { 
            		Edge lastEdge = createEdge(previousStop, currentStop, graph); 
            		trip.add(lastEdge);

            		createTrip(currentStop, trip, lastEdge);
            		
            		trip.clear();
            	}
            	
            	else if (isFirstStation == 0 && isLastStation == 0) { 
            		Edge newEdge = createEdge(previousStop, currentStop, graph);
            		trip.add(newEdge);
            		previousStop = currentStop;
            	}
            	
            }

            bufferedReader.close();
            
		} catch (IOException e) { 
			System.out.println("Error reading");
		}
    	
    }
	
	public Edge createEdge(String previousStop, String currentStop, Graph graph) {

		String [] previousStopInfo = previousStop.split(",");
		
		long tripId = Long.parseLong(previousStopInfo[0].trim());
		int previousDepartureTime = timeFormat(previousStopInfo[2].trim());
		String previousDepartureTimeString = previousStopInfo[2].trim();
		Stop previousStation = graph.getStationById(Integer.parseInt(previousStopInfo[3].trim()));
		int previousStopInOrder = Integer.parseInt(previousStopInfo[4].trim());
		

		String [] currentStopInfo = currentStop.split(",");
		
		int currentArrivalTime = timeFormat(currentStopInfo[1].trim());
		String currentDepartureTimeString = currentStopInfo[2].trim();
		Stop currentStation = graph.getStationById(Integer.parseInt(currentStopInfo[3].trim()));
		int currentStopInOrder = Integer.parseInt(currentStopInfo[4].trim());
		Edge newEdge = new Edge(
				tripId, 
				previousStation,
				currentStation,
				currentArrivalTime,
				previousDepartureTime,
				previousDepartureTimeString,
				headSigns.get(tripId),
				previousStopInOrder,
				currentStopInOrder
				);
		
		previousStation.addEdge(newEdge);
		graph.addStation(previousStation, newEdge); 
		
		return newEdge;
		
	}
	
	public void createTrip(String currentStop, List<Edge> trip, Edge lastEdge) {
		String [] lastStopInfo = currentStop.split(",");
		
		int amountOfStops = Integer.parseInt(lastStopInfo[4].trim());
		String departureTimeString = trip.get(0).getDepartureTimeString();
		long tripId = Long.parseLong(lastStopInfo[0].trim());
		Stop firstStation = trip.get(0).getFrom();
		Stop lastStation = lastEdge.getDestination();
		String headSign = headSigns.get(tripId);
		int departureTime = trip.get(0).getFromDepartureTime();
		int arrivalTime = lastEdge.getDestinationArrivalTime();
		int timeSpent = arrivalTime-departureTime;
		
		Trip newTrip = new Trip(
				amountOfStops,
				departureTimeString,
				timeSpent,
				tripId,
				firstStation,
				lastStation,
				trip,
				headSign,
				departureTime,
				arrivalTime
				);
	}

    public int timeFormat (String time){
    	int timeDifference = 0;
    	String [] parts = time.split(":");
    	
    	int hours = Integer.parseInt(parts[0].trim());
    	int minutes = Integer.parseInt(parts[1].trim());
    	int seconds = Integer.parseInt(parts[2].trim());
    	
    	timeDifference += hours * 60;
    	timeDifference += minutes;
    	timeDifference += seconds / 60;
    		
    	return timeDifference;
    }
}
