package services;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;

import models.Passenger;

public class ProfileSettings {
    private static final String USER_FILE="data/users.txt";
    private Scanner scanner;
    public ProfileSettings (Scanner scanner){
        this.scanner=scanner;
    }
    public void displayUserdetails(Passenger passenger) {
        System.out.println("----- Your Profile Details -----");
        System.out.println("Passenger ID: " + passenger.getPassengerId());
        System.out.println("First Name: " + passenger.getFirstName());
        System.out.println("Last Name: " + passenger.getLastName());
        System.out.println("Date of Birth: " + passenger.getDateOfBirth());
        System.out.println("Phone Number: " + passenger.getPhoneNo());
        System.out.println("Email: " + passenger.getEmail());
        System.out.println("Nationality: " + passenger.getNationality());
        System.out.println("-------------------------------");
    }

    public void updateUserDetails(Passenger passenger) {
        boolean updateFlag = true;
        while (updateFlag) {
            System.out.println("\n=== Update Profile Details ===");
            System.out.println("------------------------------");
            System.out.println("1. First Name");
            System.out.println("2. Last Name");
            System.out.println("3. Date of Birth");
            System.out.println("4. Phone Number");
            System.out.println("5. Email");
            System.out.println("6. Nationality");
            System.out.println("7. Back to Profile Settings");
            System.out.print("Select the detail to update: ");
            String updateChoice = scanner.nextLine();
            switch (updateChoice) {
                case "1":
                    System.out.print("Enter new first name: ");
                    String newFirstName = scanner.nextLine();
                    if (updateUserInFile(passenger, "firstName", newFirstName)) {
                        passenger.setFirstName(newFirstName);
                        System.out.println("First name updated successfully.");
                    }
                    break;
                case "2":
                    System.out.print("Enter new last name: ");
                    String newLastName = scanner.nextLine();
                    if (updateUserInFile(passenger, "lastName", newLastName)) {
                        passenger.setLastName(newLastName);
                        System.out.println("Last name updated successfully.");
                    }
                    break;
                case "3":
                    System.out.print("Enter new D.O.B: ");
                    String newDateOfBirth = scanner.nextLine();
                    if (updateUserInFile(passenger, "dateOfBirth", newDateOfBirth)) {
                        passenger.setDateOfBirth(newDateOfBirth);
                        System.out.println("Date of birth updated successfully.");
                    }
                    break;
                case "4":
                    System.out.print("Enter new Phone Number: ");
                    String newPhone = scanner.nextLine();
                    if (updateUserInFile(passenger, "phoneNo", newPhone)) {
                        passenger.setPhoneNo(newPhone);
                        System.out.println("Phone Number updated successfully.");
                    }
                    break;
                case "5":
                    System.out.print("Enter new Email: ");
                    String newEmail = scanner.nextLine();
                    if (updateUserInFile(passenger, "email", newEmail)) {
                        passenger.setEmail(newEmail);
                        System.out.println("Email updated successfully.");
                    }
                    break;
                case "6":
                    System.out.print("Enter new Nationality: ");
                    String newNationality = scanner.nextLine();
                    if (updateUserInFile(passenger, "nationality", newNationality)) {
                        passenger.setNationality(newNationality);
                        System.out.println("Nationality updated successfully.");
                    }
                    break;
                case "7":
                    updateFlag = false;
                    System.out.println("Returning to Profile Settings.");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
            System.out.println();
        }
    }

    private boolean updateUserInFile(Passenger passenger, String field, String newValue) {
        StringBuilder fileContent = new StringBuilder();
        boolean updated = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[0].equals(passenger.getPassengerId())) {
                    switch (field) {
                        case "firstName":
                            parts[1] = newValue;
                            break;
                        case "lastName":
                            parts[2] = newValue;
                            break;
                        case "dateOfBirth":
                            parts[3] = newValue;
                            break;
                        case "phoneNo":
                            parts[4] = newValue;
                            break;
                        case "email":
                            parts[5] = newValue;
                            break;
                        case "nationality":
                            parts[6] = newValue;
                            break;
                    }
                    updated = true;
                    line = String.join(",", parts);
                }
                fileContent.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
            return false;
        }
        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(USER_FILE))) {
                writer.write(fileContent.toString());
            } catch (IOException e) {
                System.out.println("Error writing user file: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("No matching user found to update.");
            return false;
        }
        return true;
    }
}
