package auth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class ForgotPassword {
    private static final String USER_FILE = "data/users.txt";
    private Scanner scanner;

    public ForgotPassword(Scanner scanner) {
        this.scanner = scanner;
    }

    public void forgot() {
        System.out.println("=== Forgot Password ===");
        System.out.print("Enter your registered email: ");
        String email = scanner.nextLine();

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            System.out.println("Invalid email format.");
            return;
        }

        try {
            if (!emailExists(email)) {
                System.out.println("Email not found. Please register first.");
                return;
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
            return;
        }

        System.out.print("Enter new password: ");
        String password = scanner.nextLine();
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long.");
            return;
        }

        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            System.out.println("Error hashing password.");
            return;
        }

        try {
            if (updatePassword(email, hashedPassword)) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("Failed to update password.");
            }
        } catch (IOException e) {
            System.out.println("Error updating user file: " + e.getMessage());
        }
    }

    private boolean emailExists(String email) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    String storedEmail = parts[5];
                    if (storedEmail.equals(email)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean updatePassword(String email, String newHashedPassword) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    String storedEmail = parts[5];
                    if (storedEmail.equals(email)) {
                        parts[7] = newHashedPassword;
                        updated = true;
                    }
                    line = String.join(",", parts);
                }
                fileContent.append(line).append(System.lineSeparator());
            }
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
                writer.write(fileContent.toString());
            }
        }

        return updated;
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
