import java.util.Scanner;

import auth.Register;
import auth.Login;
import services.FlightService;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Register register = new Register(scanner);
        Login login = new Login(scanner);
        boolean flag = true;
        welcomeMessage();
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
            showServices(scanner);
        }
    }

    private static void welcomeMessage()
    {
      System.out.println("\n**************************");
      System.out.println("Welcome to the Flight Schedule Manager");
      System.out.println("Manage your flights, bookings, and check-ins with ease.");
      System.out.println("Please choose an option: Register, Login, or Exit.");
      System.out.println("**************************\n");
    }

    private static void showServices(Scanner scanner) {
        boolean keepRunning = true;
        FlightService flightService = new FlightService();
        while (keepRunning) {
            System.out.println("=== Available Services ===");
            System.out.println("1. View Flights");
            System.out.println("2. Manage Bookings");
            System.out.println("3. Check-in");
            System.out.println("4. Notifications");
            System.out.println("5. Profile Settings");
            System.out.println("6. Go Back");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewFlights(scanner, flightService);
                    break;
                case "2":
                   // manageBookings();
                    break;
                case "3":
                   // checkIn();
                    break;
                case "4":
                   // notifications();
                    break;
                case "5":
                    //profileSettings();
                    break;
                case "6":
                    keepRunning = false;
                    break;
                default:
                   System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
            System.out.println();
        }
    }

    private static void viewFlights(Scanner scanner, FlightService flightService) {
        System.out.print("Enter source: ");
        String source = scanner.nextLine();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        java.time.LocalDate date;
        try {
            date = java.time.LocalDate.parse(dateStr);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }
        flightService.displayAvailableFlights(source, destination, date);
    }
}
