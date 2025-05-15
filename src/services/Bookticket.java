package services;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import models.Booking;
import models.Passenger;

public class Bookticket {

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
                    System.out.println(
                            "Invalid Phone Number format. Please enter digits only, optionally starting with '+'.");
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

            Passenger passenger = new Passenger(passengerId, firstName, lastName, dob, phoneNo, email,
                    nationality);

            passengers.add(passenger);
        }
        return passengers;
    }

    public void saveBooking(Booking booking, String filePath) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            String line = String.format("%s,%s,%s,%s,%s,%s,%s,%d,%d,%s,%s,%s,%s,%.2f\n",
                    booking.getPassengerId(),
                    booking.getFirstName(),
                    booking.getLastName(),
                    booking.getDateOfBirth(),
                    booking.getPhoneNo(),
                    booking.getEmail(),
                    booking.getNationality(),
                    booking.getBookingID(),
                    booking.getFlightID(),
                    booking.getBookingDate(),
                    booking.getSeatNumber(),
                    booking.getTravelClass(),
                    booking.getPaymentStatus(),
                    booking.getPrice());
            writer.write(line);
            System.out.println("Booking saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving booking: " + e.getMessage());
        }
    }

    public void saveBookings(LinkedList<Passenger> passengers, String filePath, Queue<String> seatQueue,
            int flightID, String travelClass, double price) {
        for (Passenger passenger : passengers) {
            String seatNumber = seatQueue.poll();
            Booking booking = new Booking(
                    passenger.getPassengerId(),
                    passenger.getFirstName(),
                    passenger.getLastName(),
                    passenger.getDateOfBirth(),
                    passenger.getPhoneNo(),
                    passenger.getEmail(),
                    passenger.getNationality(),
                    generateBookingID(),
                    flightID,
                    LocalDate.now().toString(),
                    seatNumber,
                    travelClass,
                    price,
                    "Paid");

            saveBooking(booking, filePath);
        }
    }

    private int generateBookingID() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}
