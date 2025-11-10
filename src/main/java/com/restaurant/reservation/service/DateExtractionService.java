package com.restaurant.reservation.service;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Service zur Extraktion von Datumswerten aus deutschem Text. */
public class DateExtractionService {
    
    private static final Map<String, Integer> GERMAN_MONTHS = new HashMap<>();

    static {
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

    // Unterstützte Formate:
    // 1. dd.mm Format: "15.03", "am 15.03"
    // 2. dd.mm. Format: "15.03.", "am 15.03."
    // 3. dd.mm.yyyy Format: "15.03.2025", "am 15.03.2025"
    // 4. dd. Monat Format: "15. März", "am 15. März", "15. mar"
    private static final Pattern DATE_PATTERN = Pattern.compile(
        "(?:am\\s+)?" +  // optional "am"
        "(\\d{1,2})\\." +  // Tag (1-2 Ziffern) mit Punkt
        "(?:" +
            "(\\d{1,2})\\.?" +  // Monat als Zahl mit optionalem Punkt
            "(?:\\s*(\\d{4}))?" +  // optional Jahr (4 Ziffern)
            "|" +
            "\\s*([a-zäöüß]+)" +  // oder Monat als Wort
        ")" +
        "(?!\\s*(?:uhr|:|morgens|vormittags|mittags|nachmittags|abends|pm|am))",  // nicht gefolgt von Zeitangabe
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    /** Extrahiert Datum aus Text. */
    public LocalDate extractDate(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text darf nicht leer sein");
        }

        Matcher matcher = DATE_PATTERN.matcher(text);
        if (matcher.find()) {
            try {
                int day = Integer.parseInt(matcher.group(1));
                
                if (matcher.group(2) != null) {
                    int month = Integer.parseInt(matcher.group(2));
                    int year = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : Year.now().getValue();
                    
                    if (year < 2000 || year > 2100) {
                        throw new IllegalArgumentException("Jahr muss zwischen 2000 und 2100 liegen.");
                    }
                    
                    return LocalDate.of(year, month, day);
                } else if (matcher.group(4) != null) {
                    Integer month = GERMAN_MONTHS.get(matcher.group(4).toLowerCase());
                    
                    if (month != null) {
                        return LocalDate.of(Year.now().getValue(), month, day);
                    }
                }
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalArgumentException("Ungültiges Datumsformat.");
            }
        }

        throw new IllegalArgumentException("Datum nicht gefunden.");
    }

    /** Prüft ob Text ein Datums-Muster enthält. */
    public boolean containsDate(String text) {
        return text != null && DATE_PATTERN.matcher(text).find();
    }
}