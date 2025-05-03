import auth.Login;
import auth.Register;
import java.util.LinkedList;
import java.util.Scanner;
import models.Booking;
import models.Passenger;
import services.BookingService;
import services.FlightService;
import services.ProfileSettings;

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
                    manageBookings(scanner);
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

    private static void manageBookings(Scanner scanner) {
        BookingService bookingService = new BookingService();
        boolean bookingMenuFlag = true;

        while (bookingMenuFlag) {
            System.out.println("\n=== Manage Bookings ===");
            System.out.println("1. View My Bookings");
            System.out.println("2. Create New Booking");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Make Payment");
            System.out.println("5. Go Back");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewMyBookings(scanner, bookingService);
                    break;
                case "2":
                    createNewBooking(scanner, bookingService);
                    break;
                case "3":
                    cancelBooking(scanner, bookingService);
                    break;
                case "4":
                    makePayment(scanner, bookingService);
                    break;
                case "5":
                    bookingMenuFlag = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private static void viewMyBookings(Scanner scanner, BookingService bookingService) {
        if (loggedInPassenger == null) {
            System.out.println("You must be logged in to view bookings.");
            return;
        }
        LinkedList<Booking> bookings = bookingService.getBookingsByPassenger(loggedInPassenger.getPassengerId());
        if (bookings.isEmpty()) {
            System.out.println("You have no bookings.");
            return;
        }
        System.out.println("Your Bookings:");
        for (Booking b : bookings) {
            System.out.printf("Booking ID: %d, Flight ID: %d, Date: %s, Seat: %d, Class: %s, Price: %.2f, Payment Status: %s\n",
                    b.getBookingID(), b.getFlightID(), b.getBookingDate(), b.getSeatNumber(), b.getTravelClass(), b.getPrice(), b.getPaymentStatus());
        }
    }

    private static void createNewBooking(Scanner scanner, BookingService bookingService) {
        if (loggedInPassenger == null) {
            System.out.println("You must be logged in to create a booking.");
            return;
        }
        try {
            System.out.print("Enter Flight ID: ");
            int flightID = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Booking Date (YYYY-MM-DD): ");
            String bookingDate = scanner.nextLine();
            System.out.print("Enter Seat Number: ");
            int seatNumber = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Travel Class (Economy/Business): ");
            String travelClass = scanner.nextLine();
            System.out.print("Enter Price: ");
            double price = Double.parseDouble(scanner.nextLine());

            Object bookingObj = bookingService.createBooking(flightID, loggedInPassenger.getPassengerId(), bookingDate, seatNumber, travelClass, price);
            if (bookingObj != null) {
                System.out.println("Booking created successfully!");
            } else {
                System.out.println("Failed to create booking.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numeric values where required.");
        }
    }

    private static void cancelBooking(Scanner scanner, BookingService bookingService) {
        if (loggedInPassenger == null) {
            System.out.println("You must be logged in to cancel a booking.");
            return;
        }
        try {
            System.out.print("Enter Booking ID to cancel: ");
            int bookingID = Integer.parseInt(scanner.nextLine());
            boolean success = bookingService.cancelBooking(bookingID);
            if (success) {
                System.out.println("Booking canceled successfully.");
            } else {
                System.out.println("Booking not found or could not be canceled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a numeric Booking ID.");
        }
    }

    private static void makePayment(Scanner scanner, BookingService bookingService) {
        if (loggedInPassenger == null) {
            System.out.println("You must be logged in to make a payment.");
            return;
        }
        try {
            System.out.print("Enter Booking ID to make payment for: ");
            int bookingID = Integer.parseInt(scanner.nextLine());

            // Get current bookings of the user
            LinkedList<Booking> bookings = bookingService.getBookingsByPassenger(loggedInPassenger.getPassengerId());
            Booking targetBooking = null;
            for (Booking b : bookings) {
                if (b.getBookingID() == bookingID) {
                    targetBooking = b;
                    break;
                }
            }
            if (targetBooking == null) {
                System.out.println("Booking not found.");
                return;
            }

            String currentStatus = targetBooking.getPaymentStatus();
            if ("Paid".equalsIgnoreCase(currentStatus)) {
                System.out.println("Payment is already done for this booking.");
                return;
            } else if ("Pending".equalsIgnoreCase(currentStatus)) {
                System.out.print("Payment is pending. Do you want to make the payment now? (yes/no): ");
                String confirm = scanner.nextLine();
                if ("yes".equalsIgnoreCase(confirm)) {
                    boolean success = bookingService.updatePaymentStatus(bookingID, "Paid");
                    if (success) {
                        System.out.println("Payment done successfully.");
                    } else {
                        System.out.println("Failed to update payment status.");
                    }
                } else {
                    System.out.println("Payment not made.");
                }
            } else {
                System.out.println("Unknown payment status: " + currentStatus);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a numeric Booking ID.");
        }
    }
}
