package auth;

import models.Passenger;
import utils.UserUtils;
import java.util.Map;
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

        if (!UserUtils.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return;
        }

        Map<Passenger, String> users = UserUtils.loadUsers(USER_FILE);
        Passenger user = UserUtils.findUserByEmail(users, email);
        if (user == null) {
            System.out.println("Email not found. Please register first.");
            return;
        }

        System.out.print("Enter new password: ");
        String password = scanner.nextLine();
        if (!UserUtils.isValidPassword(password)) {
            System.out.println("Password must be at least 8 characters long.");
            return;
        }

        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }
        if (UserUtils.updatePassword(users, user, password, USER_FILE)) {
            System.out.println("Password updated successfully.");
        } else {
            System.out.println("Failed to update password.");
        }
    }

}
