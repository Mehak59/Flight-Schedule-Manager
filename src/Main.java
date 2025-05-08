import auth.Login;
import auth.Register;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;
import models.Booking;
import models.Passenger;
import services.Bookticket;
import services.FlightService;
import services.MakePayment;
import services.ManageBooking;
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
                    if (loggedInPassenger != null) {
                        showServices(scanner);
                    }
                    break;
                case "3":
                    System.out.println("Exiting from the program.Goodbye!");
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
            System.out.println();
        }
    }

    private static void welcomeMessage() {
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
            System.out.println("4. Profile Settings");
            System.out.println("5. Go Back");
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
                    profileSettings(scanner);
                    break;
                case "5":
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
            System.out.println();
        }
    }

    private static void viewFlights(Scanner scanner, FlightService flightService) {
        ManageBooking bookingService = new ManageBooking();
        flightService.displayListFlights();
        System.out.print("Enter source: ");
        String source = scanner.nextLine();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        String dateStr;
        while (true) {
            System.out.print("Enter date (YYYY-MM-DD): ");
            dateStr = scanner.nextLine();
            if (flightService.isValidFutureDate(dateStr)) {
                break;
            }
            System.out.println("Please enter a valid future date.");
        }

        java.time.LocalDate date = java.time.LocalDate.parse(dateStr);

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
            models.Flight flight = flightService.getFlightById(flightId);

            if (flight != null) {
                flightService.displayFlightDetails(flight);
                System.out.println("\n---------------------Prices for this flight------------------------");
                System.out.printf("Economy: Rs.%.2f%n", flight.getPriceEconomy());
                System.out.printf("Business: Rs.%.2f%n", flight.getPriceBusiness());
                System.out.printf("First: Rs.%.2f%n", flight.getPriceFirst());
                System.out.println("-------------------------------------------------------------------\n");
                System.out.print("Do you want to book tickets for this flight? (yes/no): ");
                String bookResponse = scanner.nextLine().trim().toLowerCase();
                if (bookResponse.equals("yes") || bookResponse.equals("y")) {
                    System.out.print("Select class (economy/business/first): ");
                    String flightClass = scanner.nextLine().trim().toLowerCase();
                    System.out.print("Enter number of tickets: ");
                    String ticketsStr = scanner.nextLine().trim();
                    int numberOfTickets;
                    try {
                        numberOfTickets = Integer.parseInt(ticketsStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format for tickets.");
                        return;
                    }
                    Bookticket bookticket = new Bookticket();
                    boolean confirmed = bookticket.confirmBooking(scanner);
                    if (confirmed) {
                        System.out.println(" ");
                        LinkedList<Passenger> passengers = bookticket.collectPassengerDetails(numberOfTickets, scanner, loggedInPassenger.getPassengerId());
                        String bookingSummary = String.format("FlightID: %d, Class: %s, Tickets: %d",
                                flight.getFlightId(), flightClass, numberOfTickets);
                        String filePath = "data/bookings.txt";
                        bookticket.saveBookings(passengers, bookingSummary, filePath);
                        MakePayment makePayment = new MakePayment();
                        // Normalize flightClass to expected values
                        String normalizedClass;
                        switch (flightClass.toLowerCase()) {
                            case "economy":
                            case "eco":
                                normalizedClass = "economy";
                                break;
                            case "business":
                            case "bus":
                                normalizedClass = "business";
                                break;
                            case "first":
                            case "fst":
                                normalizedClass = "first";
                                break;
                            default:
                                System.out.println("Invalid flight class selected. Defaulting to economy.");
                                normalizedClass = "economy";
                        }
                        double pricePerPassenger = makePayment.getFlightPrice(flight.getFlightId(), normalizedClass,
                                "data/flights.txt");
                        double layoverSurcharge = 0.0;
                        // Read layover surcharge from flights.txt for the flight ID
                        try (BufferedReader br = new BufferedReader(new FileReader("data/flights.txt"))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                String[] parts = line.split(",");
                                if (parts.length >= 15) {
                                    int id = Integer.parseInt(parts[0].trim());
                                    if (id == flight.getFlightId()) {
                                        int stops = Integer.parseInt(parts[10].trim());
                                        if (stops > 0) {
                                            // Calculate 10% surcharge as layover surcharge
                                            layoverSurcharge = pricePerPassenger * 0.10;
                                        }
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Error reading layover surcharge: " + e.getMessage());
                        }
                        makePayment.displayDetailedBill(numberOfTickets, pricePerPassenger, layoverSurcharge);

                        boolean paymentSuccess = makePayment.processPayment(scanner,
                                numberOfTickets * (pricePerPassenger + layoverSurcharge));
                        if (paymentSuccess) {
                            System.out.println("Booking made successfully!");
                            System.out.print("Enter '1' to print tickets or '2' to go back: ");
                            String postPaymentChoice = scanner.nextLine().trim();
                            if (postPaymentChoice.equals("1")) {
                                services.TicketPrinter.printTickets(passengers, flight, normalizedClass);
                            } else if (postPaymentChoice.equals("2")) {
                                System.out.println("Returning to main menu.");
                            } else {
                                System.out.println("Invalid choice. Returning to main menu.");
                            }
                        } else {
                            System.out.println("Booking cancelled due to payment failure.");
                        }
                    } else {
                        System.out.println("Booking cancelled.");
                    }
                }
            } else {
                System.out.println("Flight with ID " + flightId + " not found.");
            }
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
                    } else {
                        System.out.println("No user logged in.");
                    }
                    break;
                case "2":
                    if (loggedInPassenger != null) {
                        profileSettings.updateUserDetails(loggedInPassenger);
                    } else {
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
        ManageBooking bookingService = new ManageBooking();
        boolean bookingMenuFlag = true;

        while (bookingMenuFlag) {
            System.out.println("\n=== Manage Bookings ===");
            System.out.println("1. View My Bookings");
            System.out.println("2. Cancel Booking");
            System.out.println("3. Make Payment");
            System.out.println("4. Go Back");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewMyBookings(scanner, bookingService);
                    break;
                case "2":
                    cancelBooking(scanner, bookingService);
                    break;
                case "3":
                    makePayment(scanner, bookingService);
                    break;
                case "4":
                    bookingMenuFlag = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }

    private static void viewMyBookings(Scanner scanner, ManageBooking bookingService) {
        if (loggedInPassenger == null) {
            System.out.println("You must be logged in to view bookings.");
            return;
        }
        System.out.println("Logged in passenger ID: '" + loggedInPassenger.getPassengerId() + "'");
        bookingService.displayBookingsForPassenger(loggedInPassenger.getPassengerId());
    }


    private static void cancelBooking(Scanner scanner, ManageBooking bookingService) {
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

    private static void makePayment(Scanner scanner, ManageBooking bookingService) {
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
