package services;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import models.Passenger;

public class Bookticket {

    // Removed duplicate saveBookingWithFlightData method to fix duplicate method error and syntax error

    public boolean confirmBooking(Scanner scanner) {
        System.out.print("Are you sure with your booking? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }

    public LinkedList<Passenger> collectPassengerDetails(int numberOfTickets, Scanner scanner, String passengerId) {
        LinkedList<Passenger> passengers = new LinkedList<>();
        for (int i = 1; i <= numberOfTickets; i++) {
            System.out.println("Passenger " + i + ":");
            String firstName = "";
            while (firstName.isEmpty()) {
                System.out.print("- Enter First Name: ");
                firstName = scanner.nextLine().trim();
                if (firstName.isEmpty()) {
                    System.out.println("First Name is required.");
                }
            }
            String lastName = "";
            while (lastName.isEmpty()) {
                System.out.print("- Enter Last Name: ");
                lastName = scanner.nextLine().trim();
                if (lastName.isEmpty()) {
                    System.out.println("Last Name is required.");
                }
            }
            String dob = "";
            while (true) {
                System.out.print("- Enter Date of Birth (YYYY-MM-DD): ");
                dob = scanner.nextLine().trim();
                if (dob.isEmpty()) {
                    System.out.println("Date of Birth is required.");
                } else if (!utils.UserUtils.isValidDateOfBirth(dob)) {
                    System.out.println("Invalid Date of Birth format. Please use YYYY-MM-DD.");
                } else {
                    break;
                }
            }
            String nationality = "";
            while (nationality.isEmpty()) {
                System.out.print("- Enter Nationality: ");
                nationality = scanner.nextLine().trim();
                if (nationality.isEmpty()) {
                    System.out.println("Nationality is required.");
                }
            }
            String phoneNo = "";
            while (true) {
                System.out.print("- Enter Phone Number: ");
                phoneNo = scanner.nextLine().trim();
                if (phoneNo.isEmpty()) {
                    System.out.println("Phone Number is required.");
                } else if (!phoneNo.matches("^\\+?[0-9]{7,15}$")) {
                    System.out.println("Invalid Phone Number format. Please enter digits only, optionally starting with '+'.");
                } else {
                    break;
                }
            }
            String email = "";
            while (true) {
                System.out.print("- Enter Email Address: ");
                email = scanner.nextLine().trim();
                if (email.isEmpty()) {
                    System.out.println("Email Address is required.");
                } else if (!utils.UserUtils.isValidEmail(email)) {
                    System.out.println("Invalid Email format.");
                } else {
                    break;
                }
            }
            System.out.print("- Enter Seat Preference (w for window, a for aisle, m for middle): ");
            String seatPref = scanner.nextLine().trim();

            Passenger passenger = new Passenger(passengerId, firstName, lastName, dob, phoneNo, email, nationality);
            // Optionally, you can store seatPref somewhere if needed

            passengers.add(passenger);
        }
        return passengers;
    }

    public void saveBookings(LinkedList<Passenger> passengers, String bookingSummary, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            for (Passenger p : passengers) {
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s\n",
                        p.getPassengerId(),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getDateOfBirth(),
                        p.getNationality(),
                        p.getPhoneNo(),
                        p.getEmail(),
                        bookingSummary);
                writer.write(line);
            }
            System.out.println("Bookings saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    public void saveBookingWithFlightData(models.Booking booking, String filePath) {
        try (java.io.FileWriter writer = new java.io.FileWriter(filePath, true)) {
            String line = String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%.2f,%s,%s,%d,%d\n",
                    booking.getBookingID(),
                    booking.getPassengerId(),
                    booking.getEmail(),  // Added username (email) here after passengerId
                    booking.getFirstName(),
                    booking.getLastName(),
                    booking.getDateOfBirth(),
                    booking.getPhoneNo(),
                    booking.getNationality(),
                    booking.getBookingDate(),
                    booking.getTravelClass(),
                    booking.getCheckInStatus(),
                    booking.getPrice(),
                    booking.getPaymentStatus(),
                    booking.getCheckInTime(),
                    booking.getSeatNumber(),
                    booking.getFlightID());
            writer.write(line);
            System.out.println("Booking with flight data saved successfully");
        } catch (java.io.IOException e) {
            System.out.println("Error saving booking with flight data: " + e.getMessage());
        }
    }
}
