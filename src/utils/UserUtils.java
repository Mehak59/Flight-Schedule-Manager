package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import models.Passenger;

public class UserUtils {
    public static Map<Passenger, String> loadUsers(String filename) {
        Map<Passenger, String> users = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    String passengerId = parts[0];
                    String firstName = parts[1];
                    String lastName = parts[2];
                    String dateOfBirth = parts[3];
                    String phoneNo = parts[4];
                    String email = parts[5];
                    String nationality = parts[6];
                    String hashedPassword = parts[7];

                    Passenger passenger = new Passenger(passengerId, firstName, lastName, dateOfBirth, phoneNo, email,
                            nationality);

                    users.put(passenger, hashedPassword);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + filename);
            e.printStackTrace();
        }
        return users;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    public static boolean isValidDateOfBirth(String dateOfBirth) {
        return dateOfBirth != null && dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    public static boolean userExists(Map<Passenger, String> users, String email, String passengerId) {
        for (Passenger passenger : users.keySet()) {
            if (passenger.getEmail().equals(email) || passenger.getPassengerId().equals(passengerId)) {
                return true;
            }

        }
        return false;
    }

    public static boolean registerUserToFile(Passenger passenger, String password, String filename) {
        if (passenger == null || password == null || password.isEmpty()) {
            System.out.println("Invalid passenger details or password.");
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            String hashedPassword = SecurityUtil.hashPassword(password);

            if (hashedPassword == null) {
                System.out.println("Error hashing the password.");
                return false;
            }
            String userData = String.join(",",
                    passenger.getPassengerId(),
                    passenger.getFirstName(),
                    passenger.getLastName(),
                    passenger.getDateOfBirth(),
                    passenger.getPhoneNo(),
                    passenger.getEmail(),
                    passenger.getNationality(),
                    hashedPassword);
            writer.write(userData);
            writer.newLine();
            return true;

        } catch (IOException e) {
            System.out.println("Error writing to user file: " + e.getMessage());
            return false;
        }
    }

    public static boolean updatePassword(Map<Passenger, String> users, Passenger passenger, String newPassword,
            String filename) {
        if (passenger == null || newPassword == null || newPassword.isEmpty()) {
            System.out.println("Invalid user details or password.");
            return false;
        }
        String newHashedPassword = SecurityUtil.hashPassword(newPassword);
        if (newHashedPassword == null) {
            System.out.println("Error hashing the new password.");
            return false;
        }
        users.put(passenger, newHashedPassword);
        return saveUsersToFile(users, filename);
    }

    private static boolean saveUsersToFile(Map<Passenger, String> users, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<Passenger, String> entry : users.entrySet()) {
                Passenger passenger = entry.getKey();
                String hashedPassword = entry.getValue();
                String userData = String.join(",",
                        passenger.getPassengerId(),
                        passenger.getFirstName(),
                        passenger.getLastName(),
                        passenger.getDateOfBirth(),
                        passenger.getPhoneNo(),
                        passenger.getEmail(),
                        passenger.getNationality(),
                        hashedPassword);
                writer.write(userData);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving users to file: " + e.getMessage());
            return false;
        }
    }

    public static Passenger findUserByEmail(Map<Passenger, String> users, String email) {
        for (Passenger passenger : users.keySet()) {
            if (passenger.getEmail().equals(email)) {
                return passenger;
            }
        }
        return null;
    }

}