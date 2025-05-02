package services;
import models.Flight;
import utils.FlightUtils;
import java.time.LocalDate;
import java.util.LinkedList;


public class FlightService {
    private LinkedList<Flight> flights;
    public FlightService() {
        this.flights = new LinkedList<>();
        this.flights.addAll(FlightUtils.loadFlights("data/flights.txt")); 
    }

    
    public void displayAvailableFlights(String source, String destination, LocalDate date) {
        LinkedList<Flight> availableFlights = new LinkedList<>();

        for (Flight f : flights) {
            if (f.getFrom().equalsIgnoreCase(source)
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
        System.out.printf("ID   |   From    |    To     |  Depart  |  Arrive  | Duration | Price   | Stops |  Layover   \n");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Flight f : availableFlights) {
            String layover = f.getStops() == 0 ? "-" : f.getLayoverTime();
            System.out.printf("%-4s | %-9s | %-9s | %-8s | %-8s | %-4dmins | Rs.%-4.0f | %-5d | %-9s \n",
                    f.getFlightId(), f.getFrom(), f.getTo(), f.getDepartureTime(), f.getArrivalTime(),
                    f.getDuration(), f.getPriceEconomy(), f.getStops(), layover);
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
}
