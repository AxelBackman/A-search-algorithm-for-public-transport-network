import java.util.*;


public class SpatialHashGrid {
	
	private Graph graph;
    private Map<String, List<Stop>> cells;
    private static final double CELL_SIZE = 0.01; // roughly 1 km²)
    private static final double MAX_DISTANCE = 500; // 500 m radius
    private static final double WALKING_SPEED = 1.00;
    
    public SpatialHashGrid(Graph graph) {
    	this.graph = graph;
    	this.cells = new HashMap<>();
    }
	
	public void addStop(Stop stop) {
        String cellKey = getCellKey(CELL_SIZE, stop);

        if (!cells.containsKey(cellKey)) {
            cells.put(cellKey, new ArrayList<>());
        }

        List<Stop> stopsInCell = cells.get(cellKey);

        for (Stop existingStop : stopsInCell) {
            double distance = calculateDistance(stop, existingStop);
            if (distance <= MAX_DISTANCE) {
                int time = (int) calculateWalkingTime(distance)/60;
                createEdge(stop, existingStop, time);
            }
        }
        stopsInCell.add(stop);
    }
    
    public String getCellKey(double cellSize, Stop stop) {
        int cellLat = (int) (stop.getY() / cellSize);
        int cellLon = (int) (stop.getX() / cellSize);
        return cellLat + "," + cellLon;
    }
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

        return EARTH_RADIUS * c; 
    }
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

    public List<Stop> getStopsInCell(double latitude, double longitude) {
        String cellKey = getCellKeyFromCoordinates(latitude, longitude, CELL_SIZE);
        return cells.getOrDefault(cellKey, new ArrayList<>());
    }

    private String getCellKeyFromCoordinates(double latitude, double longitude, double cellSize) {
        int cellLat = (int) (latitude / cellSize);
        int cellLon = (int) (longitude / cellSize);
        return cellLat + "," + cellLon;
    }
}
