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
	private Graph <String, DefaultWeightedEdge> grafo;
	private EventsDAO dao;
	private List<Adiacenza> adiacenze;
	private List<String> best;
	private Integer pMin;
	
	public Model(){
		this.dao = new EventsDAO();
	}
	
	public void creaGrafo(String categoria, Integer anno) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.dao.getVertex(categoria, anno));
		this.adiacenze = new ArrayList<> (this.dao.getEdge(categoria, anno));
		
		for(Adiacenza a : this.dao.getEdge(categoria, anno)) {
			if(a.getPeso()!=0) {
				Graphs.addEdgeWithVertices(this.grafo, a.getT1(), a.getT2(), a.getPeso());
			}
		}
	}
	
	public List<Adiacenza> getPesoMassimo(){
		List<Adiacenza> result = new ArrayList<>();
		Collections.sort(this.adiacenze);
		Integer pesoMax = this.adiacenze.get(0).getPeso();
		
		for(Adiacenza a : this.adiacenze) {
			if(a.getPeso() == pesoMax) {
				result.add(a);
			}
		}
		
		return result;
	}
	
	public List<Adiacenza> getAdiacenze() {
		return adiacenze;
	}

	public List<String> getVertex(){
		List<String> vertici = new ArrayList<>(this.grafo.vertexSet());
		return vertici;
	}
		
	public List<DefaultWeightedEdge> getEdge(){
		List<DefaultWeightedEdge> archi = new ArrayList<>(this.grafo.edgeSet());
		return archi;
	}
	
	public List<String> percorsoMinimo(Adiacenza a){
		String partenza = a.getT1();
		String arrivo = a.getT2();
		
		this.best = new ArrayList<>();
		this.pMin = Integer.MAX_VALUE;
		
		List<String> parziale = new ArrayList<>();
		Integer pTemp = Integer.MAX_VALUE;
		parziale.add(partenza);
		
		cerca(arrivo, parziale, pTemp);
		
		return best;
		
	}

	private void cerca(String arrivo, List<String> parziale, Integer pTemp) {
		List<String> vicini = Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1));
		
		if(parziale.get(parziale.size()-1).equals(arrivo)) {
			if(parziale.size()>best.size() && pTemp < pMin) {
				this.best = new ArrayList<>(parziale);
				this.pMin = pTemp;
			}
		}
	
		for(String s:vicini) {
			if(parziale.size()==1) {
				pTemp = 0;
			}
			if(!parziale.contains(s)) {
				String precedente = parziale.get(parziale.size()-1);
				parziale.add(s);
				pTemp += (int) this.grafo.getEdgeWeight(this.grafo.getEdge(precedente, s));
				cerca(arrivo, parziale, pTemp);
				parziale.remove(s);
				pTemp = pTemp - (int) this.grafo.getEdgeWeight(this.grafo.getEdge(precedente, s));
			} 
		}
	}

	


}
