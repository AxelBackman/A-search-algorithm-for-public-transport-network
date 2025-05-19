import java.util.*;


public class SpatialHashGrid {
	
	private Graph graph;
    private Map<String, List<Stop>> cells;
    private static final double CELL_SIZE = 0.01; // Cellstorlek i grader (ungefär 1 km²)
    private static final double MAX_DISTANCE = 500; // 500 meter radie
    private static final double WALKING_SPEED = 1.00; // Gånghastighet i meter per sekund - average är egentligen 1.42 men de är för generöst.
    
    public SpatialHashGrid(Graph graph) {
    	this.graph = graph;
    	this.cells = new HashMap<>();
    }
    
    

    // Lägg till stationen och skapa kanter om den är nära andra stationer i samma cell
    public void addStop(Stop stop) {
        String cellKey = getCellKey(CELL_SIZE, stop);

        // Kontrollera om cellen redan finns
        if (!cells.containsKey(cellKey)) {
            cells.put(cellKey, new ArrayList<>());
        }

        // Hämta alla stationer i samma cell
        List<Stop> stopsInCell = cells.get(cellKey);

        // Skapa kanter mellan stationen och alla andra stationer inom radien
        for (Stop existingStop : stopsInCell) {
            double distance = calculateDistance(stop, existingStop);
            if (distance <= MAX_DISTANCE) {
                // Beräkna gångtiden mellan stationerna
                int time = (int) calculateWalkingTime(distance)/60;
                // Skapa en kant mellan stationerna
                createEdge(stop, existingStop, time);
            }
        }

        // Lägg till stationen till cellen
        stopsInCell.add(stop);
    }
    
    public String getCellKey(double cellSize, Stop stop) {
        int cellLat = (int) (stop.getY() / cellSize);
        int cellLon = (int) (stop.getX() / cellSize);
        return cellLat + "," + cellLon;
    }

    // Beräkna avståndet mellan två Stop i meter
    public double calculateDistance(Stop stop1, Stop stop2) {
        final double EARTH_RADIUS = 6371000; // radie i meter
        double lat1 = Math.toRadians(stop1.getY());
        double lon1 = Math.toRadians(stop1.getX());
        double lat2 = Math.toRadians(stop2.getY());
        double lon2 = Math.toRadians(stop2.getX());

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // Avstånd i meter
    }

    // Beräkna gångtiden mellan två stationer baserat på avståndet
    public double calculateWalkingTime(double distance) {
        return distance / WALKING_SPEED; // Tid i sekunder
    }
    

    private void createEdge(Stop a, Stop b, int time) {
    	Edge aToBEdge = new Edge(
    			(long) 1,
    			a,
    			b,
    			0,
    			0,
    			"",
    			"Walk",
    			0,
    			0,
    			time
    			);
//    	System.out.println("Skapade gångkant: " + a + " -> " + b + " med TripId: " + aToBEdge.getTripId() + " tar tid: " + time/60);
    	
    	Edge bToAEdge = new Edge(
    			(long) 1,
    			b,
    			a,
    			0,
    			0,
    			"",
    			"Walk",
    			0,
    			0,
    			time
    			);

    	
    	graph.addStation(a, aToBEdge);
    	
    	graph.addStation(b, bToAEdge);
    	
    }


    // För att hämta alla stationer inom en viss cell
    public List<Stop> getStopsInCell(double latitude, double longitude) {
        String cellKey = getCellKeyFromCoordinates(latitude, longitude, CELL_SIZE);
        return cells.getOrDefault(cellKey, new ArrayList<>());
    }

    // Direkt metod för att beräkna cellnyckel från koordinater utan att skapa en Stop
    private String getCellKeyFromCoordinates(double latitude, double longitude, double cellSize) {
        int cellLat = (int) (latitude / cellSize);
        int cellLon = (int) (longitude / cellSize);
        return cellLat + "," + cellLon;
    }
}