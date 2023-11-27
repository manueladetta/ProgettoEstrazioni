package com.estrazioni.ProgettoEstrazioni.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.estrazioni.ProgettoEstrazioni.model.Partecipante;

public class DatabaseConnection {
	
	private static Connection con = null;
	
	private static String path = "./resources/config.properties", jdbcDriver = "com.mysql.cj.jdbc.Driver";
	
	private static String db_user = "", db_url = "", db_password = "", db_schema = "";	
	
	 public DatabaseConnection(){

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
					System.out.println("Valori letti da file di properties: \n\t- URL: " + db_url + "\n\t- User: " + db_user + "\n\t- Password: " + db_password + "\n\t- Schema: " + db_schema);
//					System.out.println(properties.getProperty("script_create_schema"));
				} catch (IOException e) {
					System.out.println("Errore caricamento dati dal file di properties " + path + " : " + e.getMessage());
				}
	}
	
	// Metodo per recuperare il Driver per un database
	private static void getDriverClass(String driver) {
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
	}
	
	// Metodo che restituisce uno statement 
	private static Statement creaStatement(Connection connection) throws SQLException {
		return connection.createStatement();
	}
	
	// Metodo per creare recuperare l'elenco dei partecipanti
	private List<Partecipante> getPartecipanti(Connection connection) throws SQLException {
		List<Partecipante> elencoPartecipanti = new ArrayList<>();

		Statement stm = creaStatement(connection);
		
		ResultSet rs = stm.executeQuery("SELECT * FROM partecipanti");
		
		while(rs.next()) {
			Partecipante p = new Partecipante(rs.getInt("id"), rs.getString("nome"), rs.getString("sede"));
			elencoPartecipanti.add(p);
		}
		return elencoPartecipanti;
	}
	
	// Metodo che esegue una query usando uno Statement
	private static void eseguiStatement(Connection connection, String query) throws SQLException {
		Statement stm = creaStatement(connection);
		stm.executeUpdate(query);
		chiudiStatement(stm);
	}
	
	// Metodo che inizializza il database
	public static void creaTabelle(Connection connection) throws SQLException {
		creaTabellaPartecipanti(connection);
		creaTabellaEstrazioni(connection);
	}
	
	// Metodo per creare la tabella contenente l'elenco dei partecipanti in caso non esistesse già
	private static void creaTabellaPartecipanti(Connection connection) throws SQLException {
		// Drop della tabella se esiste
		dropTabella(connection, "partecipanti");

		String query = "CREATE TABLE partecipanti ("
				+ " id int NOT NULL AUTO_INCREMENT, "
				+ " nome varchar(255) NOT NULL, "
				+ " sede varchar(255) NOT NULL, "
				+ " PRIMARY KEY (id));";
		
		eseguiStatement(connection, query);
		
		System.out.println("Creata tabella PARTECIPANTI");
	}
	
	// Metodo per creare la tabella contenente l'elenco delle estrazioni in caso non esistesse già
	private static void creaTabellaEstrazioni(Connection connection) throws SQLException {	
		// Drop della tabella se esiste
		dropTabella(connection, "estrazioni");
		
		String query = "CREATE TABLE estrazioni ("
			+ " id int NOT NULL AUTO_INCREMENT, "
			+ " partecipante int NOT NULL, "
			+ " timestamp_estrazione TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
			+ " PRIMARY KEY (id));";
	
		eseguiStatement(connection, query);
		
		System.out.println("Creata tabella ESTRAZIONI");
	}
	
	// Metodo che esegue il drop di una tabella passata come parametro
	private static void dropTabella(Connection connection, String tabella) throws SQLException {
		String query = "DROP TABLE IF EXISTS " + tabella + ";"; 
		eseguiStatement(connection, query);
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
	private static void chiudiConnection(Connection connection) {
		// Chiudo la connessione
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
//		leggiValoriConfig(path);
		System.out.println("Apro la connessione");
		Connection con = getDatabaseConnection();
		try {
			System.out.println("Creo le tabelle");
			creaTabelle(con);
			System.out.println("Tabelle create");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Chiudo la connessione");
			chiudiConnection(con);
			System.out.println("Connessione chiusa");
		}
		
	}
}
