package com.restaurant.reservation.app;

import com.restaurant.reservation.model.Reservation;
import com.restaurant.reservation.service.ReservationExtractorService;

import java.util.Scanner;

/** Hauptanwendung zur Extraktion von Reservierungsinformationen. */
public class ReservationApp {
    
    public static void main(String[] args) {
        ReservationExtractorService service = new ReservationExtractorService();
        Scanner scanner = new Scanner(System.in, "UTF-8");
        
        System.out.println("=== Restaurant Reservation Information Extractor ===");
        System.out.println("Geben Sie Ihre Reservierungsanfrage ein (oder 'exit' zum Beenden):\n");
        
        try {
            while (true) {
                try {
                    System.out.print("Eingabe: ");
                    String input = scanner.nextLine();
                    
                    if (input.trim().equalsIgnoreCase("exit")) {
                        System.out.println("\nAnwendung wird beendet. Auf Wiedersehen!");
                        break;
                    }
                    
                    if (input.trim().isEmpty()) {
                        continue;
                    }
                    
                    try {
                        Reservation reservation = service.extractReservationInfo(input);
                        System.out.println("Extrahiert: " + reservation);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Fehler: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Fehler: Unerwarteter Fehler bei der Verarbeitung");
                    }
                    
                    System.out.println();
                } catch (java.util.NoSuchElementException e) {
                    break;  // Ctrl+C
                }
            }
        } finally {
            scanner.close();
        }
    }
}