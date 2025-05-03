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
    private String checkInTime;
    private String layover;
    private String layoverTime;

    public Flight(int flightId, LocalDate flightDate, String from, String to, String departureTime, String arrivalTime,
            int duration, double priceEconomy, double priceBusiness, double priceFirst,
            int stops, String status, String checkInTime, String layover, String layoverTime) {
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
        this.checkInTime = checkInTime;
        this.layover = layover;
        this.layoverTime = layoverTime;
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

    public String getCheckInTime() {
        return checkInTime;
    }

    public String getLayover() {
        return layover;
    }

    public String getLayoverTime() {
        return layoverTime;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public void setFlightDate(LocalDate flightDate) {
        this.flightDate = flightDate;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPriceEconomy(double priceEconomy) {
        this.priceEconomy = priceEconomy;
    }

    public void setPriceBusiness(double priceBusiness) {
        this.priceBusiness = priceBusiness;
    }

    public void setPriceFirst(double priceFirst) {
        this.priceFirst = priceFirst;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public void setLayover(String layover) {
        this.layover = layover;
    }

    public void setLayoverTime(String layoverTime) {
        this.layoverTime = layoverTime;
    }
}
