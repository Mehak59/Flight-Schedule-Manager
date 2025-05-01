package auth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import models.Passenger;

public class Register {
    private static final String USER_FILE = "data/users.txt";
    private Scanner scanner;

    public Register(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean registerUserInFile(Passenger passenger, String password) {
        if (passenger == null || password == null || password.isEmpty()) {
            System.out.println("Passenger details and password cannot be empty.");
            return false;
        }
        if (userExists(passenger.getEmail())) {
            System.out.println("User already exists.");
            return false;
        }
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
                StringBuilder sb = new StringBuilder();
                sb.append(passenger.getPassengerId()).append(",");
                sb.append(passenger.getFirstName()).append(",");
                sb.append(passenger.getLastName()).append(",");
                sb.append(passenger.getDateOfBirth()).append(",");
                sb.append(passenger.getPhoneNo()).append(",");
                sb.append(passenger.getEmail()).append(",");
                sb.append(passenger.getNationality()).append(",");
                sb.append(hashPassword(password));
                writer.write(sb.toString());
                writer.newLine();
                System.out.println("User registered successfully.");
                return true;
            }
        } catch (IOException e) {
            System.out.println("Error writing to user file: " + e.getMessage());
            return false;
        }

    }

    public void register() {

        System.out.print("Enter Passenger ID : ");
        String passengerId = scanner.nextLine();
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        if (firstName.isEmpty()) {
            System.out.println("First name cannot be empty.");
            return;
        }
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        if (lastName.isEmpty()) {
            System.out.println("Last name cannot be empty.");
            return;
        }
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine();
        if (!dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Date of birth must be in YYYY-MM-DD format.");
            return;
        }
        System.out.print("Enter phone number: ");
        String phoneNo = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            System.out.println("Invalid email format.");
            return;
        }
        System.out.print("Enter nationality: ");
        String nationality = scanner.nextLine();
        if (nationality.isEmpty()) {
            System.out.println("Nationality cannot be empty.");
            return;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return;
        }

        Passenger passenger = new Passenger(passengerId, firstName, lastName, dateOfBirth, phoneNo, email,
                nationality);
        registerUserInFile(passenger, password);

    }

    public boolean userExists(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 8) {
                    String storedEmail = details[5];
                    if (storedEmail.equals(email)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }
}
