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
        String customerName = null;
        LocalDate date = null;
        LocalTime time = null;
        Integer numberOfPeople = null;

        try {
            customerName = customerNameExtractor.extractCustomerName(text);
        } catch (IllegalArgumentException e) {
            errors.append("Name nicht gefunden. ");
        }

        try {
            date = dateExtractor.extractDate(text);
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            if (msg.contains("Invalid date")) {
                errors.append("Ungültiges Datumsformat. ");
            } else if (msg.contains("Year must be")) {
                errors.append("Jahr muss zwischen 2000 und 2100 liegen. ");
            } else {
                errors.append("Datum nicht gefunden. ");
            }
        }

        try {
            time = timeExtractor.extractTime(text);
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            if (msg.contains("Invalid time")) {
                errors.append("Ungültiges Zeitformat. ");
            } else {
                errors.append("Uhrzeit nicht gefunden. ");
            }
        }

        try {
            numberOfPeople = peopleCountExtractor.extractNumberOfPeople(text);
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            if (msg.contains("between 1 and 99")) {
                errors.append("Personenanzahl muss zwischen 1 und 99 liegen. ");
            } else {
                errors.append("Personenanzahl nicht gefunden. ");
            }
        }

        if (errors.length() > 0) {
            throw new IllegalArgumentException(errors.toString().trim());
        }

        return new Reservation(customerName, date, time, numberOfPeople);
    }
}