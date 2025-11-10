package com.restaurant.reservation.service;

import com.restaurant.reservation.model.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

/** Haupt-Service zur Extraktion aller Reservierungsinformationen aus deutschem Text. */
public class ReservationExtractorService {
    
    private final CustomerNameExtractionService customerNameExtractor;
    private final DateExtractionService dateExtractor;
    private final TimeExtractionService timeExtractor;
    private final PeopleCountExtractionService peopleCountExtractor;

    public ReservationExtractorService() {
        this.customerNameExtractor = new CustomerNameExtractionService();
        this.dateExtractor = new DateExtractionService();
        this.timeExtractor = new TimeExtractionService();
        this.peopleCountExtractor = new PeopleCountExtractionService(new GermanNumberParserService());
    }

    /** Extrahiert alle Reservierungsinformationen aus Text. */
    public Reservation extractReservationInfo(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text darf nicht leer sein");
        }
        
        StringBuilder errors = new StringBuilder();
        
        // Lambda-basierte Extraktion mit minimalen Änderungen
        String customerName = tryExtract(() -> customerNameExtractor.extractCustomerName(text), errors);
        LocalDate date = tryExtract(() -> dateExtractor.extractDate(text), errors);
        LocalTime time = tryExtract(() -> timeExtractor.extractTime(text), errors);
        Integer numberOfPeople = tryExtract(() -> peopleCountExtractor.extractNumberOfPeople(text), errors);

        if (errors.length() > 0) {
            throw new IllegalArgumentException(errors.toString().trim());
        }

        return new Reservation(customerName, date, time, numberOfPeople);
    }
    
    // Einfache Hilfsmethode für Try-Catch mit Lambda
    private <T> T tryExtract(java.util.function.Supplier<T> extractor, StringBuilder errors) {
        try { 
            return extractor.get();
        } catch (IllegalArgumentException e) {
            errors.append(e.getMessage()).append(" ");
            return null;
        }
    }
}