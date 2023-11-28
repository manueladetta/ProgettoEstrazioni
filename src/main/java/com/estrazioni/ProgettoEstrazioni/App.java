package com.estrazioni.ProgettoEstrazioni;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Menu: \nSelezionare una tra le seguenti operazioni:\n\t 1) Inizializzazione \n\t 2) Estrazione \n\t 3) Stampa situazione estrazioni \n\t 4) Eliminazione tabelle e dati");
        
        boolean cicla = true;
        
        while (cicla) {
        	int operazione = 0;
        	String risposta = "";

        	try {
        		while(operazione != 1 && operazione != 2 && operazione != 3 && operazione != 4) {
		        	System.out.print("Inserire il tipo di operazione che si vuole effettuare: ");
        			operazione = scanner.nextInt();
        		}
            } catch (InputMismatchException e) {
                System.out.print("Errore! Scelte possibili: 1, 2, 3 o 4\n Indicare nuovamente l'operazione: ");
                scanner.nextLine(); // Consuma l'input non valido
                operazione = scanner.nextInt();
            }	
        	
        	switch(operazione) {
	        	case 1:
	        		// Inizializzazione database
	        		System.out.println("Inizializzazione database");
	        		break;
	        		
	        	case 2:
	        		// Estrazione
	        		System.out.println("Estrazione");
	        		break;
	        		
	        	case 3:
	        		// Stampa situazioni estrazioni
	        		System.out.println("Stampa situazioni estrazioni");
	        		break;
	        		
	        	case 4:
	        		// Cancellazione dati e tabelle
	        		System.out.println("Cancellazione dati e tabelle");
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
             }
        }
    }
}
