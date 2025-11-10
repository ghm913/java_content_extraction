package com.restaurant.reservation.service;

import com.restaurant.reservation.model.Reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReservationExtractorService.
 */
class ReservationExtractorServiceTest {
    
    private ReservationExtractorService service;
    
    @BeforeEach
    void setUp() {
        service = new ReservationExtractorService();
    }
    
    @ParameterizedTest
    @CsvSource({
        "'Hallo, bitte für zwei Personen einen Tisch am 19.3. um 20:00 Uhr, Vielen Dank Klaus Müller', Klaus Müller, 3, 19, 20, 0, 2",
        "'Sehr geehrte Damen Herren, wir würden gern am 9. April 9:45 Uhr mit sechs Leuten zum Brunch kommen, Mit freundlichen Grüßen Maria Meier', Maria Meier, 4, 9, 9, 45, 6",
        "'Guten Tag, einen Tisch für 8 Mann am 1.5. 9 Uhr abends, Gruß Franz Schulze', Franz Schulze, 5, 1, 21, 0, 8",
        "'Ich bin Anna Schmidt, bitte 4 Personen am 15.6. um 19:00 Uhr', Anna Schmidt, 6, 15, 19, 0, 4"
    })
    void testValidRequests(String input, String expectedName, int month, int day, int hour, int minute, int people) {
        Reservation res = service.extractReservationInfo(input);
        assertEquals(expectedName, res.getCustomerName());
        assertEquals(LocalDate.of(Year.now().getValue(), month, day), res.getDate());
        assertEquals(LocalTime.of(hour, minute), res.getTime());
        assertEquals(people, res.getNumberOfPeople());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "Reservierung für 4 Personen am 15.5. um 20:00 Uhr",
        "Reservierung für 4 Personen am 32.5. um 20:00 Uhr, vG Klaus Müller"
    })
    void testBoundaries(String text) {
        assertThrows(IllegalArgumentException.class, () -> 
            service.extractReservationInfo(text));
    }
}