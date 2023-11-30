package com.estrazioni.ProgettoEstrazioni.utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.estrazioni.ProgettoEstrazioni.model.Estrazione;
import com.estrazioni.ProgettoEstrazioni.model.Partecipante;
import com.estrazioni.ProgettoEstrazioni.model.SituazionePartecipante;

public class DatabaseConnection {
	
	private static Connection con = null;
	
	private static String path = "./resources/config.properties", jdbcDriver = "com.mysql.cj.jdbc.Driver", path_csv = "./resources/esercizioPartecipanti.csv";
	
	private static String db_user = "", db_url = "", db_password = "", db_schema = "";	
	
	 public DatabaseConnection(){
		 getDatabaseConnection();
	 }
	
	// Pattern Singleton
	public static Connection getDatabaseConnection() {
		if(con == null) {
			// Prima chiamata = creiamo l'oggetto di tipo Connection
			leggiValoriConfig(path);
			try {
				getDriverClass(jdbcDriver);
				con = DriverManager.getConnection("jdbc:mysql://" + db_url + "/" + db_schema, db_user, db_password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return con;
		} else {
			return con;
		}
	}
	
	public static String getPath() {
		return path;
	}
	public static void setPath(String path) {
		DatabaseConnection.path = path;
	}
	public static String getDb_user() {
		return db_user;
	}
	public static void setDb_user(String db_user) {
		DatabaseConnection.db_user = db_user;
	}
	public static String getDb_url() {
		return db_url;
	}
	public static void setDb_url(String db_url) {
		DatabaseConnection.db_url = db_url;
	}
	public static String getDb_password() {
		return db_password;
	}
	public static void setDb_password(String db_password) {
		DatabaseConnection.db_password = db_password;
	}
	public static String getDb_schema() {
		return db_schema;
	}
	public static void setDb_schema(String db_schema) {
		DatabaseConnection.db_schema = db_schema;
	}

	// Metodo per leggere da file di configurazione i valori per effettuare la connessione al db 
	private static void leggiValoriConfig(String path) {				
				InputStream inputStream = null;
				try {
					inputStream = new FileInputStream(path);
				} catch (FileNotFoundException e2) {
					System.out.println("Errore caricamento dati dal file di properties " + path + " : " + e2.getMessage());
				}
				
				if(inputStream == null) {
					System.out.println("Errore caricamento file di properties " + path);
					return ;
				}
				
				Properties properties = new Properties();
				try {
					properties.load(inputStream);
					db_user = properties.getProperty("db.user");
					db_url = properties.getProperty("db.url");
					db_password = properties.getProperty("db.password");
					db_schema = properties.getProperty("db.schema");
//					System.out.println("Valori letti da file di properties: \n\t- URL: " + db_url + "\n\t- User: " + db_user + "\n\t- Password: " + db_password + "\n\t- Schema: " + db_schema);
//					System.out.println(properties.getProperty("script_create_schema"));
				} catch (IOException e) {
					System.out.println("Errore caricamento dati dal file di properties " + path + " : " + e.getMessage());
				}
	}
	
	// Metodo che crea il DB
//	public static void creaDB() {
//		Connection conn;
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			conn = DriverManager.getConnection
//					("jdbc:mysql://localhost:3306/?user=root&password=Sterpaglia157@_");
//			Statement statm = conn.createStatement();
//			int result= statm.executeUpdate("CREATE DATABASE IF NOT EXISTS SecondoDB");
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	}
	
	// Metodo per recuperare il Driver per un database
	private static void getDriverClass(String driver) {
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
	}
	
	// Metodo che restituisce uno statement 
	private static Statement creaStatement() throws SQLException {
		return con.createStatement();
	}
	
	// Metodo per recuperare l'elenco dei partecipanti
	private static List<Partecipante> getPartecipanti() throws SQLException {
		List<Partecipante> elencoPartecipanti = new ArrayList<>();

		Statement stm = creaStatement();
		
		ResultSet rs = stm.executeQuery("SELECT * FROM partecipanti");
		
		while(rs.next()) {
			Partecipante p = new Partecipante(rs.getInt("id"), rs.getString("nome"), rs.getString("sede"));
			elencoPartecipanti.add(p);
		}
		return elencoPartecipanti;
	}
	
	// Metodo per recuperare l'elenco delle estrazioni in ordine dalla più recente alla meno
	private static List<Estrazione> getEstrazioni() throws SQLException {
		List<Estrazione> elencoEstrazioni = new ArrayList<>();

		Statement stm = creaStatement();
		
		ResultSet rs = stm.executeQuery("SELECT * FROM estrazioni e JOIN partecipanti p ON p.id = e.partecipante ORDER BY timestamp_estrazione DESC");
		
		while(rs.next()) {
			Estrazione e = new Estrazione(rs.getInt("id"), rs.getString("nome") + " (" + rs.getString("sede") + ")", rs.getString("timestamp_estrazione"));
			elencoEstrazioni.add(e);
		}
		return elencoEstrazioni;
	}
	
	// Metodo che esegue una query usando uno Statement
	private static void eseguiStatement(String query) throws SQLException {
		Statement stm = creaStatement();
		stm.executeUpdate(query);
		chiudiStatement(stm);
	}
	
	// Metodo che inizializza il database
	public static void creaTabelle() throws SQLException {
		creaTabellaPartecipanti();
		creaTabellaEstrazioni();
	}
	
	// Metodo per creare la tabella contenente l'elenco dei partecipanti in caso non esistesse già
	private static void creaTabellaPartecipanti() throws SQLException {
		// Drop della tabella se esiste
//		dropTabella("partecipanti");

		String query = "CREATE TABLE IF NOT EXISTS partecipanti ("
				+ " id int NOT NULL AUTO_INCREMENT, "
				+ " nome varchar(255) NOT NULL, "
				+ " sede varchar(255) NOT NULL, "
				+ " PRIMARY KEY (id));";
		
		eseguiStatement(query);
		
		System.out.println("Creata tabella PARTECIPANTI");
	}
	
	// Metodo per creare la tabella contenente l'elenco delle estrazioni in caso non esistesse già
	private static void creaTabellaEstrazioni() throws SQLException {	
		// Drop della tabella se esiste
//		dropTabella("estrazioni");
		
		String query = "CREATE TABLE IF NOT EXISTS estrazioni ("
			+ " id int NOT NULL AUTO_INCREMENT, "
			+ " partecipante int NOT NULL, "
			+ " timestamp_estrazione TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
			+ " PRIMARY KEY (id));";
	
		eseguiStatement(query);
		
		System.out.println("Creata tabella ESTRAZIONI");
	}
	
	// Metodo che esegue il drop di una tabella passata come parametro
	private static void dropTabella(String tabella) throws SQLException {
		String query = "DROP TABLE IF EXISTS " + tabella + ";"; 
		eseguiStatement(query);
	}
	
	// Metodo per chiudere uno statement
	private static void chiudiStatement(Statement stm) {
		// Chiudo lo statement
		if (stm != null) {
			try {
				stm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Metodo per chiudere una connessione
	public static void chiudiConnection() {
		// Chiudo la connessione
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Metodo utilizzato per riempire la tabella partecipanti 
	public static void inizializzaPartecipanti() {
		try {
			svuotaTabella("partecipanti");
			BufferedReader reader;
			String line;
			reader = new BufferedReader(new FileReader(path_csv));
			while ((line = reader.readLine()) != null) {
			    String[] parts = line.split(";");
			    String nome = parts[0];
			    String sede = parts[1];
			    
//			    System.out.println("Nome: " + nome + ", Sede: " + sede);
			    
			    PreparedStatement prepared = creaPreparedStatement("INSERT INTO partecipanti (nome, sede) VALUES (?, ?);");
			    prepared.setString(1, nome);
			    prepared.setString(2, sede);
			    prepared.executeUpdate();
			    
			}
			reader.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.out.println(e1.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
    
	}
	
	// Metodo che restituisce un PreparedStatement
	private static PreparedStatement creaPreparedStatement(String query) throws SQLException {
		return con.prepareStatement(query);
	}
	
	// Metodo utilizzato per svuotare il contenuto della tabella partecipanti
	public static void svuotaTabella(String tabella) throws SQLException {
		String query = "TRUNCATE TABLE " + tabella + ";";
		eseguiStatement(query);
	}
	
	// Metodo che estrae casualmente uno tra i partecipanti e registra l'estrazione all'interno della relativa tabella
	public static void effettuaEstrazione() throws SQLException {
		List<Partecipante> partecipanti = getPartecipanti();
		Random random = new Random();
		int i = random.nextInt(partecipanti.size() - 1);
		
		Partecipante soggettoEstratto = partecipanti.get(i);
		
		PreparedStatement prepared = creaPreparedStatement("INSERT INTO estrazioni (partecipante, timestamp_estrazione) VALUES (?, ?);");
	    prepared.setInt(1, soggettoEstratto.getId());
	    LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
	    prepared.setTimestamp(2, timestamp);
	    prepared.executeUpdate();
	    
	    Estrazione estrazione = ottieniUltimaEstrazione();
	    System.out.println(estrazione);
		
	}
	
	// Metodo per recuperare l'ultima estrazione registrata
	private static Estrazione ottieniUltimaEstrazione() throws SQLException {
		Estrazione ultimaEstrazione = null;
		
		Statement stm = creaStatement();
		
		ResultSet rs = stm.executeQuery("SELECT * FROM estrazioni e JOIN partecipanti p ON p.id = e.partecipante ORDER BY timestamp_estrazione DESC LIMIT 1;");
		
		while(rs.next()) {
			ultimaEstrazione = new Estrazione(rs.getInt("id"), rs.getString("nome") + " (" + rs.getString("sede") + ")", rs.getString("timestamp_estrazione"));
		}
		return ultimaEstrazione;
	}
	
	// Metodo per ottenere la situazione delle estrazioni
	private static List<SituazionePartecipante> ottieniSituazioneEstrazioni() throws SQLException {
		List<SituazionePartecipante> esiti = new ArrayList<>();
		
		Statement stm = creaStatement();
		
		ResultSet rs = stm.executeQuery("SELECT p.id, p.nome, p.sede, COUNT(e.partecipante) AS numEstrazioni"
				+ " FROM estrazioni e JOIN partecipanti p ON p.id = e.partecipante"
				+ " GROUP BY e.partecipante; ");
		
		while(rs.next()) {
			SituazionePartecipante s = new SituazionePartecipante();
			s.setId(rs.getInt("id"));
			s.setNome(rs.getString("nome"));
			s.setSede(rs.getString("sede"));
			s.setNumEstrazioni(rs.getInt("numEstrazioni"));
			esiti.add(s);
		}
		
		for(SituazionePartecipante e : esiti) {
			rs = stm.executeQuery("SELECT timestamp_estrazione"
					+ " FROM estrazioni"
					+ " WHERE partecipante = " + e.getId()
					+ " ORDER BY timestamp_estrazione DESC\r\n"
					+ " LIMIT 1;");
			while(rs.next()) {
				e.setTimestamp_utltima_estrazione(rs.getString("timestamp_estrazione"));
			}
		}
		
		return esiti;
	}
	
	// Metodo per stampare la situazione delle estrazioni
	public static void stampaSituazioneEstrazioni() throws SQLException {
		List<SituazionePartecipante> situa = ottieniSituazioneEstrazioni();
		for(SituazionePartecipante s : situa) {
			System.out.println(s);
		}
	}
	
	// Metodo che elimina i dati dalle varie tabelle del db
	public static void svuotaDati() throws SQLException {
		svuotaTabella("estrazioni");
		svuotaTabella("partecipanti");
	}
	
	// Metodo che elimina le tabelle
	public static void eliminaTabelle() throws SQLException {
		dropTabella("partecipanti");
		dropTabella("estrazioni");
	}
	
	public static void main(String[] args) {
//		leggiValoriConfig(path);
		System.out.println("Apro la connessione");
		con = getDatabaseConnection();
		try {
			System.out.println("Creo le tabelle");
			creaTabelle();
			System.out.println("Tabelle create");
			System.out.println("Riempo la tabella partecipanti");
			inizializzaPartecipanti();
			System.out.println("Tabella partecipanti riempita");
			
			List<Partecipante> partecipanti = getPartecipanti();
			for(Partecipante p : partecipanti) {
				System.out.println(p);
			}
			
			System.out.println("Eseguo un'estrazione casuale");
			effettuaEstrazione();
			List<Estrazione> estrazioni = getEstrazioni();
			for(Estrazione es : estrazioni) {
				System.out.println(es);
			}
			
			stampaSituazioneEstrazioni();
			
			svuotaDati();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Chiudo la connessione");
			chiudiConnection();
			System.out.println("Connessione chiusa");
		}
		
		
	}
}
