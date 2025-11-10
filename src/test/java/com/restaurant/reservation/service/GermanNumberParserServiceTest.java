package com.restaurant.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/** Tests für GermanNumberParserService. */
class GermanNumberParserServiceTest {

    private GermanNumberParserService service;

    @BeforeEach
    void setUp() {
        service = new GermanNumberParserService();
    }

    @ParameterizedTest
    @CsvSource({
        "eins, 1",
        "zwei, 2", 
        "zwölf, 12",
        "zwanzig, 20",
        "neunzig, 90"
    })
    void testParseBasicNumbers(String input, int expected) {
        assertEquals(expected, service.parseNumberString(input));
    }

    @ParameterizedTest
    @CsvSource({
        "einundzwanzig, 21",
        "fünfundvierzig, 45",
        "neunundachtzig, 89"
    })
    void testParseCompoundNumbers(String input, int expected) {
        assertEquals(expected, service.parseNumberString(input));
    }

    @ParameterizedTest
    @CsvSource({
        "5, 5",
        "25, 25",
        "99, 99"
    })
    void testParseNumericStrings(String input, int expected) {
        assertEquals(expected, service.parseNumberString(input));
    }

    @Test
    void testInvalidInputs() {
        assertNull(service.parseNumberString("invalid"));
        assertNull(service.parseNumberString(""));
        assertNull(service.parseNumberString(null));
        assertNull(service.parseNumberString("hundert"));
    }
}