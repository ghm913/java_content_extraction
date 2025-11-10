package com.restaurant.reservation.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for parsing German number words (1-99) into integer values.
 */
public class GermanNumberParserService {
    
    private static final Map<String, Integer> GERMAN_NUMBERS = new HashMap<>();
    static {
        // Basic numbers 1-20
        GERMAN_NUMBERS.put("ein", 1);
        GERMAN_NUMBERS.put("eine", 1);
        GERMAN_NUMBERS.put("eins", 1);
        GERMAN_NUMBERS.put("zwei", 2);
        GERMAN_NUMBERS.put("drei", 3);
        GERMAN_NUMBERS.put("vier", 4);
        GERMAN_NUMBERS.put("fünf", 5);
        GERMAN_NUMBERS.put("sechs", 6);
        GERMAN_NUMBERS.put("sieben", 7);
        GERMAN_NUMBERS.put("acht", 8);
        GERMAN_NUMBERS.put("neun", 9);
        GERMAN_NUMBERS.put("zehn", 10);
        GERMAN_NUMBERS.put("elf", 11);
        GERMAN_NUMBERS.put("zwölf", 12);
        GERMAN_NUMBERS.put("dreizehn", 13);
        GERMAN_NUMBERS.put("vierzehn", 14);
        GERMAN_NUMBERS.put("fünfzehn", 15);
        GERMAN_NUMBERS.put("sechzehn", 16);
        GERMAN_NUMBERS.put("siebzehn", 17);
        GERMAN_NUMBERS.put("achtzehn", 18);
        GERMAN_NUMBERS.put("neunzehn", 19);
        GERMAN_NUMBERS.put("zwanzig", 20);
        
        // Tens
        GERMAN_NUMBERS.put("dreißig", 30);
        GERMAN_NUMBERS.put("vierzig", 40);
        GERMAN_NUMBERS.put("fünfzig", 50);
        GERMAN_NUMBERS.put("sechzig", 60);
        GERMAN_NUMBERS.put("siebzig", 70);
        GERMAN_NUMBERS.put("achtzig", 80);
        GERMAN_NUMBERS.put("neunzig", 90);
    }

    /**
     * Parses German number words or numeric strings.
     */
    public Integer parseNumberString(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        
        String number = text.toLowerCase().trim();
        
        // Try German number first
        Integer germanNumber = GERMAN_NUMBERS.get(number);
        if (germanNumber != null) return germanNumber;
        
        // Try compound numbers (e.g., "einunddreißig")
        if (number.contains("und")) {
            String[] parts = number.split("und");
            if (parts.length == 2) {
                Integer ones = GERMAN_NUMBERS.get(parts[0].trim());
                Integer tens = GERMAN_NUMBERS.get(parts[1].trim());
                if (ones != null && tens != null && ones < 10 && tens >= 20) {
                    return tens + ones;
                }
            }
        }
        
        // Try numeric parsing
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
