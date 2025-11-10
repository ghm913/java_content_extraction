package com.restaurant.reservation.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for extracting the number of people from German text.
 */
public class PeopleCountExtractionService {

    // Supported formats:
    // 1. Numeric with preposition: "für 4 Personen", "mit 8 Leuten", "für 2 Mann"
    // 2. Numeric without preposition: "4 Personen", "8 Leute", "2 Menschen"  
    // 3. German words with preposition: "für zwei Personen", "mit acht Leuten"
    // 4. German words without preposition: "zwei Personen", "acht Menschen"
    // - Supports numeric (1-100) 

    private static final Pattern PEOPLE_PATTERN = Pattern.compile(
        "(?:für\\s+|mit\\s+)?(\\d+|[a-zäöüß]+)(?!\\s*(?:uhr|morgens|vormittags|mittags|nachmittags|abends|pm|am))\\s+(?:personen|leuten?|mann|menschen)", 
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    private final GermanNumberParserService germanNumberParser;

    public PeopleCountExtractionService(GermanNumberParserService germanNumberParser) {
        this.germanNumberParser = germanNumberParser;
    }

    /**
     * Extracts the number of people from German text.
     */
    public int extractNumberOfPeople(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        Matcher matcher = PEOPLE_PATTERN.matcher(text);
        while (matcher.find()) {
            String countText = matcher.group(1);
            try {
                // Try numeric first
                int count = Integer.parseInt(countText);
                // Boundary check: 1-99 people
                if (count < 1 || count > 99) {
                    throw new IllegalArgumentException("Number of people must be between 1 and 99, got: " + count);
                }
                return count;
            } catch (NumberFormatException e) {
                // Try German number words
                Integer count = germanNumberParser.parseNumberString(countText.toLowerCase());
                if (count != null) {
                    // Boundary check: 1-99 people
                    if (count < 1 || count > 99) {
                        throw new IllegalArgumentException("Number of people must be between 1 and 99, got: " + count);
                    }
                    return count;
                }
            }
        }

        throw new IllegalArgumentException("Could not extract number of people from: " + text);
    }

    /**
     * Checks if text contains a people count pattern.
     */
    public boolean containsPeopleCount(String text) {
        return text != null && PEOPLE_PATTERN.matcher(text).find();
    }
}