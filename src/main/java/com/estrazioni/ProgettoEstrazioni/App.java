package com.estrazioni.ProgettoEstrazioni;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.estrazioni.ProgettoEstrazioni.utilities.DatabaseConnection;

public class App {
	
	protected static final Logger logger = LogManager.getLogger("App");

    public static void main( String[] args ) {
        Scanner scanner = new Scanner(System.in);
        logger.info("Avvio applicazione");
        
        System.out.println("Menu: ");
        
        boolean cicla = true;
        
        while (cicla) {
        	int operazione = 0;
        	String risposta = "";

        	try {
        		while(operazione != 1 && operazione != 2 && operazione != 3 && operazione != 4) {
		        	System.out.print("\t 1) Inizializzazione \n\t 2) Estrazione \n\t 3) Stampa situazione estrazioni \n\t 4) Eliminazione tabelle e dati\nInserire il tipo di operazione che si vuole effettuare: ");
        			operazione = scanner.nextInt();
        		}
            } catch (InputMismatchException e) {
            	logger.error("Errore! Scelte possibili: 1, 2, 3 o 4; Scelta effettuata = " + operazione);
                System.out.print("Errore! Scelte possibili: 1, 2, 3 o 4\n Indicare nuovamente l'operazione: ");
                scanner.nextLine(); // Consuma l'input non valido
                operazione = scanner.nextInt();
            }	

        	DatabaseConnection.getDatabaseConnection();
        	
        	switch(operazione) {
	        	case 1:
	        		// Inizializzazione database
	        		logger.info("Operazione scelta: inizializzazione database");
					try {
						DatabaseConnection.creaTabelle();
						logger.info("Tabelle create correttamente");
						DatabaseConnection.inizializzaPartecipanti();
						logger.info("Tabella partecipanti riempita correttamente");
					} catch (SQLException e) {
						logger.error("Errore durante l'inizializzazione del database: " + e.getErrorCode() + " - " + e.getMessage());
						DatabaseConnection.chiudiConnection();
						logger.info("Connessione con il database chiusa correttamente");
						cicla = false;
						e.printStackTrace();
					}
	        		break;
	        		
	        	case 2:
	        		// Estrazione
					try {
						logger.info("Operazione scelta: estrazione");
						DatabaseConnection.effettuaEstrazione();
					} catch (SQLException e1) {
						logger.error("Errore durante l'estrazione casuale di un partecipante: "+ e1.getErrorCode() + " - "  + e1.getMessage());
						DatabaseConnection.chiudiConnection();
						logger.info("Connessione con il database chiusa correttamente");
						cicla = false;
						e1.printStackTrace();
					}
	        		break;
	        		
	        	case 3:
	        		// Stampa situazioni estrazioni
	        		logger.info("Operazione scelta: stampa della situazione estrazioni fino a questo dato momento");
					try {
						DatabaseConnection.stampaSituazioneEstrazioni();
					} catch (SQLException e) {
						logger.error("Errore durante la stampa della situazione estrazioni: " + e.getErrorCode() + " - " + e.getMessage());
						DatabaseConnection.chiudiConnection();
						logger.info("Connessione con il database chiusa correttamente");
						cicla = false;
						e.printStackTrace();
					}
	        		break;
	        		
	        	case 4:
	        		// Cancellazione dati e tabelle
	        		logger.info("Operazione scelta: cancellazione dei dati ed eliminazione delle tabelle");
					try {
						DatabaseConnection.svuotaDati();
						logger.info("Truncate sulle tabelle eseguita con successo");
						DatabaseConnection.eliminaTabelle();
						logger.info("Drop delle tabelle eseguito con successo");
					} catch (SQLException e) {
						logger.error("Errore durante la cancellazione dei dati e l'eleiminazione delle tabelle: " + e.getErrorCode() + " - " + e.getMessage());
						DatabaseConnection.chiudiConnection();
						logger.info("Connessione con il database chiusa correttamente");
						cicla = false;
						e.printStackTrace();
					}
	        		break;
        		
        		default:
        			logger.error("Operazione selezionata non valida!");
        			break;
        	}
            
        	if(cicla) {
        		 while (!(risposta.equals("s") || risposta.equals("n"))){
	                 System.out.print("Vuoi fare un'altra operazione? (s/n)");
	                 risposta = scanner.next();
	             }
	             if (risposta.equals("n")) {
	                cicla = false;
	                System.out.println("Termine esecuzione");
	                logger.info("Termine esecuzione");
	                DatabaseConnection.chiudiConnection();
	                logger.info("Connessione con il database chiusa correttamente");
	             }
        	}
            
        }
        
        scanner.close();
    }
}
