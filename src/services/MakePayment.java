package services;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MakePayment {

    public double getFlightPrice(int flightId, String flightClass, String flightsFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(flightsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 9) {
                    continue; 
                }
                int id = Integer.parseInt(parts[0].trim());
                if (id == flightId) {
                    switch (flightClass.toLowerCase()) {
                        case "economy":
                            return Double.parseDouble(parts[7].trim());
                        case "business":
                            return Double.parseDouble(parts[8].trim());
                        case "first":
                            return Double.parseDouble(parts[9].trim());
                        default:
                            return 0.0;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading flight prices: " + e.getMessage());
        }
        return 0.0;
    }

    public void displayBill(int numberOfPassengers, double pricePerPassenger) {
        double totalPrice = numberOfPassengers * pricePerPassenger;
        System.out.println("\n========== Booking Bill ==========");
        System.out.println("Number of Passengers: " + numberOfPassengers);
        System.out.printf("Price per Passenger: Rs. %.2f%n", pricePerPassenger);
        System.out.println("----------------------------------");
        System.out.printf("Total Price: Rs. %.2f%n", totalPrice);
        System.out.println("==================================\n");
    }

    public void displayDetailedBill(int numberOfPassengers, double pricePerPassenger, double layoverSurcharge) {
        double totalLayoverCharge = layoverSurcharge * numberOfPassengers;
        double totalPrice = (pricePerPassenger * numberOfPassengers) + totalLayoverCharge;
        System.out.println("\n========== Detailed Booking Bill ==========");
        System.out.println("Number of Passengers: " + numberOfPassengers);
        System.out.printf("Price per Passenger: Rs. %.2f%n", pricePerPassenger);
        System.out.printf("Layover Subcharge per Passenger: Rs. %.2f%n", layoverSurcharge);
        System.out.println("--------------------------------------------");
        System.out.printf("Total Layover Subcharge: Rs. %.2f%n", totalLayoverCharge);
        System.out.printf("Total Price: Rs. %.2f%n", totalPrice);
        System.out.println("============================================\n");
    }

    public boolean processPayment(Scanner scanner, double amount) {
        System.out.printf("Please pay Rs. %.2f to confirm your booking.%n", amount);
        System.out.print("Enter 'paid' once payment is done or 'cancel' to cancel: ");
        String input = scanner.nextLine().trim().toLowerCase();
        if ("paid".equals(input)) {
            System.out.println("Payment successful. Thank you!");
            return true;
        } else {
            System.out.println("Payment cancelled.");
            return false;
        }
    }
}
