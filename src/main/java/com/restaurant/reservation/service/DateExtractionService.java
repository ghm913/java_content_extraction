package com.restaurant.reservation.service;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for extracting dates from German text.
 */
public class DateExtractionService {
    
    private static final Map<String, Integer> GERMAN_MONTHS = new HashMap<>();

    static {
        // Full German month names
        GERMAN_MONTHS.put("januar", 1);
        GERMAN_MONTHS.put("februar", 2);
        GERMAN_MONTHS.put("märz", 3);
        GERMAN_MONTHS.put("april", 4);
        GERMAN_MONTHS.put("mai", 5);
        GERMAN_MONTHS.put("juni", 6);
        GERMAN_MONTHS.put("juli", 7);
        GERMAN_MONTHS.put("august", 8);
        GERMAN_MONTHS.put("september", 9);
        GERMAN_MONTHS.put("oktober", 10);
        GERMAN_MONTHS.put("november", 11);
        GERMAN_MONTHS.put("dezember", 12);
        
        // Abbreviated forms (Jan -> januar pattern)
        GERMAN_MONTHS.put("jan", 1);
        GERMAN_MONTHS.put("feb", 2);
        GERMAN_MONTHS.put("mär", 3);
        GERMAN_MONTHS.put("apr", 4);
        GERMAN_MONTHS.put("jun", 6);
        GERMAN_MONTHS.put("jul", 7);
        GERMAN_MONTHS.put("aug", 8);
        GERMAN_MONTHS.put("sep", 9);
        GERMAN_MONTHS.put("okt", 10);
        GERMAN_MONTHS.put("nov", 11);
        GERMAN_MONTHS.put("dez", 12);
    }

    // Date pattern regex with improved specificity to avoid conflicts
    // Supported formats:
    // 1. dd.mm format: "15.03", "am 15.03" 
    // 2. dd.mm. format: "15.03.", "am 15.03."
    // 3. dd.mm.yyyy format: "15.03.2025", "am 15.03.2025"
    // 4. dd. Month format: "15. März", "am 15. März", "15. mar"
    private static final Pattern DATE_PATTERN = Pattern.compile(
        "(?:am\\s+)?(\\d{1,2})\\.(\\d{1,2})\\.?(?:\\s*(\\d{4}))?(?!\\s*(?:uhr|:|morgens|vormittags|mittags|nachmittags|abends|pm|am))|(?:am\\s+)?(\\d{1,2})\\.\\s*([a-zäöüß]+)(?!\\s*(?:uhr|:|morgens|vormittags|mittags|nachmittags|abends|pm|am))", 
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    /**
     * Extracts a date from German text.
     */
    public LocalDate extractDate(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        Matcher matcher = DATE_PATTERN.matcher(text);
        if (matcher.find()) {
            try {
                if (matcher.group(3) != null || matcher.group(2) != null && matcher.group(2).matches("\\d+")) {
                    // Numeric format: dd.mm. or dd.mm.yyyy
                    int day = Integer.parseInt(matcher.group(1));
                    int month = Integer.parseInt(matcher.group(2));
                    int year = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : Year.now().getValue();
                    
                    if (year < 2000 || year > 2100) {
                        throw new IllegalArgumentException("Year must be between 2000 and 2100, got: " + year);
                    }
                    
                    return LocalDate.of(year, month, day);
                } else if (matcher.group(5) != null) {
                    // Month name format: dd. Month
                    int day = Integer.parseInt(matcher.group(4));
                    Integer month = GERMAN_MONTHS.get(matcher.group(5).toLowerCase());
                    
                    if (month != null) {
                        return LocalDate.of(Year.now().getValue(), month, day);
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date: " + e.getMessage());
            }
        }

        throw new IllegalArgumentException("Could not extract date from: " + text);
    }

    /**
     * Checks if text contains a date pattern.
     */
    public boolean containsDate(String text) {
        return text != null && DATE_PATTERN.matcher(text).find();
    }
}