package com.restaurant.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/** Tests für PeopleCountExtractionService. */
class PeopleCountExtractionServiceTest {

    private PeopleCountExtractionService service;

    @BeforeEach
    void setUp() {
        service = new PeopleCountExtractionService(new GermanNumberParserService());
    }

    @ParameterizedTest
    @CsvSource({
        "'Hallo bitte für zwei Personen einen Tisch am 19.3. um 20:00 Uhr Vielen Dank Klaus Müller', 2",
        "'Sehr geehrte Damen Herren wir würden gern am 9. April 9:45 Uhr mit sechs Leuten zum Brunch kommen Mit freundlichen Grüßen Maria Meier', 6", 
        "'Guten Tag einen Tisch für 8 Mann am 1.5. 9 Uhr abends Gruß Franz Schulze', 8",
        "'Reservierung für 4 Personen', 4",
        "'mit drei Leuten', 3"
    })
    void testValidRequests(String text, int expected) {
        assertEquals(expected, service.extractNumberOfPeople(text));
    }

    @ParameterizedTest
    @CsvSource({
        "keine Anzahl hier",
        "für 0 Personen",      // below minimum
        "für 100 Personen",    // above maximum
        "für 150 Leute"        // above maximum
    })
    void testBoundaries(String text) {
        assertThrows(IllegalArgumentException.class, () -> 
            service.extractNumberOfPeople(text));
    }
}