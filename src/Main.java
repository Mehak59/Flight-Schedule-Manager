import java.util.Scanner;

import auth.Register;
import auth.Login;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Register register = new Register(scanner);
        Login login = new Login(scanner);
        boolean flag = true;
        Welcomemsg();
        while (flag) {
            System.out.println("=== Menu ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    register.register();
                    break;
                case "2":
                    login.login();
                    break;
                case "3":
                    System.out.println("Exiting from the program.Goodbye!");
                    flag=false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
            System.out.println();
        }
    }
    private static void Welcomemsg()
    {
      System.out.println("\n**************************");
      System.out.println("Welcome to the Flight Schedule Manager");
      System.out.println("Manage your flights, bookings, and check-ins with ease.");
      System.out.println("Please choose an option: Register, Login, or Exit.");
      System.out.println("**************************\n");
    }
}
