package models;

public class Booking extends Passenger {
    private int bookingID;
    private int flightID;
    private String bookingDate;
    private String seatNumber;
    private String travelClass;
    private double price;
    private String paymentStatus;

    public Booking(String passengerId, String firstName, String lastName, String dateOfBirth, String phoneNo,
            String email, String nationality, int bookingID, int flightID, String bookingDate, String seatNumber,
            String travelClass, double price, String paymentStatus) {
        super(passengerId, firstName, lastName, dateOfBirth, phoneNo, email, nationality);
        this.bookingID = bookingID;
        this.flightID = flightID;
        this.bookingDate = bookingDate;
        this.seatNumber = seatNumber;
        this.travelClass = travelClass;
        this.price = price;
        this.paymentStatus = paymentStatus;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(String travelClass) {
        this.travelClass = travelClass;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
