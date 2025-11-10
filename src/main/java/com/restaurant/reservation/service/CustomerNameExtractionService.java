package com.restaurant.reservation.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Service zur Extraktion von Kundennamen aus deutschem Text. */
public class CustomerNameExtractionService {

    // Unterstützte Formate:
    // 1. Grußformeln (voll/abgekürzt): "Vielen Dank Klaus", "vG Klaus", "mfG Maria"
    // 2. Selbstvorstellungen: "Ich bin Franz Schulze"
    private static final Pattern NAME_PATTERN = Pattern.compile(
        "(?:dank|gruß|grüße|grüßen|vG|mfG|vd|bg|ich\\s+bin|hier\\s+ist)\\s+" +
        "([A-ZÄÖÜ][a-zäöüß]+(?:\\s+[A-ZÄÖÜ][a-zäöüß]+){0,2})" +
        "(?=\\s*[,]|\\s+und|\\s*$)", 
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    /** Extrahiert Kundenname aus Text. */
    public String extractCustomerName(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text darf nicht leer sein");
        }

        Matcher matcher = NAME_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).replaceAll("\\s+", " ").trim();
        }
        
        throw new IllegalArgumentException("Name nicht gefunden");
    }

    /** Prüft ob Text ein Namenmuster enthält. */
    public boolean containsCustomerName(String text) {
        return text != null && NAME_PATTERN.matcher(text).find();
    }
}