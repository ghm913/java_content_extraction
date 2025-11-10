package com.restaurant.reservation.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for extracting customer names from German text.
 */
public class CustomerNameExtractionService {

    // Simple working pattern for German greetings - removed broken "?" handling
    private static final Pattern NAME_PATTERN = Pattern.compile(
        "(?:(?:vielen\\s+dank|gruß|grüße|mit\\s+freundlichen\\s+grüßen?|\\bvg|mfg|lg)\\s+([A-ZÄÖÜa-zäöüß]+(?:\\s+[A-ZÄÖÜa-zäöüß]+)*))\\s*$|(?:(?:ich\\s+bin|mein\\s+name\\s+ist|hier\\s+(?:ist|spricht))\\s+([A-ZÄÖÜ][a-zäöüß]+\\s+[A-ZÄÖÜ][a-zäöüß]+))", 
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    /**
     * Extracts a customer name from German text.
     * Handles greetings at end, abbreviated greetings at end, and self-introductions anywhere.
     */
    public String extractCustomerName(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        Matcher matcher = NAME_PATTERN.matcher(text);
        if (matcher.find()) {
            // Group 1: greetings at end (full or abbreviated)
            // Group 2: self-introduction pattern
            String name = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            // Normalize whitespace and line breaks
            return name.replaceAll("\\s+", " ").trim();
        }
        
        throw new IllegalArgumentException("Could not extract customer name from: " + text);
    }

    /**
     * Checks if text contains a name pattern.
     */
    public boolean containsCustomerName(String text) {
        return text != null && NAME_PATTERN.matcher(text).find();
    }
}