package com.estrazioni.ProgettoEstrazioni.model;

public class Estrazione {
	
	private int id;
	private String partecipante, timestamp_estrazione;
	
		
	public Estrazione() {
		super();
	}

	public Estrazione(int id, String partecipante, String timestamp_estrazione) {
		super();
		this.id = id;
		this.partecipante = partecipante;
		this.timestamp_estrazione = timestamp_estrazione;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPartecipante() {
		return partecipante;
	}

	public void setPartecipante(String partecipante) {
		this.partecipante = partecipante;
	}

	public String getTimestamp_estrazione() {
		return timestamp_estrazione;
	}

	public void setTimestamp_estrazione(String timestamp_estrazione) {
		this.timestamp_estrazione = timestamp_estrazione;
	}

	@Override
	public String toString() {
		return "Estrazione: ID = " + id + ", partecipante = " + partecipante + ", timestamp_estrazione = "
				+ timestamp_estrazione;
	}

}
