package services;

import java.util.LinkedList;
import models.Booking;

public class ManageBooking {
    private LinkedList<Booking> bookings;

    public ManageBooking() {
        bookings = new LinkedList<>();
    }

    public LinkedList<Booking> getBookingsByPassenger(String passengerId) {
        LinkedList<Booking> passengerBookings = new LinkedList<>();
        int passengerIDInt;
        try {
            passengerIDInt = Integer.parseInt(passengerId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid passenger ID format.");
            return passengerBookings;
        }
        for (Booking b : bookings) {
            if (b.getPassengerID() == passengerIDInt) {
                passengerBookings.add(b);
            }
        }
        return passengerBookings;
    }

    private static int bookingCounter = 1;

    public Object createBooking(int flightID, String passengerId, String bookingDate, int seatNumber, String travelClass, double price) {
        // Validate inputs
        if (flightID <= 0) {
            System.out.println("Invalid flight ID. Must be positive.");
            return null;
        }
        if (seatNumber <= 0) {
            System.out.println("Invalid seat number. Must be positive.");
            return null;
        }
        if (!travelClass.equalsIgnoreCase("Economy") && !travelClass.equalsIgnoreCase("Business")) {
            System.out.println("Invalid travel class. Must be 'Economy' or 'Business'.");
            return null;
        }
        if (price <= 0) {
            System.out.println("Invalid price. Must be positive.");
            return null;
        }
        // Validate bookingDate format YYYY-MM-DD
        try {
            java.time.LocalDate.parse(bookingDate);
        } catch (Exception e) {
            System.out.println("Invalid booking date format. Use YYYY-MM-DD.");
            return null;
        }

        int bookingID = bookingCounter++;
        int passengerIDInt;
        try {
            passengerIDInt = Integer.parseInt(passengerId);
        } catch (NumberFormatException e) {
            System.out.println("Invalid passenger ID format.");
            return null;
        }

        // Apply discount for first booking
        boolean isFirstBooking = true;
        for (Booking b : bookings) {
            if (b.getPassengerID() == passengerIDInt) {
                isFirstBooking = false;
                break;
            }
        }
        if (isFirstBooking) {
            double discount = 0.10; // 10% discount
            price = price * (1 - discount);
            System.out.println("Congratulations! A 10% discount has been applied to your first booking.");
        }

        Booking newBooking = new Booking(bookingID, flightID, passengerIDInt, bookingDate, seatNumber, travelClass, price, "Pending", "Not Checked In", null);
        bookings.add(newBooking);
        System.out.println("Booking created with ID: " + bookingID);
        return newBooking;
    }

    public boolean cancelBooking(int bookingID) {
        for (Booking b : bookings) {
            if (b.getBookingID() == bookingID) {
                bookings.remove(b);
                System.out.println("Booking with ID " + bookingID + " canceled.");
                return true;
            }
        }
        return false;
    }

    public boolean updatePaymentStatus(int bookingID, String status) {
        for (Booking b : bookings) {
            if (b.getBookingID() == bookingID) {
                b.setPaymentStatus(status);
                System.out.println("Payment status for booking ID " + bookingID + " updated to " + status + ".");
                return true;
            }
        }
        return false;
    }
}
