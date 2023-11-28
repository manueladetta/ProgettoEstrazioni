package com.estrazioni.ProgettoEstrazioni;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.estrazioni.ProgettoEstrazioni.utilities.DatabaseConnection;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Scanner scanner = new Scanner(System.in);
        
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
                System.out.print("Errore! Scelte possibili: 1, 2, 3 o 4\n Indicare nuovamente l'operazione: ");
                scanner.nextLine(); // Consuma l'input non valido
                operazione = scanner.nextInt();
            }	

        	DatabaseConnection.getDatabaseConnection();
        	
        	switch(operazione) {
	        	case 1:
	        		// Inizializzazione database
	        		System.out.println("Inizializzazione database");
					try {
						DatabaseConnection.creaTabelle();
						DatabaseConnection.inizializzaPartecipanti();
					} catch (SQLException e) {
						DatabaseConnection.chiudiConnection();
						cicla = false;
						e.printStackTrace();
						System.out.println(e.getMessage());
					}
	        		break;
	        		
	        	case 2:
	        		// Estrazione
	        		System.out.println("Estrazione");
					try {
						DatabaseConnection.effettuaEstrazione();
					} catch (SQLException e1) {
						DatabaseConnection.chiudiConnection();
						cicla = false;
						e1.printStackTrace();
						System.out.println(e1.getMessage());
					}
	        		break;
	        		
	        	case 3:
	        		// Stampa situazioni estrazioni
	        		System.out.println("Stampa situazioni estrazioni");
					try {
						DatabaseConnection.stampaSituazioneEstrazioni();
					} catch (SQLException e) {
						DatabaseConnection.chiudiConnection();
						cicla = false;
						e.printStackTrace();
						System.out.println(e.getMessage());
					}
	        		break;
	        		
	        	case 4:
	        		// Cancellazione dati e tabelle
	        		System.out.println("Cancellazione dati e tabelle");
					try {
						DatabaseConnection.svuotaDati();
						DatabaseConnection.eliminaTabelle();
					} catch (SQLException e) {
						DatabaseConnection.chiudiConnection();
						cicla = false;
						e.printStackTrace();
						System.out.println(e.getMessage());
					}
	        		break;
        		
        		default:
        			break;
        	}
            
             while (!(risposta.equals("s") || risposta.equals("n"))){
                 System.out.print("Vuoi fare un'altra operazione? (s/n)");
                 risposta = scanner.next();
             }
             if (risposta.equals("n")) {
                cicla = false;
                System.out.println("Termine esecuzione");
                DatabaseConnection.chiudiConnection();
             }
        }
        
        scanner.close();
    }
}
