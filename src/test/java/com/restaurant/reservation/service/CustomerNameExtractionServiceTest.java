package com.restaurant.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CustomerNameExtractionService.
 */
class CustomerNameExtractionServiceTest {

    private CustomerNameExtractionService service;

    @BeforeEach
    void setUp() {
        service = new CustomerNameExtractionService();
    }

    @ParameterizedTest
    @CsvSource({
        "Hallo bitte für zwei Personen einen Tisch am 19.3. um 20:00 Uhr Vielen Dank Klaus Müller, Klaus Müller",
        "Sehr geehrte Damen Herren wir würden gern am 9. April 9:45 Uhr mit sechs Leuten zum Brunch kommen Mit freundlichen Grüßen Maria Meier, Maria Meier",
        "Guten Tag einen Tisch für 8 Mann am 1.5. 9 Uhr abends Gruß Franz Schulze, Franz Schulze",
        "Bitte einen Tisch für 2 Personen am 19.3. vG Klaus Müller, Klaus Müller",
        "Reservierung für 6 Leute am 9. April mfG Maria Meier, Maria Meier",
        "Ich bin Franz Schulze und möchte reservieren, Franz Schulze"
    })
    void testValidRequests(String text, String expectedName) {
        assertEquals(expectedName, service.extractCustomerName(text));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "keine Namen hier",
        "",
        "nur ein Name",
        "123 456"
    })
    void testBoundaries(String text) {
        assertThrows(IllegalArgumentException.class, () -> 
            service.extractCustomerName(text));
    }
}