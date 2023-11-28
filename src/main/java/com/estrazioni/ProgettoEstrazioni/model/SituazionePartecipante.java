package com.estrazioni.ProgettoEstrazioni.model;

public class SituazionePartecipante {
	
	private String nome, sede, timestamp_ultima_estrazione;
	private int id, numEstrazioni;
	
	public SituazionePartecipante() {
		super();
	}

	public SituazionePartecipante(String nome, String sede, String timestamp_utltima_estrazione, int id, int numEstrazioni) {
		super();
		this.nome = nome;
		this.sede = sede;
		this.timestamp_ultima_estrazione = timestamp_utltima_estrazione;
		this.id = id;
		this.numEstrazioni = numEstrazioni;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}

	public String getTimestamp_utltima_estrazione() {
		return timestamp_ultima_estrazione;
	}

	public void setTimestamp_utltima_estrazione(String timestamp_utltima_estrazione) {
		this.timestamp_ultima_estrazione = timestamp_utltima_estrazione;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumEstrazioni() {
		return numEstrazioni;
	}

	public void setNumEstrazioni(int numEstrazioni) {
		this.numEstrazioni = numEstrazioni;
	}

	@Override
	public String toString() {
		return "Partecipante: ID = " + id + ", nome = " + nome + ", sede = " + sede + ", Ultima estrazione = "
				+ timestamp_ultima_estrazione + ", Numero estrazioni =" + numEstrazioni;
	}
	
	
	
	
}
