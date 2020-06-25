package it.polito.tdp.crimes.model;

public class Arco {
	private String categoria1;
	private String categoria2;
	private Integer peso;
	/**
	 * @param categoria1
	 * @param categoria2
	 * @param peso
	 */
	public Arco(String categoria1, String categoria2, Integer peso) {
		super();
		this.categoria1 = categoria1;
		this.categoria2 = categoria2;
		this.peso = peso;
	}
	public String getCategoria1() {
		return categoria1;
	}
	public void setCategoria1(String categoria1) {
		this.categoria1 = categoria1;
	}
	public String getCategoria2() {
		return categoria2;
	}
	public void setCategoria2(String categoria2) {
		this.categoria2 = categoria2;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		
		return String.format("R1: %s. R2: %s. P= %d", categoria1, categoria2, peso);
	}
	
	

}
