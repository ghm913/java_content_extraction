package com.restaurant.reservation.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Service zur Extraktion der Personenanzahl aus deutschem Text. */
public class PeopleCountExtractionService {

    // Unterstützte Formate:
    // 1. Numerisch mit Präposition: "für 4 Personen", "mit 8 Leuten"
    // 2. Numerisch ohne Präposition: "4 Personen", "8 Leute"
    // 3. Zahlwörter mit Präposition: "für zwei Personen", "mit acht Leuten"
    // 4. Zahlwörter ohne Präposition: "zwei Personen", "acht Menschen"
    // 5. Nur Zahl mit Präposition: "für 2" (nur mit "für"/"mit")

    private static final Pattern PEOPLE_PATTERN = Pattern.compile(
        // Pattern 1: Mit Präposition "für" oder "mit"
        "(?:für|mit)\\s+" +
        "(\\d+|[a-zäöüß]+)" +  // Anzahl (Zahl oder Wort)
        "(?:" +
            "\\s+(?:personen|leuten?|mann|menschen)" +  // mit explizitem Personenwort
            "|" +
            "(?=\\s|$)(?!\\s*(?:uhr|morgens|vormittags|mittags|nachmittags|abends|pm|am|p\\.m\\.|a\\.m\\.))" +  // ohne Wort, aber nicht vor Zeitangabe
        ")" +
        "|" +
        // Pattern 2: Ohne Präposition (muss Personenwort folgen)
        "(\\d+|[a-zäöüß]+)" +  // Anzahl (Zahl oder Wort)
        "(?!\\s*(?:uhr|morgens|vormittags|mittags|nachmittags|abends|pm|am|p\\.m\\.|a\\.m\\.))" +  // nicht vor Zeitangabe
        "\\s+(?:personen|leuten?|mann|menschen)",  // muss Personenwort folgen
        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    private final GermanNumberParserService germanNumberParser;

    public PeopleCountExtractionService(GermanNumberParserService germanNumberParser) {
        this.germanNumberParser = germanNumberParser;
    }

    /** Extrahiert Personenanzahl aus Text. */
    public int extractNumberOfPeople(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text darf nicht leer sein");
        }

        Matcher matcher = PEOPLE_PATTERN.matcher(text);
        while (matcher.find()) {
            String countText = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            try {
                int count = Integer.parseInt(countText);
                if (count < 1 || count > 99) {
                    throw new IllegalArgumentException("Number of people must be between 1 and 99");
                }
                return count;
            } catch (NumberFormatException e) {
                Integer count = germanNumberParser.parseNumberString(countText.toLowerCase());
                if (count != null) {
                    if (count < 1 || count > 99) {
                        throw new IllegalArgumentException("Number of people must be between 1 and 99");
                    }
                    return count;
                }
            }
        }

        throw new IllegalArgumentException("Personenanzahl nicht gefunden");
    }

    /** Prüft ob Text ein Personenanzahl-Muster enthält. */
    public boolean containsPeopleCount(String text) {
        return text != null && PEOPLE_PATTERN.matcher(text).find();
    }
}