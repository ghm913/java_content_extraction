package com.restaurant.reservation.service;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Service zur Extraktion von Uhrzeiten aus deutschem Text. */
public class TimeExtractionService {

    // Unterstützte Formate:
    // 1. HH:mm Format: "14:30", "um 14:30", "14:30 Uhr"
    // 2. H + Uhr: "8 Uhr", "um 8 Uhr"
    // 3. H + Tageszeit: "8 abends", "um 8 morgens"
    // 4. H allein: "um 8" (nur mit "um")
    // 5. Mit Punkt: "5 p.m.", "5 a.m."
    private static final Pattern TIME_PATTERN = Pattern.compile(
        "um\\s+(\\d{1,2})(?!\\d)(?::(\\d{2})(?:\\s*uhr)?|\\s+(?:uhr|(morgens|vormittags|mittags|nachmittags|abends|pm|am|p\\.m\\.|a\\.m\\.))|(?=\\D|$))" +
        "(?:\\s+(morgens|vormittags|mittags|nachmittags|abends|pm|am|p\\.m\\.|a\\.m\\.))?" +
        "|" +
        "(\\d{1,2})(?!\\d)(?::(\\d{2})(?:\\s*uhr)?|\\s+(?:uhr|p\\.m\\.|a\\.m\\.|pm|am|morgens|vormittags|mittags|nachmittags|abends))" +
        "(?:\\s+(morgens|vormittags|mittags|nachmittags|abends|pm|am|p\\.m\\.|a\\.m\\.))?", 
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
                Integer hourVal = null;
                int minute = 0;
                String period = null;
                
                // Try groups 1-4 first (with "um")
                if (matcher.group(1) != null) {
                    hourVal = Integer.parseInt(matcher.group(1));
                    minute = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;
                    period = matcher.group(3) != null ? matcher.group(3) : matcher.group(4);
                }
                // Try groups 5-8 (without "um")
                else if (matcher.group(5) != null) {
                    hourVal = Integer.parseInt(matcher.group(5));
                    minute = matcher.group(6) != null ? Integer.parseInt(matcher.group(6)) : 0;
                    period = matcher.group(7) != null ? matcher.group(7) : matcher.group(8);
                }
                
                if (hourVal == null) continue;
                int hour = hourVal;
                
                // Validate hour range before processing
                if (hour > 23) continue;
                
                if (period != null) {
                    period = period.toLowerCase();
                    if ((period.equals("nachmittags") || period.equals("abends") || period.equals("pm") || period.equals("p.m."))) {
                        if (hour >= 12) {
                            throw new IllegalArgumentException("Invalid time format");
                        }
                        hour += 12;
                    }
                }
                
                return LocalTime.of(hour, minute);
                
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                continue;
            }
        }

        throw new IllegalArgumentException("Uhrzeit nicht gefunden");
    }

    /** Prüft ob Text ein Uhrzeit-Muster enthält. */
    public boolean containsTime(String text) {
        return text != null && TIME_PATTERN.matcher(text).find();
    }
}