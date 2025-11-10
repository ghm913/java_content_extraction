package com.restaurant.reservation.app;

import com.restaurant.reservation.model.Reservation;
import com.restaurant.reservation.service.ReservationExtractorService;

import java.util.Scanner;

/**
 * Restaurant Reservation Information Extractor - Main Application
 * 
 * Interactive application for extracting reservation information from German text.
 * Continuously processes user input until shutdown signal is received.
 */
public class ReservationApp {
    
    public static void main(String[] args) {
        // Set console encoding to UTF-8 for proper German character support
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("console.encoding", "UTF-8");
        
        ReservationExtractorService service = new ReservationExtractorService();
        Scanner scanner = new Scanner(System.in, "UTF-8");
        
        System.out.println("=== Restaurant Reservation Information Extractor ===");
        System.out.println("Geben Sie Ihre Reservierungsanfrage ein (oder 'exit' zum Beenden):\n");
        
        while (true) {
            System.out.print("Eingabe: ");
            String input = scanner.nextLine();
            
            // Check for shutdown signals
            if (input.trim().equalsIgnoreCase("exit") || 
                input.trim().equalsIgnoreCase("quit") ||
                input.trim().equalsIgnoreCase("beenden")) {
                System.out.println("\nAnwendung wird beendet. Auf Wiedersehen!");
                break;
            }
            
            // Skip empty lines
            if (input.trim().isEmpty()) {
                continue;
            }
            
            // Process the reservation request
            try {
                Reservation reservation = service.extractReservationInfo(input);
                System.out.println("Extrahiert: " + reservation);
            } catch (Exception e) {
                System.out.println("Fehler: " + e.getMessage());
            }
            
            System.out.println();
        }
        
        scanner.close();
    }
}