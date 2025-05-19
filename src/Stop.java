import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/*
 * class represents stop / station
 * 
 */

public class Stop {
	
	private int id;
	private String name;
	private double y;
	private double x;
	private List<Edge> edges;
	
	public Stop(int id, String name, double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.y = latitude;
		this.x = longitude;
		this.edges = new ArrayList<>();
	}
	
	public int getId() { return id; }
	public String getName() { return name; }
	public double getY() { return y; }
	public double getX() { return x; }
	public List<Edge> getEdges() { return edges; }
	public void addEdge(Edge edge) { edges.add(edge); }

    @Override
    public boolean equals(Object o) {
    	if (this == o) return true;
    	if (o == null || getClass() != o.getClass()) return false;
    	Stop stop = (Stop) o;
    	return id == stop.id &&
    			Objects.equals(name, stop.name) &&
    			Double.compare(y, stop.x) == 0 &&
    			Double.compare(y, stop.x) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, y, x);
    }
    
    @Override
	public String toString() { return name; }
   


	


	
    
}
