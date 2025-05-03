package models;

import java.time.LocalDate;

public class Flight {
    private int flightId;
    private LocalDate flightDate;
    private String from;
    private String to;
    private String departureTime;
    private String arrivalTime;
    private int duration;  
    private double priceEconomy;
    private double priceBusiness;
    private double priceFirst;
    private int stops;
    private String status;

    public Flight(int flightId, LocalDate flightDate, String from, String to, String departureTime, String arrivalTime, 
                  int duration, double priceEconomy, double priceBusiness, double priceFirst, 
                  int stops, String status) {
        this.flightId = flightId;
        this.flightDate = flightDate;
        this.from = from;
        this.to = to;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.priceEconomy = priceEconomy;
        this.priceBusiness = priceBusiness;
        this.priceFirst = priceFirst;
        this.stops = stops;
        this.status = status;
    }

    public int getFlightId() {
        return flightId;
    }

    public LocalDate getFlightDate() {
        return flightDate;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public int getDuration() {
        return duration;
    }

    public double getPriceEconomy() {
        return priceEconomy;
    }

    public double getPriceBusiness() {
        return priceBusiness;
    }

    public double getPriceFirst() {
        return priceFirst;
    }

    public int getStops() {
        return stops;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Flight ID: " + flightId +
                " | From: " + from +
                " | To: " + to +
                " | Departure Time: " + departureTime +
                " | Arrival Time: " + arrivalTime +
                " | Duration: " + duration + " mins" +
                " | Economy Price: ₹" + priceEconomy +
                " | Business Price: ₹" + priceBusiness +
                " | First Class Price: ₹" + priceFirst +
                " | Stops: " + stops +
                " | Status: " + status;
    }
}
