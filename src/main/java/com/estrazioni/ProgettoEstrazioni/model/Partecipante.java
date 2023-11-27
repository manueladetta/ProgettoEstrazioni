package com.estrazioni.ProgettoEstrazioni.model;

public class Partecipante {
	
	private int id;
	private String nome, sede;
	

	public Partecipante(int id, String nome, String sede) {
		super();
		this.id = id;
		this.nome = nome;
		this.sede = sede;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Partecipante: ID = " + id + ", nome = " + nome + ", sede = " + sede;
	}
	
	

}
