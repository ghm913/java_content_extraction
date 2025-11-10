package com.restaurant.reservation.service;

import com.restaurant.reservation.model.Reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    
    @Test
    void testValidRequests() {
        String input1 = "Hallo, bitte für zwei Personen einen Tisch am 19.3. um 20:00 Uhr, Vielen Dank Klaus Müller";
        Reservation res1 = service.extractReservationInfo(input1);
        assertEquals("Klaus Müller", res1.getCustomerName());
        assertEquals(LocalDate.of(Year.now().getValue(), 3, 19), res1.getDate());
        assertEquals(LocalTime.of(20, 0), res1.getTime());
        assertEquals(2, res1.getNumberOfPeople());
        
        String input2 = "Sehr geehrte Damen Herren, wir würden gern am 9. April 9:45 Uhr mit sechs Leuten zum Brunch kommen, Mit freundlichen Grüßen Maria Meier";
        Reservation res2 = service.extractReservationInfo(input2);
        assertEquals("Maria Meier", res2.getCustomerName());
        assertEquals(LocalDate.of(Year.now().getValue(), 4, 9), res2.getDate());
        assertEquals(LocalTime.of(9, 45), res2.getTime());
        assertEquals(6, res2.getNumberOfPeople());
        
        String input3 = "Guten Tag, einen Tisch für 8 Mann am 1.5. 9 Uhr abends, Gruß Franz Schulze";
        Reservation res3 = service.extractReservationInfo(input3);
        assertEquals("Franz Schulze", res3.getCustomerName());
        assertEquals(LocalDate.of(Year.now().getValue(), 5, 1), res3.getDate());
        assertEquals(LocalTime.of(21, 0), res3.getTime());
        assertEquals(8, res3.getNumberOfPeople());
        
        // Additional casual format
        String input4 = "Ich bin Anna Schmidt, bitte 4 Personen am 15.6. um 19:00 Uhr";
        Reservation res4 = service.extractReservationInfo(input4);
        assertEquals("Anna Schmidt", res4.getCustomerName());
        assertEquals(LocalDate.of(Year.now().getValue(), 6, 15), res4.getDate());
        assertEquals(LocalTime.of(19, 0), res4.getTime());
        assertEquals(4, res4.getNumberOfPeople());
    }
    
    @Test
    void testBoundaries() {
        assertThrows(IllegalArgumentException.class, () -> 
            service.extractReservationInfo(null));
        assertThrows(IllegalArgumentException.class, () -> 
            service.extractReservationInfo(""));
        
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> 
            service.extractReservationInfo("Reservierung für 4 Personen am 15.5. um 20:00 Uhr"));
        assertTrue(ex.getMessage().contains("Incomplete reservation information"));
        
        assertThrows(IllegalArgumentException.class, () -> 
            service.extractReservationInfo("Reservierung für 4 Personen am 32.5. um 20:00 Uhr, vG Klaus Müller"));
    }
}