import java.util.Scanner;
import services.FlightService;
import auth.Register;
import models.Passenger;
import services.ProfileSettings;
import auth.Login;

public class Main {
    private static Passenger loggedInPassenger = null;

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
                loggedInPassenger = login.login();
                    showServices(scanner);
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
            System.out.println();
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
                    profileSettings(scanner);
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
        System.out.print("Do you want to get details of a flight by entering its Flight ID? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.equals("yes") || response.equals("y")) {
            System.out.print("Enter Flight ID: ");
            String flightIdStr = scanner.nextLine();
            int flightId;
            try {
                flightId = Integer.parseInt(flightIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Flight ID format.");
                return;
            }
        //     models.Flight flight = flightService.displayFlightDetails(flight, source, flight);

        // if (flight != null) {
        //     System.out.println("Flight Details:");
        //     System.out.println(flight.toString());
        // } else {
        //     System.out.println("Flight with ID " + flightId + " not found.");
        // }
    }
}

    private static void profileSettings(Scanner scanner) {
        ProfileSettings profileSettings = new ProfileSettings(scanner);
        boolean profileMenuFlag = true;
        while (profileMenuFlag) {
            System.out.println();
            System.out.println("=== Profile Settings ===");
            System.out.println("1. Show my details");
            System.out.println("2. Update my details");
            System.out.println("3. Log out");
            System.out.println("4. Back to services menu");
            System.out.print("Enter your choice: ");
            String profileChoice = scanner.nextLine();
            switch (profileChoice) {
                case "1":
                    if (loggedInPassenger != null) {
                        profileSettings.displayUserdetails(loggedInPassenger);
                    } 
                    else {
                        System.out.println("No user logged in.");
                    }
                    break;
                case "2":
                    if (loggedInPassenger != null) {
                        profileSettings.updateUserDetails(loggedInPassenger);
                    } 
                    else {
                        System.out.println("No user logged in.");
                    }
                    break;
                case "3":
                    System.out.println("Logging out...");
                    loggedInPassenger = null;
                    profileMenuFlag = false;
                    break;
                case "4":
                    profileMenuFlag = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
            }
            System.out.println();
        }
    }
}
