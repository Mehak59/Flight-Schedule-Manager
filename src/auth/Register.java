package auth;

import java.util.Map;
import java.util.Scanner;
import models.Passenger;
import utils.UserUtils;

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
        Map<Passenger, String> users = UserUtils.loadUsers(USER_FILE);

        if (UserUtils.userExists(users, passenger.getEmail())) {
            System.out.println("User already exists.");
            return false;
        }
        return UserUtils.registerUserToFile(passenger, password, USER_FILE);
    }

    public void register() {
        System.out.println("=== Register ===");
        System.out.print("Enter Passenger ID: ");
        String passengerId = scanner.nextLine();
        if (passengerId.isEmpty()) {
            System.out.println("Passenger ID cannot be empty.");
            return;
        }
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
        if (!UserUtils.isValidDateOfBirth(dateOfBirth)) {
            System.out.println("Date of birth must be in YYYY-MM-DD format.");
            return;
        }
        System.out.print("Enter phone number: ");
        String phoneNo = scanner.nextLine();
        if (phoneNo.isEmpty()) {
            System.out.println("Phone number cannot be empty.");
            return;
        }

        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        if (!UserUtils.isValidEmail(email)) {
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
        if (!UserUtils.isValidPassword(password)) {
            System.out.println("Password must be at least 8 characters long.");
            return;
        }

        Passenger passenger = new Passenger(passengerId, firstName, lastName, dateOfBirth, phoneNo, email, nationality);
        boolean registrationSuccess = registerUserInFile(passenger, password);

        if (registrationSuccess) {
            System.out.println("User registered successfully.");
        } else {
            System.out.println("Failed to register user.");
        }
    }
}