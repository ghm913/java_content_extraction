package com.restaurant.reservation.service;

import com.restaurant.reservation.model.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Main service for extracting reservation information from German text.
 */
public class ReservationExtractorService {
    
    private final CustomerNameExtractionService customerNameExtractor;
    private final DateExtractionService dateExtractor;
    private final TimeExtractionService timeExtractor;
    private final PeopleCountExtractionService peopleCountExtractor;

    /**
     * Default constructor that creates all extraction services.
     */
    public ReservationExtractorService() {
        this.customerNameExtractor = new CustomerNameExtractionService();
        this.dateExtractor = new DateExtractionService();
        this.timeExtractor = new TimeExtractionService();
        this.peopleCountExtractor = new PeopleCountExtractionService(new GermanNumberParserService());
    }

    /**
     * Extracts reservation information from German text.
     * All information must be present: customer name, date, time, and number of people.
     */
    public Reservation extractReservationInfo(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        // Debug output for total extraction
        System.err.println("DEBUG ReservationExtractor INPUT: '" + text + "'");
        
        StringBuilder missingInfo = new StringBuilder();
        String customerName = null;
        LocalDate date = null;
        LocalTime time = null;
        Integer numberOfPeople = null;

        // Try to extract customer name
        try {
            customerName = customerNameExtractor.extractCustomerName(text);
            System.err.println("DEBUG: Customer name extracted: '" + customerName + "'");
        } catch (IllegalArgumentException e) {
            System.err.println("DEBUG: Customer name extraction failed: " + e.getMessage());
            missingInfo.append("Customer name not found. ");
        }

        // Try to extract date
        try {
            date = dateExtractor.extractDate(text);
            System.err.println("DEBUG: Date extracted: " + date);
        } catch (IllegalArgumentException e) {
            System.err.println("DEBUG: Date extraction failed: " + e.getMessage());
            missingInfo.append("Date not found. ");
        }

        // Try to extract time
        try {
            time = timeExtractor.extractTime(text);
            System.err.println("DEBUG: Time extracted: " + time);
        } catch (IllegalArgumentException e) {
            System.err.println("DEBUG: Time extraction failed: " + e.getMessage());
            missingInfo.append("Time not found. ");
        }

        // Try to extract number of people
        try {
            numberOfPeople = peopleCountExtractor.extractNumberOfPeople(text);
            System.err.println("DEBUG: People count extracted: " + numberOfPeople);
        } catch (IllegalArgumentException e) {
            System.err.println("DEBUG: People count extraction failed: " + e.getMessage());
            missingInfo.append("Number of people not found. ");
        }

        // Check if all required information was found
        if (missingInfo.length() > 0) {
            System.err.println("DEBUG: Final result - INCOMPLETE: " + missingInfo.toString().trim());
            throw new IllegalArgumentException("Incomplete reservation information: " + missingInfo.toString().trim());
        }

        System.err.println("DEBUG: Final result - SUCCESS");
        return new Reservation(customerName, date, time, numberOfPeople);
    }

    /**
     * Checks if text contains all required reservation components.
     */
    public boolean containsCompleteReservationInfo(String text) {
        return text != null && 
               customerNameExtractor.containsCustomerName(text) &&
               dateExtractor.containsDate(text) &&
               timeExtractor.containsTime(text) &&
               peopleCountExtractor.containsPeopleCount(text);
    }
}