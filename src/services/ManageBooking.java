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

    public ManageBooking() {
        bookings = new LinkedList<>();
        flights = new HashMap<>();
        loadFlightsFromFile("data/flights.txt");
        loadBookingsFromFile("data/bookings.txt");
    }

    private void loadBookingsFromFile(String filePath) {
        System.out.println("Loading bookings from file: " + filePath);
        int bookingCounter = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Reading line: " + line);
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    try {
                        String passengerId = parts[0].trim();
                        String firstName = parts[1].trim();
                        String lastName = parts[2].trim();
                        String dateOfBirth = parts[3].trim();
                        String nationality = parts[4].trim();
                        String phoneNo = parts[5].trim();
                        String email = parts[6].trim();

                        StringBuilder flightInfoBuilder = new StringBuilder();
                        for (int i = 7; i < parts.length; i++) {
                            flightInfoBuilder.append(parts[i].trim());
                            if (i < parts.length - 1) {
                                flightInfoBuilder.append(",");
                            }
                        }
                        String flightInfo = flightInfoBuilder.toString();

                        int flightID = -1;
                        String travelClass = "Unknown";
                        int tickets = 1;

                        String[] flightParts = flightInfo.split(",");
                        for (String fp : flightParts) {
                            fp = fp.trim();
                            if (fp.startsWith("FlightID:")) {
                                try {
                                    flightID = Integer.parseInt(fp.substring(9).trim());
                                } catch (NumberFormatException e) {
                                    flightID = -1;
                                }
                            } else if (fp.startsWith("Class:")) {
                                travelClass = fp.substring(6).trim();
                            } else if (fp.startsWith("Tickets:")) {
                                try {
                                    tickets = Integer.parseInt(fp.substring(8).trim());
                                } catch (NumberFormatException e) {
                                    tickets = 1;
                                }
                            }
                        }

                        int bookingID = bookingCounter++;
                        String bookingDate = java.time.LocalDate.now().toString();
                        String checkInStatus = "N/A";
                        String paymentStatus = "Paid";  // Changed to Paid as per user feedback
                        String checkInTime = "N/A";
                        int seatNumber = 0;

                        double price = 0.0;
                        Flight flight = flights.get(flightID);
                        if (flight != null) {
                            switch (travelClass.toLowerCase()) {
                                case "economy":
                                    price = flight.getPriceEconomy() * tickets;
                                    break;
                                case "business":
                                    price = flight.getPriceBusiness() * tickets;
                                    break;
                                case "first":
                                    price = flight.getPriceFirst() * tickets;
                                    break;
                                default:
                                    price = 0.0;
                            }
                        }

                        Booking booking = new Booking(passengerId, firstName, lastName, dateOfBirth, phoneNo, email, nationality,
                                bookingID, flightID, bookingDate, seatNumber, travelClass, price, paymentStatus, checkInStatus, checkInTime);
                        bookings.add(booking);
                        System.out.println("Added booking for passengerId: " + passengerId);
                    } catch (Exception e) {
                        System.out.println("Error parsing booking line: " + e.getMessage());
                    }
                } else {
                    System.out.println("Skipping line due to insufficient parts: " + line);
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
                                duration, priceEconomy, priceBusiness, priceFirst, stops, status, checkInTime, layover, layoverTime);
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

    public void printRawBookings() {
        System.out.println("Your Bookings:");
        for (Booking b : bookings) {
            Flight flight = flights.get(b.getFlightID());
            System.out.println("===============================================");
            System.out.println("Booking ID: " + b.getBookingID() + " | Booking Date: " + b.getBookingDate());
            System.out.println("Name: " + b.getFirstName() + " " + b.getLastName());
            System.out.println("Passenger ID: " + b.getPassengerId());
            System.out.println("Travel Class: " + b.getTravelClass());
            System.out.println("Seat Number: " + b.getSeatNumber());
            System.out.println("Price: $" + String.format("%.2f", b.getPrice()));
            System.out.println("Payment Status: " + b.getPaymentStatus());
            System.out.println("Check-In Status: " + b.getCheckInStatus());
            System.out.println("Check-In Time: " + (b.getCheckInTime() != null ? b.getCheckInTime() : "N/A"));
            System.out.println("Flight Details:");
            if (flight != null) {
                System.out.println("  Flight ID: " + flight.getFlightId());
                System.out.println("  Date: " + flight.getFlightDate());
                System.out.println("  From: " + flight.getFrom());
                System.out.println("  To: " + flight.getTo());
                System.out.println("  Departure Time: " + flight.getDepartureTime());
                System.out.println("  Arrival Time: " + flight.getArrivalTime());
                System.out.println("  Duration: " + flight.getDuration() + " minutes");
                System.out.println("  Price Economy: $" + flight.getPriceEconomy());
                System.out.println("  Price Business: $" + flight.getPriceBusiness());
                System.out.println("  Price First: $" + flight.getPriceFirst());
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
            System.out.println("Price: $" + b.getPrice());
            System.out.println("Payment Status: " + b.getPaymentStatus());
            System.out.println("Check-In Status: " + b.getCheckInStatus());
            System.out.println("Check-In Time: " + (b.getCheckInTime() != null ? b.getCheckInTime() : "N/A"));
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
                    System.out.println("  Price Economy: $" + flight.getPriceEconomy());
                    System.out.println("  Price Business: $" + flight.getPriceBusiness());
                    System.out.println("  Price First: $" + flight.getPriceFirst());
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
                // Update bookings.txt file to remove the canceled booking
                try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("data/bookings.txt"))) {
                    for (Booking booking : bookings) {
                        StringBuilder flightInfoBuilder = new StringBuilder();
                        flightInfoBuilder.append("FlightID: ").append(booking.getFlightID());
                        flightInfoBuilder.append(", Class: ").append(booking.getTravelClass());
                        flightInfoBuilder.append(", Tickets: 1"); // Assuming 1 ticket per booking for simplicity
                        String line = String.join(", ",
                                booking.getPassengerId(),
                                booking.getFirstName(),
                                booking.getLastName(),
                                booking.getDateOfBirth(),
                                booking.getNationality(),
                                booking.getPhoneNo(),
                                booking.getEmail(),
                                flightInfoBuilder.toString()
                        );
                        writer.write(line);
                        writer.newLine();
                    }
                } catch (Exception e) {
                    System.out.println("Error updating bookings file: " + e.getMessage());
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
