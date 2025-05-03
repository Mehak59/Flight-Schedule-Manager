package services;
import models.Flight;
import models.Airport;
import utils.FlightUtils;
import utils.AirportUtils;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class FlightService {
    private LinkedList<Flight> flights;
    private List<Airport> airports;
    private Map<String, Airport> locationToAirportMap;
    public FlightService() {
        this.flights = new LinkedList<>();
        this.flights.addAll(FlightUtils.loadFlights("data/flights.txt")); 
        this.airports = AirportUtils.loadAirports("data/airport.txt");
        this.locationToAirportMap = new HashMap<>();
        for (Airport airport : airports) {
            locationToAirportMap.put(airport.getLocation().toLowerCase(), airport);
        }
    }

    
    public void displayAvailableFlights(String source, String destination, LocalDate date) {
        LinkedList<Flight> availableFlights = new LinkedList<>();

        for (Flight f : flights) {
            if (f.getFrom() != null && f.getTo() != null &&
                f.getFrom().equalsIgnoreCase(source)
                && f.getTo().equalsIgnoreCase(destination)
                && f.getFlightDate().equals(date)) {
                availableFlights.add(f);
            }
        }

        if (availableFlights.isEmpty()) {
            System.out.println("No flights available for the given route on " + date);
            return;
        }

        System.out.println("Available Flights from " + source + " to " + destination + " on " + date + ":");
        System.out.printf("ID   |   From (Airport ID)    |    To (Airport ID)     |  Depart  |  Arrive  | Duration | Price   | Stops |  Status   \n");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Flight f : availableFlights) {
            Airport fromAirport = locationToAirportMap.get(f.getFrom().toLowerCase());
            Airport toAirport = locationToAirportMap.get(f.getTo().toLowerCase());

            String fromDisplay = f.getFrom();
            if (fromAirport != null) {
                fromDisplay += " (" + fromAirport.getAirportID() + ")";
            }

            String toDisplay = f.getTo();
            if (toAirport != null) {
                toDisplay += " (" + toAirport.getAirportID() + ")";
            }
            System.out.printf("%-4s | %-22s | %-22s | %-8s | %-8s | %-4dmins | Rs.%-6.0f | %-5d | %-9s %n",
                    f.getFlightId(), fromDisplay,
                    toDisplay, f.getDepartureTime(), f.getArrivalTime(),
                    f.getDuration(), f.getPriceEconomy(), f.getStops(), f.getStatus());
        }
    }

    public Flight getFlightById(int flightId) {
        for (Flight f : flights) {
            if (f.getFlightId() == flightId) {
                return f;
            }
        }
        return null;
    }

    public void displayFlightDetails(Flight flight) {
        if (flight == null) {
            System.out.println("Flight details not found.");
            return;
        }
        Airport fromAirport = locationToAirportMap.get(flight.getFrom().toLowerCase());
        Airport toAirport = locationToAirportMap.get(flight.getTo().toLowerCase());

        System.out.println("-------------------------- Flight Details --------------------------");
        System.out.printf("Flight ID        : %d%n", flight.getFlightId());
        System.out.printf("From             : %s%n", flight.getFrom());
        System.out.printf("Airport ID       : %d%n", fromAirport.getAirportID());
        System.out.printf("Airport          : %s%n", fromAirport.getAirportName());
        System.out.printf("To               : %s%n", flight.getTo());
        System.out.printf("Airport ID       : %d%n", toAirport.getAirportID());
        System.out.printf("Airport          : %s%n", toAirport.getAirportName());
        System.out.printf("Departure Time   : %s%n", flight.getDepartureTime());
        System.out.printf("Arrival Time     : %s%n", flight.getArrivalTime());
        System.out.printf("Duration         : %d mins%n", flight.getDuration());
        System.out.printf("Economy Price    : Rs.%.2f%n", flight.getPriceEconomy());
        System.out.printf("Stops            : %d%n", flight.getStops());
        System.out.printf("Status           : %s%n",flight.getStatus());
        int routeTime = flight.getDuration();
        System.out.printf("Route Time       : %d mins%n", routeTime);
        System.out.printf("Check-in Time    : %s%n", flight.getCheckInTime());
        System.out.printf("Layover          : %s%n", flight.getLayover());
        Airport layoverAirport = null;
        if (flight.getLayover() != null && !flight.getLayover().trim().isEmpty()) {
            layoverAirport = locationToAirportMap.get(flight.getLayover().toLowerCase());
        }
        System.out.printf("Layover AirportID: %s%n", layoverAirport != null ? layoverAirport.getAirportID() : "N/A");
        System.out.printf("Layover Airport  : %s%n", layoverAirport != null ? layoverAirport.getAirportName() : "N/A");
        System.out.printf("Layover Time     : %s%n", flight.getLayoverTime());
        System.out.println("-------------------------------------------------------------------");

    }

    public void displayBookingSummary(Flight flight, String flightClass, int numberOfTickets) {
        if (flight == null) {
            System.out.println("Cannot display booking summary: Flight not found.");
            return;
        }

        double basePrice;
        switch (flightClass.toLowerCase()) {
            case "economy":
                basePrice = flight.getPriceEconomy();
                break;
            case "business":
                basePrice = flight.getPriceBusiness();
                break;
            case "first":
                basePrice = flight.getPriceFirst();
                break;
            default:
                System.out.println("Invalid class selected.");
                return;
        }

        double totalPrice = basePrice * numberOfTickets;

        // Add surcharge if flight has stops (layover)
        if (flight.getStops() > 0) {
            double surchargeRate = 0.10; // 10% surcharge for layover
            double surcharge = totalPrice * surchargeRate;
            System.out.println("\n--------------------Layover Surcharge----------------------------");
            System.out.printf("Base price before layover: Rs.%.2f%n",totalPrice);
            totalPrice += surcharge*numberOfTickets;
            System.out.printf("Layover surcharge (10%%): Rs.%.2f%n", surcharge);
            System.out.printf("Total Layover surcharge : Rs.%.2f%n", surcharge*numberOfTickets);
            System.out.println("-------------------------------------------------------------------");
        }

        System.out.println("\n---------------------Booking Summary-------------------------------");
        System.out.printf("Flight ID: %d%n", flight.getFlightId());
        System.out.printf("Class: %s%n", flightClass);
        System.out.printf("Number of Tickets: %d%n", numberOfTickets);
        System.out.printf("Total Price: Rs.%.2f%n", totalPrice);
        System.out.println("-------------------------------------------------------------------");
    }
}
