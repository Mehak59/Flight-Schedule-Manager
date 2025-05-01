package auth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import models.Passenger;

public class Login {
    private static final String USER_FILE = "data/users.txt";
    private Scanner scanner;

    public Login(Scanner scanner) {
        this.scanner = scanner;
    }

    public Passenger loginUser(String email, String password) {

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            System.out.println("Email and password cannot be empty.");
            return null;
        }
        String hashedInputPassword = hashPassword(password);
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    String storedEmail = parts[5];
                    String storedPassword = parts[7];
                    if (storedEmail.equals(email) && storedPassword.equals(hashedInputPassword)) {
                        System.out.println("Login successful.");
                        String passengerId = parts[0];
                        String firstName = parts[1];
                        String lastName = parts[2];
                        String dateOfBirth = parts[3];
                        String phoneNo = parts[4];
                        String nationality = parts[6];
                        return new Passenger(passengerId, firstName, lastName, dateOfBirth, phoneNo, storedEmail,
                                nationality);
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
            return null;
        }
        return null;
    }

    public void login() {
        ForgotPassword forgot = new ForgotPassword(scanner);
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            System.out.println("Invalid email format.");
            return;
        }
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return;
        }
        Passenger logPassenger = loginUser(email, password);
        if (logPassenger != null) {
            System.out.println("Welcome, " + logPassenger.getFirstName() + " " + logPassenger.getLastName() + "!");
        } else {
            System.out.println("\nLogin failed.");
            System.out.println("1. Retry Login");
            System.out.println("2. Forgot Password");
            System.out.println("3. Back to Main Menu");

            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    login();
                    break;
                case "2":
                    forgot.forgot();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Returning to main menu.");
                    return;
            }
        }
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