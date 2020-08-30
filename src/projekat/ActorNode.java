package projekat;
import java.util.ArrayList;
import java.util.Collections;

public class ActorNode {
	private String name;
	private Integer movieYear;
	private ArrayList<String> movies = new ArrayList<String>();
	private int distance;
	private ActorNode prev = null;

	public ActorNode(String name) {
		this.name = name;
	}

	public ActorNode(String name,Integer movieYear) {
		this.name=name;
		this.movieYear = movieYear;
	}
	   
	public String getName() {
		return this.name;
	}
	
	public Integer getMovieYear() {
		return this.movieYear;
	}
	
	public ArrayList<String> getMovies(){
	    return this.movies;
	}	
	
	public String toString() {
		return name + ": " + movies.toString();
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public int getDistance() {
		return this.distance;
	}
	
	public void setPrev(ActorNode node) {
		this.prev = node;
	}
	public ActorNode getPrev() {
		return this.prev;
	}
	
	public Integer parseMovie() {
		ArrayList<Integer> moviesArray = new ArrayList<Integer>();
		for(String m :movies) {
			String[] year;
			String[] parsedMovie = m.split("\\s*[()]\\s*");
			year = parsedMovie[1].split(" ");  
			moviesArray.add(Integer.parseInt(year[0]));
		}
		 Collections.sort(moviesArray,Collections.reverseOrder());
		 this.movieYear = moviesArray.get(0); 
		return moviesArray.get(0);
	}
}
