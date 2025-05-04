package auth;

import java.util.Map;
import java.util.Scanner;

import models.Passenger;
import utils.SecurityUtil;
import utils.UserUtils;

public class Login {
    private static final String USER_FILE = "data/users.txt";
    private Scanner scanner;

    public Login(Scanner scanner) {
        this.scanner = scanner;
    }

    public Passenger login() {
        ForgotPassword forgot = new ForgotPassword(scanner);

        System.out.println("=== Login ===");
        System.out.println("Enter email:");
        String email = scanner.nextLine();

        if (!UserUtils.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return null;
        }
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        if (!UserUtils.isValidPassword(password)) {
            System.out.println("Password must be at least 8 characters long.");
            return null;
        }
        String hashPassword = SecurityUtil.hashPassword(password);
        if (hashPassword == null) {
            System.out.println("Error hashing password.");
            return null;
        }

        Map<Passenger, String> users = UserUtils.loadUsers(USER_FILE);
        for (Map.Entry<Passenger, String> entry : users.entrySet()) {
            Passenger passenger = entry.getKey();
            String storedPassword = entry.getValue();

            if (passenger.getEmail().equals(email) && storedPassword.equals(hashPassword)) {
                System.out.println("Welcome, " + passenger.getFirstName() + " " + passenger.getLastName() + "!");
                return passenger;
            }
        }

        System.out.println("\nLogin failed.");
        System.out.println("1. Retry Login");
        System.out.println("2. Forgot Password");
        System.out.println("3. Back to Main Menu");

        System.out.print("Enter choice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                return login();
            case "2":
                forgot.forgot();
                break;
            case "3":
                return null;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
                return null;
        }
        return null;
    }
}
