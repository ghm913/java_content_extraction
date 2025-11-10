package com.restaurant.reservation.model;

import java.time.LocalDate;
import java.time.LocalTime;

/** Datenmodell für eine Restaurant-Reservierung. */
public class Reservation {
    private final String customerName;
    private final LocalDate date;
    private final LocalTime time;
    private final int numberOfPeople;

    public Reservation(String customerName, LocalDate date, LocalTime time, int numberOfPeople) {
        this.customerName = customerName;
        this.date = date;
        this.time = time;
        this.numberOfPeople = numberOfPeople;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    @Override
    public String toString() {
        return String.format("(%s, %02d.%02d., %02d:%02d, %d)",
                customerName,
                date.getDayOfMonth(),
                date.getMonthValue(),
                time.getHour(),
                time.getMinute(),
                numberOfPeople);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Reservation that = (Reservation) obj;
        return numberOfPeople == that.numberOfPeople &&
                customerName.equals(that.customerName) &&
                date.equals(that.date) &&
                time.equals(that.time);
    }

    @Override
    public int hashCode() {
        int result = customerName.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + time.hashCode();
        result = 31 * result + numberOfPeople;
        return result;
    }
}