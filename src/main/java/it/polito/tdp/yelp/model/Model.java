package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;


import it.polito.tdp.yelp.db.YelpDao;



public class Model {
	
	private SimpleWeightedGraph<User, DefaultWeightedEdge> grafo;
	private YelpDao dao;
	private Map<String,User> idMap;

	
	public Model() {
		dao = new YelpDao();
		idMap = new HashMap<String,User>();
		dao.getAllUsers(idMap);
	}
	
	public void creaGrafo(int x, int anno) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungo vertici "filtrati"
		Graphs.addAllVertices(grafo, dao.getVertici(x, idMap));
		
		//aggiungo archi
		for(Adiacenza a: dao.getAdiacenze(x,anno,idMap)) {
			try {
			Graphs.addEdgeWithVertices(grafo, a.getU1(), a.getU2(), a.getPeso());
			}catch (Throwable t) {
	            // Ignora eventuali errori durante l'aggiunta degli archi
	        }
		}
		
		System.out.println("VERTICI: " + this.grafo.vertexSet().size());
		System.out.println("ARCHI: " + this.grafo.edgeSet().size());
	}
	
	public List<User> getUtenteSimile(User utente){
	
	List<User> percorso = new ArrayList<>();
	DefaultWeightedEdge arcoMaxPeso = null;
	double pesoMax = Double.NEGATIVE_INFINITY;
	List<User> similiMaxPeso = new ArrayList<>();
	
	BreadthFirstIterator<User,DefaultWeightedEdge> it =
			new BreadthFirstIterator<>(this.grafo,utente);
	
	while(it.hasNext()) {
		User simile = it.next();
		DefaultWeightedEdge e = grafo.getEdge(utente, simile);
	
		
		 if (e != null) {
		        double peso = grafo.getEdgeWeight(e);
		        if (peso > pesoMax) {
		            pesoMax = peso;
		            arcoMaxPeso = e;
		            similiMaxPeso.clear(); // Rimuovi gli User precedenti con peso inferiore
		            similiMaxPeso.add(simile);
		        } else if (peso == pesoMax) {
		            similiMaxPeso.add(simile);
		        }
		    }
		
	}
	
	return similiMaxPeso;
	
	
	
	}

	public Set<User> getVertici() {
		if(grafo != null)
			return grafo.vertexSet();
		
		return null;
	}

	public int getNVertici() {
		if(grafo != null)
			return grafo.vertexSet().size();
		
		return 0;
	}
	
	public int getNArchi() {
		if(grafo != null)
			return grafo.edgeSet().size();
		
		return 0;
	}
	
	
}
