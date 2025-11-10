package com.restaurant.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

/** Tests für DateExtractionService. */
class DateExtractionServiceTest {

    private DateExtractionService service;

    @BeforeEach
    void setUp() {
        service = new DateExtractionService();
    }

    @ParameterizedTest
    @CsvSource({
        "'Hallo bitte für zwei Personen einen Tisch am 19.3. um 20:00 Uhr Vielen Dank Klaus Müller', 3, 19",
        "'Sehr geehrte Damen Herren wir würden gern am 9. April 9:45 Uhr mit sechs Leuten zum Brunch kommen Mit freundlichen Grüßen Maria Meier', 4, 9",
        "'Guten Tag einen Tisch für 8 Mann am 1.5. 9 Uhr abends Gruß Franz Schulze', 5, 1",
        "'Reservierung am 15.3.', 3, 15",
        "'Tisch für morgen am 20. März bitte', 3, 20"
    })
    void testValidRequests(String text, int month, int day) {
        LocalDate expected = LocalDate.of(Year.now().getValue(), month, day);
        assertEquals(expected, service.extractDate(text));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "keine Datum hier",
        "am 32.5. um 20:00 Uhr",     // day out of range
        "am 15.13. um 20:00 Uhr",    // month out of range
        "am 29.2.2025 um 20:00 Uhr", // Feb 29 in non-leap year
        "am 15.5.1999 um 20:00 Uhr"  // year out of range
    })
    void testBoundaries(String text) {
        assertThrows(IllegalArgumentException.class, () -> 
            service.extractDate(text));
    }
}