import auth.Login;
import auth.Register;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import models.Flight;
import models.Passenger;
import services.Bookticket;
import services.FlightService;
import services.MakePayment;
import services.ManageBooking;
import services.ProfileSettings;
import services.SeatService;
import services.TicketPrinter;
import java.util.Deque;
import java.util.ArrayDeque;

public class Main {
    private static Passenger loggedInPassenger = null;

    private enum MenuState {
        MAIN_MENU,
        SERVICES_MENU,
        PROFILE_MENU,
        BOOKING_MENU
    }

    public Main() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Register register = new Register(scanner);
        Login login = new Login(scanner);
        Deque<MenuState> menuStack = new ArrayDeque<>();
        menuStack.push(MenuState.MAIN_MENU);
        welcomeMessage();

        while (!menuStack.isEmpty()) {
            MenuState currentMenu = menuStack.peek();
            switch (currentMenu) {
                case MAIN_MENU:
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
                                menuStack.push(MenuState.SERVICES_MENU);
                            }
                            break;
                        case "3":
                            System.out.println("Exiting from the program. Goodbye!");
                            menuStack.pop();
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    }
                    System.out.println();
                    break;
                case SERVICES_MENU:
                    showServices(scanner, menuStack);
                    break;
                case PROFILE_MENU:
                    profileSettings(scanner, menuStack);
                    break;
                case BOOKING_MENU:
                    manageBookings(scanner, menuStack);
                    break;
            }
        }
    }

    private static void welcomeMessage() {
        System.out.println("\n**************************");
        System.out.println("Welcome to the Flight Schedule Manager");
        System.out.println("Manage your flights, bookings, and check-ins with ease.");
        System.out.println("Please choose an option: Register, Login, or Exit.");
        System.out.println("**************************\n");
    }

    private static void showServices(Scanner scanner, Deque<MenuState> menuStack) {
        FlightService flightService = new FlightService();

        System.out.println();
        System.out.println("=== Available Services ===");
        System.out.println("1. View Flights");
        System.out.println("2. Manage Bookings");
        System.out.println("3. Profile Settings");
        System.out.println("4. Go Back");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                viewFlights(scanner, flightService, loggedInPassenger);
                break;
            case "2":
                menuStack.push(MenuState.BOOKING_MENU);
                break;
            case "3":
                menuStack.push(MenuState.PROFILE_MENU);
                break;
            case "4":
                menuStack.pop();
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 6.");
        }
        System.out.println();
    }

    public static void viewFlights(Scanner scanner, FlightService flightService, Passenger passenger) {
        flightService.displayListFlights();
        System.out.print("Enter source: ");
        String source = scanner.nextLine();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        while (true) {
            System.out.print("Enter date (YYYY-MM-DD): ");
            String dateStr = scanner.nextLine();
            if (flightService.isValidFutureDate(dateStr)) {
                LocalDate date = LocalDate.parse(dateStr);
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

                    Flight flight = flightService.getFlightById(flightId);
                    if (flight != null) {
                        flightService.displayFlightDetails(flight);
                        
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
                            SeatService seatService = new SeatService();
                            LinkedList<Passenger> passengers = bookticket.collectPassengerDetails(numberOfTickets,
                                    scanner,
                                    passenger.getPassengerId());
                            Queue<String> seatQueue = new LinkedList<>();
                            char[][] seatMap = seatService.getOrCreateSeatMap(flightId);
                            seatService.displaySeatMap(seatMap);

                            for (int i = 0; i < numberOfTickets; i++) {
                                System.out.print("Enter seat number for passenger " + (i + 1) + ": ");
                                String seatNumber = seatService.getSeatNumber(scanner);
                                if (!seatService.bookSeat(seatMap, seatNumber)) {
                                    System.out.println("Try again to select seat for passenger " + (i + 1));
                                    i--;
                                } else {
                                    System.out.println("Seat " + seatNumber + " booked for passenger " + (i + 1));
                                    seatQueue.add(seatNumber);
                                }
                            }

                            seatService.saveSeatBookings(flightId, seatMap);
                            String normalizedClass = normalizeFlightClass(flightClass);
                            MakePayment makePayment = new MakePayment();
                            double pricePerTicket = makePayment.getFlightPrice(flight.getFlightId(), normalizedClass,
                                    "data/flights.txt");
                            double layoverSurcharge = calculateLayoverSurcharge(flight, pricePerTicket);
                            makePayment.displayDetailedBill(numberOfTickets, pricePerTicket, layoverSurcharge);
                            boolean paymentSuccess = makePayment.processPayment(scanner,
                                    numberOfTickets * (pricePerTicket + layoverSurcharge));
                            if (paymentSuccess) {
                                String bookingsFile = "data/bookings.txt";
                                java.util.Queue<String> seatQueueCopy = new java.util.LinkedList<>(seatQueue);
                                bookticket.saveBookings(passengers, bookingsFile, seatQueue, flight.getFlightId(),
                                        normalizedClass, pricePerTicket + layoverSurcharge);
                                System.out.println("Booking made successfully!");
                                System.out.print("Enter '1' to print tickets or '2' to go back: ");
                                String postPaymentChoice = scanner.nextLine().trim();
                                if (postPaymentChoice.equals("1")) {

                                    TicketPrinter.printTickets(passengers, flight, normalizedClass, seatQueueCopy);
                                } else if (postPaymentChoice.equals("2")) {
                                    System.out.println("Returning to main menu.");
                                } else {
                                    System.out.println("Invalid choice. Returning to main menu.");
                                }
                            } else {
                                System.out.println("Payment not successful. Please try again later.");
                            }
                        }
                    } else {
                        System.out.println("Flight with ID " + flightId + " not found.");
                    }
                }

                return;
            }

            System.out.println("Please enter a valid future date.");
        }
    }

    private static void profileSettings(Scanner scanner, Deque<MenuState> menuStack) {
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
                    menuStack.clear();
                    menuStack.push(MenuState.MAIN_MENU);
                    break;
                case "4":
                    profileMenuFlag = false;
                    menuStack.pop();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
            }
            System.out.println();
        }
    }

    private static void manageBookings(Scanner scanner, Deque<MenuState> menuStack) {
        ManageBooking bookingService = new ManageBooking();

        System.out.println("\n=== Manage Bookings ===");
        System.out.println("1. View My Bookings");
        System.out.println("2. Cancel Booking");
        System.out.println("3. Go Back");
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
                menuStack.pop();
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 4.");
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

    private static String normalizeFlightClass(String flightClass) {
        switch (flightClass.toLowerCase()) {
            case "economy":
            case "eco":
                return "economy";
            case "business":
            case "bus":
                return "business";
            case "first":
            case "fst":
                return "first";
            default:
                System.out.println("Invalid flight class selected. Defaulting to economy.");
                return "economy";
        }
    }

    private static double calculateLayoverSurcharge(Flight flight, double basePrice) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/flights.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 15) {
                    int id = Integer.parseInt(parts[0].trim());
                    if (id == flight.getFlightId()) {
                        int stops = Integer.parseInt(parts[10].trim());
                        if (stops > 0) {
                            return basePrice * 0.10;
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading layover surcharge: " + e.getMessage());
        }
        return 0.0;
    }
}
