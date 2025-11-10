package com.restaurant.reservation.service;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Service zur Extraktion von Uhrzeiten aus deutschem Text. */
public class TimeExtractionService {

    // Unterstützte Formate:
    // 1. HH:mm Format: "14:30", "um 14:30", "14:30 Uhr"
    // 2. H + Uhr: "8 Uhr", "um 8 Uhr", "8 Uhr abends"
    // 3. H + Tageszeit: "8 abends", "um 8 morgens"
    // 4. Mit Punkt: "5 p.m.", "5 a.m."
    private static final Pattern TIME_PATTERN = Pattern.compile(
        "(?:um\\s+)?" +  // optional "um"
        "(\\d{1,2})(?!\\d)" +  // Stunde (1-2 Ziffern)
        "(?:" +
            ":(\\d{2})" +  // :mm Format
            "|" +
            "\\s+uhr" +  // Uhr
            "|" +
            "\\s+(morgens|vormittags|mittags|nachmittags|abends|pm|am|p\\.m\\.|a\\.m\\.)" +  // Tageszeit
        ")" +
        "(?:\\s*uhr)?" +  // optional "Uhr" nach Tageszeit
        "(?:\\s+(morgens|vormittags|mittags|nachmittags|abends|pm|am|p\\.m\\.|a\\.m\\.))?",  // optional Tageszeit nach "Uhr"
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    /** Extrahiert Uhrzeit aus Text. */
    public LocalTime extractTime(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text darf nicht leer sein");
        }

        Matcher matcher = TIME_PATTERN.matcher(text);
        
        while (matcher.find()) {
            try {
                int hour = Integer.parseInt(matcher.group(1));
                int minute = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;
                String period = matcher.group(3) != null ? matcher.group(3) : matcher.group(4);
                
                if (hour > 23) continue;
                
                if (period != null) {
                    period = period.toLowerCase();
                    if (period.equals("nachmittags") || period.equals("abends") || 
                        period.equals("pm") || period.equals("p.m.")) {
                        if (hour >= 12) continue;
                        hour += 12;
                    }
                }
                
                return LocalTime.of(hour, minute);
                
            } catch (Exception e) {
                continue;
            }
        }

        throw new IllegalArgumentException("Uhrzeit nicht gefunden.");
    }

    /** Prüft ob Text ein Uhrzeit-Muster enthält. */
    public boolean containsTime(String text) {
        return text != null && TIME_PATTERN.matcher(text).find();
    }
}