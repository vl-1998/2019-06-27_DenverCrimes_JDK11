package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDAO;

public class Model {
	private EventsDAO dao;
	private Graph <String, DefaultWeightedEdge> grafo;
	private List<String> bestPercorso;
	private Integer pesoMassimo;
	
	public Model() {
		this.dao = new EventsDAO ();
	}
	public List<String> getOffense(){
		List <String> result = new ArrayList<>(this.dao.getOffense());
		Collections.sort(result);
		return result;
	}
	public List<Integer> getDay(){
		return this.dao.getDay();
	}
	
	public void creaGrafo(String categoria, Integer giorno) {
		this.grafo = new SimpleWeightedGraph <>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getVertex(categoria, giorno));
		
		for (Arco a : dao.getArchi(categoria, giorno)) {
			if (this.grafo.vertexSet().contains(a.getCategoria1()) && this.grafo.vertexSet().contains(a.getCategoria2())) {
				if (a.getPeso()!=0) {
					Graphs.addEdgeWithVertices(this.grafo, a.getCategoria1(), a.getCategoria2(), a.getPeso());
				}
			}
		}
	}
	
	public List<String> getVertex(){
		List<String> result = new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(result);
		return result;
	}
	public List<DefaultWeightedEdge> getArchi(){
		List<DefaultWeightedEdge> result = new ArrayList<>(this.grafo.edgeSet());
		return result;
	}
	
	public List<Arco> getPesoInferiore(String categoria, Integer giorno){
		List<Arco> archi = new ArrayList<>(this.dao.getArchi(categoria, giorno));
		Integer max = archi.get(0).getPeso();
		Integer min = archi.get(archi.size()-1).getPeso();
		Integer medio = (max+min)/2;
		
		List<Arco> result = new ArrayList<>();
		for (Arco a : archi) {
			if (a.getPeso()<medio) {
				result.add(a);
			}
		}
		return result;
	}
	
	//
	public List<String> getCammino(Arco a){
		String partenza = a.getCategoria1();
		String arrivo = a.getCategoria2();
		
		this.bestPercorso = new ArrayList<>();
		this.pesoMassimo=0;
		
		List<String> parziale = new ArrayList<>();
		Integer pesoTemp = 0;
		parziale.add(partenza);
		
		cerca(partenza, arrivo, parziale, pesoTemp);
		
		return bestPercorso;
	}
	private void cerca(String partenza, String arrivo, List<String> parziale, Integer pesoTemp) {
		List<String> vicini = Graphs.neighborListOf(this.grafo, partenza);
		
		//METTI I RETURN 
		if (parziale.get(parziale.size()-1).compareTo(arrivo)==0) {
			if (parziale.size()>bestPercorso.size() && pesoTemp>pesoMassimo ) {
				bestPercorso = new ArrayList<>(parziale);
				pesoMassimo = pesoTemp;
			} else {
				return;
			}
		}
		
		for (String v : vicini) {
			if (!parziale.contains(v) && !parziale.contains(arrivo)) {
				pesoTemp += (int) this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, v));
				parziale.add(v);
				cerca(v, arrivo, parziale, pesoTemp);
				pesoTemp = pesoTemp - (int) this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, v));
				parziale.remove(parziale.size()-1);
			}
		}
	}
	public Integer getPesoMassimo() {
		return pesoMassimo;
	}
	
	

}
