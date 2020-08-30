package projekat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Bacon {
	private static class PathToActor implements Comparable<PathToActor> {
		private String fromActor;
		private String toActor;
		private Integer year;
		private Integer distance;
		
		public PathToActor(String fromActor, String toActor, Integer year,Integer distance) {
			this.fromActor = fromActor;
			this.toActor = toActor;
			this.year = year;
			this.distance = distance;
		}
		
		public String getFromActor() {
			return fromActor;
		}
		
		public String getToActor() {
			return toActor;
		}
		
		public Integer getYear() {
			return year;
		}
		public Integer getDistance() {
			return distance;
		}
		public int compareTo(PathToActor o) {
			return year.compareTo(o.year);
		}	
		
		public String getMovie(String fromActor, String toActor) {
			ArrayList<HashMap<Integer, String>> moviesArray = new ArrayList<HashMap<Integer, String>>();
			List<ActorNode> list = graph.get(fromActor);
			int newestMovie = Integer.MAX_VALUE;
			for(ActorNode node: list) {
				if (node.getName().equals(toActor)) {
					for(String m :node.getMovies()) {
						String[] year;
						String[] parsedMovie = m.split("\\s*[()]\\s*");
						year = parsedMovie[1].split(" "); 
						int intYear = Integer.parseInt(year[0]);
						HashMap<Integer, String> kvMovie = new HashMap<Integer, String>();
						if(intYear< newestMovie) newestMovie = intYear;
						kvMovie.put(intYear, m);
						moviesArray.add(kvMovie);
					}
					 Collections.sort(moviesArray,Collections.reverseOrder());
					return moviesArray.get(0).get(newestMovie);
				}
			}
			return null;
		}
	}
	
	private static class PathToActorNode implements Comparable<PathToActor> {
		private ActorNode fromActor;
		private ActorNode toActor;
		private Integer year;
		
		public PathToActorNode(ActorNode fromActor, ActorNode toActor, Integer year) {
			this.fromActor = fromActor;
			this.toActor = toActor;
			this.year = year;
		}
		
		public ActorNode getFromActor() {
			return fromActor;
		}
		
		public ActorNode getToActor() {
			return toActor;
		}
		
		public String getFromActorName() {
			return fromActor.getName();
		}
		
		public String getToActorName() {
			return toActor.getName();
		}
		
		public Integer getYear() {
			return year;
		}
		
		public int compareTo(PathToActor o) {
			return year.compareTo(o.year);
		}	
	}
	
	private static Map<String, List<ActorNode>> graph;
	Set<String> actorSet;
	private ActorNode actorNode;
	private LinkedList<String> path;
	public Bacon(String fileName) {
		graph = new HashMap<String, List<ActorNode>>();
		actorSet = new HashSet<String>();
		try {
			makeGraph(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// metod koji kreira graf, na osnovu fajla movies.txt
	private void makeGraph(String fileName) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String nextLine;
		String movie;
		String[] year;
		nextLine = in.readLine();
		while(nextLine != null) {
			String[] parsed = nextLine.split("/");
			String[] parsedMovie = parsed[0].split("\\s*[()]\\s*");
			year = parsedMovie[1].split(" ");  
			movie = parsed[0];
			for(int i=1; i<parsed.length; i++) {
				actorSet.add(parsed[i]);
			}
			addActors(movie);
			actorSet.clear();
			nextLine = in.readLine();
		}
		in.close();
	}
		
	private void addActors(String movie) {		
		for(String key: actorSet) {
			String mainKey = key;

			if(graph.get(mainKey)!=null) {	
				for(String left: actorSet) {
					if(!left.equals(mainKey)) {
						Boolean contains = false;
						List<ActorNode> list = graph.get(mainKey);
						if(!list.isEmpty()){
							for(ActorNode a: list) {
								if(a.getName().equals(left)) {
									a.getMovies().add(movie);
									contains = true;
									break;
								} 
							}
							if(!contains) {
								ActorNode newNode = new ActorNode(left);
								newNode.getMovies().add(movie);
								graph.get(mainKey).add(newNode);
							}
						}
					}
				}
			}
			if(graph.get(mainKey) == null) {
				List<ActorNode> newList = new ArrayList<ActorNode>();
				for(String left: actorSet){
					if(!left.equals(mainKey)) {
						ActorNode node = new ActorNode(left);
						node.getMovies().add(movie);
						newList.add(node);
					}
				}
				graph.put(mainKey, newList);
			}
		}
	}
	
	public String getMovie(String fromActor, String toActor) {
		ArrayList<HashMap<Integer, String>> moviesArray = new ArrayList<HashMap<Integer, String>>();
		List<ActorNode> list = graph.get(fromActor);
		int newestMovie = Integer.MAX_VALUE;
		for(ActorNode node: list) {
			if (node.getName().equals(toActor)) {
				for(String m :node.getMovies()) {
					String[] year;
					String[] parsedMovie = m.split("\\s*[()]\\s*");
					year = parsedMovie[1].split(" "); 
					int intYear = Integer.parseInt(year[0]);
					HashMap<Integer, String> kvMovie = new HashMap<Integer, String>();
					if(intYear< newestMovie) newestMovie = intYear;
					kvMovie.put(intYear, m);
					moviesArray.add(kvMovie);
				}
				 Collections.sort(moviesArray,Collections.reverseOrder());
				return moviesArray.get(0).get(newestMovie);
			}
		}
		return null;
	}
		
	public ArrayList<String> findBaconPath(String startActor) throws IllegalArgumentException {
		path = new LinkedList<String>();
		if(startActor.equals("Bacon, Kevin")) {
			return null;
		}
		if(!graph.containsKey(startActor) || startActor == null) {
			throw new IllegalArgumentException();
		}
		HashMap<String, String> actorsMapping = new HashMap<String, String>();
		PriorityQueue<PathToActor> queue = new PriorityQueue<PathToActor>();
		queue.add(new PathToActor(null, startActor, 0,0));
		while (!queue.isEmpty()) {
			PathToActor currentActorPath = queue.poll();
			String currentActorName = currentActorPath.getToActor();
			String fromActorName = currentActorPath.getFromActor();
			Integer currentActorYear = currentActorPath.getYear();
			Integer currentActorDistance = currentActorPath.getDistance();
			if (actorsMapping.containsKey(currentActorName))
				continue;
			actorsMapping.put(currentActorName, fromActorName);
			if (currentActorName.equals("Bacon, Kevin")) {
				System.out.println("Dužina puta: "+currentActorDistance); 
				System.out.println("Težina puta: "+currentActorYear); 
				break;
				}
			for(ActorNode neighboringPaths: graph.get(currentActorName)) {
				String neighboringActorName =  neighboringPaths.getName();
				Integer neighboringActorYear = neighboringPaths.parseMovie();
				PathToActor pathToNeighboringActor = new PathToActor(currentActorName, neighboringActorName, currentActorYear+neighboringActorYear,currentActorDistance+1);
				queue.add(pathToNeighboringActor);
			}
		}
		String currentActor = "Bacon, Kevin";
		ArrayList<String> movieList = new ArrayList<String>();
		ArrayList<String> finalPath = new ArrayList<String>();
		do {
			path.addFirst(currentActor);
			movieList.add(getMovie(currentActor,actorsMapping.get(currentActor)));
		} while ((currentActor = actorsMapping.get(currentActor)) != null);
		Collections.reverse(movieList);
		movieList.remove(0);
		System.out.println("Lista filmova: "+movieList);
		System.out.println("Lista glumaca: "+path);
		System.out.println("Putanja od unetog glumca: ");
		for(int i=0; i<path.size(); i++) {
			finalPath.add(path.get(i));
			if(movieList.size()>i)
				finalPath.add(movieList.get(i));
		}
		return finalPath;
	}
}
