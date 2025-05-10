package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import models.Booking;
import models.Flight;

public class ManageBooking {
    private LinkedList<Booking> bookings;
    private Map<Integer, Flight> flights;
    private SeatService seatService;

    public ManageBooking() {
        bookings = new LinkedList<>();
        flights = new HashMap<>();
        seatService = new SeatService();
        loadFlightsFromFile("data/flights.txt");
        loadBookingsFromFile("data/bookings.txt");
    }

    private void loadBookingsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 14) {
                    try {
                        String passengerId = parts[0].trim();
                        String firstName = parts[1].trim();
                        String lastName = parts[2].trim();
                        String dateOfBirth = parts[3].trim();
                        String phoneNo = parts[4].trim();
                        String email = parts[5].trim();
                        String nationality = parts[6].trim();
                        int bookingId = Integer.parseInt(parts[7].trim());
                        int flightID = Integer.parseInt(parts[8].trim());
                        String bookingDate = parts[9].trim();
                        String seatNumber = parts[10].trim();
                        String travelClass = parts[11].trim();
                        String paymentStatus = parts[12].trim();
                        double price = Double.parseDouble(parts[13].trim());

                        Booking booking = new Booking(passengerId, firstName, lastName, dateOfBirth, phoneNo, email,
                                nationality,
                                bookingId, flightID, bookingDate, seatNumber, travelClass, price, paymentStatus);
                        bookings.add(booking);
                    } catch (Exception e) {
                        System.out.println("Error parsing booking line: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading bookings from file: " + e.getMessage());
        }
    }

    private void loadFlightsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 15) {
                    try {
                        int flightId = Integer.parseInt(parts[0].trim());
                        java.time.LocalDate date = java.time.LocalDate.parse(parts[1].trim());
                        String from = parts[2].trim();
                        String to = parts[3].trim();
                        String departureTime = parts[4].trim();
                        String arrivalTime = parts[5].trim();
                        int duration = Integer.parseInt(parts[6].trim());
                        double priceEconomy = Double.parseDouble(parts[7].trim());
                        double priceBusiness = Double.parseDouble(parts[8].trim());
                        double priceFirst = Double.parseDouble(parts[9].trim());
                        int stops = Integer.parseInt(parts[10].trim());
                        String status = parts[11].trim();
                        String checkInTime = parts[12].trim();
                        String layover = parts[13].trim();
                        String layoverTime = parts[14].trim();

                        Flight flight = new Flight(flightId, date, from, to, departureTime, arrivalTime,
                                duration, priceEconomy, priceBusiness, priceFirst, stops, status, checkInTime, layover,
                                layoverTime);
                        flights.put(flightId, flight);
                    } catch (Exception e) {
                        System.out.println("Error parsing flight line: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading flights from file: " + e.getMessage());
        }
    }

    public LinkedList<Booking> getBookingsByPassenger(String passengerId) {
        LinkedList<Booking> passengerBookings = new LinkedList<>();
        for (Booking b : bookings) {
            if (b.getPassengerId().equals(passengerId)) {
                passengerBookings.add(b);
            }
        }
        return passengerBookings;
    }

    public void showBookingDetails(String passengerId) {
        LinkedList<Booking> passengerBookings = getBookingsByPassenger(passengerId);
        if (passengerBookings.isEmpty()) {
            System.out.println("No bookings found for passenger ID: " + passengerId);
            return;
        }
        System.out.println("Booking details for passenger ID: " + passengerId);
        for (Booking b : passengerBookings) {
            System.out.println("===============================================");
            System.out.println("Booking ID: " + b.getBookingID() + " | Booking Date: " + b.getBookingDate());
            System.out.println("Name: " + b.getFirstName() + " " + b.getLastName());
            System.out.println("Email: " + b.getEmail());
            System.out.println("Phone No: " + b.getPhoneNo());
            System.out.println("Passenger ID: " + b.getPassengerId());
            System.out.println("Travel Class: " + b.getTravelClass());
            System.out.println("Seat Number: " + b.getSeatNumber());
            System.out.println("Price: Rs." + b.getPrice());
            System.out.println("Payment Status: " + b.getPaymentStatus());
            System.out.println("Flight Details:");
            Flight flight = flights.get(b.getFlightID());
            if (flight != null) {
                System.out.println("  Flight ID: " + flight.getFlightId());
                System.out.println("  Date: " + flight.getFlightDate());
                System.out.println("  Source: " + flight.getFrom());
                System.out.println("  Destination: " + flight.getTo());
                System.out.println("  Departure Time: " + flight.getDepartureTime());
                System.out.println("  Arrival Time: " + flight.getArrivalTime());
                System.out.println("  Duration: " + flight.getDuration() + " minutes");
                System.out.println("  Stops: " + flight.getStops());
                System.out.println("  Status: " + flight.getStatus());
                System.out.println("  Check-In Time: " + flight.getCheckInTime());
                System.out.println("  Layover: " + flight.getLayover());
                System.out.println("  Layover Time: " + flight.getLayoverTime());
            } else {
                System.out.println("  Flight details not found.");
            }
            System.out.println("===============================================");
        }
    }

    public boolean cancelBooking(int bookingID) {
        for (Booking b : bookings) {
            if (b.getBookingID() == bookingID) {
                bookings.remove(b);
                System.out.println("Booking with ID " + bookingID + " canceled.");
                try (java.io.BufferedWriter writer = new java.io.BufferedWriter(
                        new java.io.FileWriter("data/bookings.txt"))) {
                    for (Booking booking : bookings) {
                        String line = String.join(", ",
                                booking.getPassengerId(),
                                booking.getFirstName(),
                                booking.getLastName(),
                                booking.getDateOfBirth(),
                                booking.getPhoneNo(),
                                booking.getEmail(),
                                booking.getNationality(),
                                String.valueOf(booking.getFlightID()),
                                booking.getBookingDate(),
                                booking.getSeatNumber(),
                                booking.getTravelClass(),
                                booking.getPaymentStatus(),
                                String.valueOf(booking.getPrice()));
                        writer.write(line);
                        writer.newLine();
                    }
                } catch (Exception e) {
                    System.out.println("Error updating bookings file: " + e.getMessage());
                }

                try {
                    Map<Integer, char[][]> flightSeatMap = seatService.loadSeatBookings();
                    char[][] seatMap = flightSeatMap.get(b.getFlightID());
                    if (seatMap != null) {
                        String seatNumber = b.getSeatNumber();
                        if (seatNumber != null && !seatNumber.isEmpty()) {
                            int row = Integer.parseInt(seatNumber.substring(0, seatNumber.length() - 1)) - 1;
                            int col = seatNumber.charAt(seatNumber.length() - 1) - 'A';
                            if (row >= 0 && row < seatMap.length && col >= 0 && col < seatMap[0].length) {
                                seatMap[row][col] = 'O'; // Mark seat as unfilled
                                seatService.saveSeatBookings(b.getFlightID(), seatMap);
                                System.out.println(
                                        "Seat " + seatNumber + " marked as unfilled for flight " + b.getFlightID());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error updating seat bookings: " + e.getMessage());
                }

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

    public void displayBookingsForPassenger(String passengerId) {
        showBookingDetails(passengerId);
    }
}
