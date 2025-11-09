package com.restaurant.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TimeExtractionService.
 */
class TimeExtractionServiceTest {

    private TimeExtractionService service;

    @BeforeEach
    void setUp() {
        service = new TimeExtractionService();
    }

    @ParameterizedTest
    @CsvSource({
        "'Hallo bitte für zwei Personen einen Tisch am 19.3. um 20:00 Uhr Vielen Dank Klaus Müller', 20:00",
        "'Sehr geehrte Damen Herren wir würden gern am 9. April 9:45 Uhr mit sechs Leuten zum Brunch kommen Mit freundlichen Grüßen Maria Meier', 09:45",
        "'Guten Tag einen Tisch für 8 Mann am 1.5. 9 Uhr abends Gruß Franz Schulze', 21:00",
        "'Reservierung um 18:30 Uhr bitte', 18:30",
        "'Tisch für 8 Uhr morgens', 08:00"
    })
    void testValidRequests(String text, String expectedTime) {
        assertEquals(LocalTime.parse(expectedTime), service.extractTime(text));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "keine Zeit hier",
        "am 15.5. um 25:00 Uhr",      // hour out of range
        "am 15.5. um 20:60 Uhr",      // minute out of range
        "am 15.5. 16 Uhr abends",     // invalid period conversion
        "am 15.5. 13 Uhr nachmittags" // invalid period conversion
    })
    void testBoundaries(String text) {
        assertThrows(IllegalArgumentException.class, () -> 
            service.extractTime(text));
    }
}