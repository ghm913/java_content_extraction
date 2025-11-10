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
            errors.append(e.getMessage()).append(" ");
        }

        try {
            date = dateExtractor.extractDate(text);
        } catch (IllegalArgumentException e) {
            errors.append(e.getMessage()).append(" ");
        }

        try {
            time = timeExtractor.extractTime(text);
        } catch (IllegalArgumentException e) {
            errors.append(e.getMessage()).append(" ");
        }

        try {
            numberOfPeople = peopleCountExtractor.extractNumberOfPeople(text);
        } catch (IllegalArgumentException e) {
            errors.append(e.getMessage()).append(" ");
        }

        if (errors.length() > 0) {
            throw new IllegalArgumentException(errors.toString().trim());
        }

        return new Reservation(customerName, date, time, numberOfPeople);
    }
}